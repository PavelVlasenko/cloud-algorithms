package cloud.algorithms.data;

import cloud.algorithms.SpringContext;
import cloud.algorithms.app.App;
import cloud.algorithms.app.AppType;
import cloud.algorithms.app.Task;
import cloud.algorithms.cloud.Cloud;
import cloud.algorithms.cloud.CloudManager;
import cloud.algorithms.dag.*;
import cloud.algorithms.utils.Config;
import cloud.algorithms.utils.Logger;
import com.google.common.collect.Table;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DataProcessor {

    @Lookup("Cloud")
    public Cloud getCloud() {
        return null;
    }

    public List<App> processFile(String path) {
        Logger.info("=== Start process file in path " + path);
        List<App> apps = new ArrayList<App>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String s;
            App app = new App();
            int count = 0;
            while ((s = br.readLine()) != null) {
                String jobState = StringUtils.substringBetween(s, "JobState=", " ");
                String jobId = StringUtils.substringBetween(s, "JobId=", " ");
                String startTime = StringUtils.substringBetween(s, "StartTime=", " ");
                String endTime = StringUtils.substringBetween(s, "EndTime=", " ");
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-hh'T'HH:mm:ss");
                if(!"COMPLETED".equals(jobState) || startTime.equals(endTime)) {
                    continue;
                }
                Task task = new Task();
                try {
                    Date startDate = sd.parse(startTime);
                    if(count == 0) {
                        app.setArrivalTime(startDate.getTime());
                    }
                    Date endDate = sd.parse(endTime);
                    long executionTime = endDate.getTime() - startDate.getTime();
                    if(executionTime < 0) {
                        continue;
                    }
                    task.setExecutionTime((int)executionTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                task.setTaskId(Integer.valueOf(jobId));
                app.getTasks().add(task);
                count++;
                if(count == 64) {
                    apps.add(app);
                    app = new App();
                    count = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.info("=== Process file in path " + path + " is finished");

        Logger.info("=== Start setting application type, AR app percentage = " + Config.arPercentage);
        int appSize = apps.size();
        List<Integer> arApps = new ArrayList<Integer>();
        for(int i = 0; i < appSize*Config.arPercentage/100.0; i++) {
            arApps.add(new Random().nextInt(appSize-1));
        }

        for(int i = 0; i< appSize; i++) {
            if(arApps.contains(i)) {
                apps.get(i).setType(AppType.ADVANCE_RESERVATION);
            }
            else {
                apps.get(i).setType(AppType.BEST_EFFORT);
            }
        }
        for(App app : apps) {
            for(Task t : app.getTasks()) {
                t.setType(app.getType());
            }
        }

        Logger.info("=== Set application arrival time");
        long firstArrivalTime = apps.get(0).getArrivalTime();
        for(App app : apps) {
            app.setArrivalTime(app.getArrivalTime() - firstArrivalTime);
        }
        Logger.info("=== Merge dag with application");

        DagGenerator dagGenerator = new DagGenerator();

        for(App app : apps) {
            Dag dag = dagGenerator.createDag(Config.taskNumb);
            for(int i=0; i<app.getTasks().size(); i++) {
                dag.vertices.get(i).averageExecutionTime = app.getTasks().get(i).getExecutionTime();
            }
            DagProcessor dagProcessor = new DagProcessor(dag);
            dagProcessor.calculateEST();
            dagProcessor.calculateLST();
            dagProcessor.calculateTaskList();

            Map<Integer, Integer> idMap = new LinkedHashMap<Integer,Integer>();
            int i = 0;
            for(Task t :app.getTasks()) {
                idMap.put(i, t.getTaskId());
                i++;
            }

            LinkedList<Task> prioritizedList = new LinkedList<Task>();
            for(Vertex v: dag.vertices) {
                Task t = app.getTaskById(idMap.get(v.index));
                prioritizedList.add(t);
            }
            app.setTasks(prioritizedList);

            for(Edge e : dag.edges) {
                Task pre = app.getTaskById(idMap.get(e.startVertex));
                Task suc = app.getTaskById(idMap.get(e.endVertex));

                pre.getSuccessors().add(suc);
                suc.getPredecessors().add(pre);
            }
        }

        Logger.info("=== Data parsing from file " + path + " is finished");
        Logger.info("=== Generated " + apps.size() + " applications");

        Logger.info("=== Generate ETM matrix");
        Cloud cloud1 = (Cloud)SpringContext.appContext.getBean("cloud");
        Cloud cloud2 = (Cloud)SpringContext.appContext.getBean("cloud");
        Cloud cloud3 = (Cloud)SpringContext.appContext.getBean("cloud");

        cloud1.setCloudId(1);
        cloud1.setCloudId(2);
        cloud1.setCloudId(3);

        cloud1.runCloud();
        cloud1.runCloud();
        cloud1.runCloud();

        Table<Integer, Cloud, Integer> etm = CloudManager.ETM;
        for(App app :apps) {
            for(Task t : app.getTasks()) {
                int avTime = t.getExecutionTime();
                int range = avTime/2;
                etm.put(t.getTaskId(), cloud1, range + new Random().nextInt(avTime));
                etm.put(t.getTaskId(), cloud2, range + new Random().nextInt(avTime));
                etm.put(t.getTaskId(), cloud3, range + new Random().nextInt(avTime));
            }
        }
        Logger.info("=== Generating ETM matrix is finished");

        return apps;
    }
}
