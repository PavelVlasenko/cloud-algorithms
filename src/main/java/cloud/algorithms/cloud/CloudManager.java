package cloud.algorithms.cloud;

import cloud.algorithms.app.AppType;
import cloud.algorithms.app.Task;
import cloud.algorithms.utils.Algorithm;
import cloud.algorithms.utils.Config;
import cloud.algorithms.utils.Logger;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;

@Component
public class CloudManager {
    public static Table<Integer, Cloud, Integer> ETM = HashBasedTable.create();
    public static LinkedList<Task> beTaskPool = new LinkedList<Task>();
    public static LinkedList<Task> arTaskPool = new LinkedList<Task>();

    public void addTask(Task task)
    {
        Logger.debug("== Adding task" + task.getTaskId() + " to cloud manger");
        if(task.getType() == AppType.BEST_EFFORT) {
            beTaskPool.add(task);
        }
        else {
            arTaskPool.add(task);
        }
    }

    @Async
    public void processTasks() {
        Logger.debug("== Process tasks in thread " + Thread.currentThread().getId());
        while(!Config.isFinished) {
            if(!arTaskPool.isEmpty()) {
                Task t = arTaskPool.poll();
                if(Config.algorithm == Algorithm.DLS || Config.algorithm == Algorithm.FDLS) {
                    Cloud cloud = calculateCloudForArTask(t);
                    cloud.getArTasks().add(t);
                }
                else {
                    sendTaskToCloudByMinMinAlgorithm(arTaskPool, AppType.ADVANCE_RESERVATION);
                }
            }
            else  if(!beTaskPool.isEmpty()){
                if(Config.algorithm == Algorithm.DLS || Config.algorithm == Algorithm.FDLS) {
                    Task t = beTaskPool.poll();
                    Cloud cloud = calculateCloudForBeTask(t);
                    cloud.getBeTasks().add(t);
                }
                else {
                    sendTaskToCloudByMinMinAlgorithm(beTaskPool, AppType.BEST_EFFORT);
                }
            }
            else {
                try {
                    Thread.sleep(Config.minTimeUnit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Cloud calculateCloudForArTask(Task t) {
        Integer min = Integer.MAX_VALUE;
        Cloud cloud = null;
        for(Map.Entry<Cloud, Integer> entry : ETM.row(t.getTaskId()).entrySet()) {
            if(entry.getValue() < min) {
                cloud = entry.getKey();
                min = entry.getValue();
            }
        }
        t.setExecutionTime(min);
        cloud.setEAT(cloud.getEAT() + min);
        return cloud;
    }

    private Cloud calculateCloudForBeTask(Task t) {
        Integer min = Integer.MAX_VALUE;
        Integer executionTime = 0;
        Cloud result = null;
        for(Map.Entry<Cloud, Integer> entry : ETM.row(t.getTaskId()).entrySet()) {
            Cloud c = entry.getKey();
            Integer time = entry.getValue();
            if((c.getEAT() + c.getFeedbackFactor()*time) < min) {
                result = c;
                min = c.getEAT() + time;
                executionTime = time;
            }
        }
        t.setCloud(result);
        t.setExecutionTime(executionTime);
        result.setEAT(result.getEAT() + min);
        return result;
    }

    private void sendTaskToCloudByMinMinAlgorithm(LinkedList<Task> tasks, AppType type) {
        Cloud cloud = null;
        Task task = null;
        Integer executionTime = null;
        Integer minExecutionTime = Integer.MAX_VALUE;
        for(Task t : tasks) {
            for(Map.Entry<Cloud, Integer> entry : ETM.row(t.getTaskId()).entrySet()) {
                Cloud entryCloud = entry.getKey();
                Integer entryTime = entry.getValue();
                Integer exTime = type == AppType.BEST_EFFORT ? entryCloud.getEAT() + (int)entryCloud.getFeedbackFactor()*entryTime : entryTime;
                if(exTime < minExecutionTime) {
                    cloud = entryCloud;
                    executionTime = entryTime;
                    task = t;
                }
            }
        }
        tasks.remove(task);
        task.setExecutionTime(executionTime);
        cloud.setEAT(cloud.getEAT() + executionTime);
        if(type == AppType.BEST_EFFORT) {
            cloud.getBeTasks().add(task);
        }
        else {
            cloud.getArTasks().add(task);
        }
    }
}
