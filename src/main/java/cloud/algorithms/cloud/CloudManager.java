package cloud.algorithms.cloud;

import cloud.algorithms.app.AppType;
import cloud.algorithms.app.Task;
import cloud.algorithms.utils.Algorithm;
import cloud.algorithms.Config;
import cloud.algorithms.utils.Logger;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class CloudManager {
    public static Table<Integer, Cloud, Integer> ETM = HashBasedTable.create();
    public static ConcurrentLinkedQueue<Task> beTaskPool = new ConcurrentLinkedQueue<Task>();
    public static ConcurrentLinkedQueue<Task> arTaskPool = new ConcurrentLinkedQueue<Task>();

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
                if(Config.algorithm == Algorithm.DLS || Config.algorithm == Algorithm.FDLS) {
                    Task t = arTaskPool.poll();
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
            if((c.getEAT() + (c.getFeedbackFactor() + 1)*time) < min) {
                result = c;
                min = c.getEAT() + time;
                executionTime = time;
            }
        }
        t.setCloud(result);
        t.setExecutionTime(executionTime);
        result.setEAT(result.getEAT() + executionTime);
        return result;
    }

    private void sendTaskToCloudByMinMinAlgorithm(ConcurrentLinkedQueue<Task> tasks, AppType type) {
        Cloud cloud = null;
        Task task = null;
        Integer executionTime = null;
        Integer minFinishTime = Integer.MAX_VALUE;
        for(Task t : tasks) {
            for(Map.Entry<Cloud, Integer> entry : ETM.row(t.getTaskId()).entrySet()) {
                Cloud entryCloud = entry.getKey();
                Integer entryExecutionTime = entry.getValue();
                Integer finishTime = type == AppType.BEST_EFFORT ? entryCloud.getEAT() + (int)((entryCloud.getFeedbackFactor() + 1)*entryExecutionTime) : entryExecutionTime;
                if(finishTime < minFinishTime) {
                    cloud = entryCloud;
                    executionTime = entryExecutionTime;
                    minFinishTime = finishTime;
                    task = t;
                }
            }
        }
        task.setExecutionTime(executionTime);
        task.setCloud(cloud);
        cloud.setEAT(cloud.getEAT() + executionTime);
        if(type == AppType.BEST_EFFORT) {
            cloud.getBeTasks().add(task);
        }
        else {
            cloud.getArTasks().add(task);
        }
        tasks.remove(task);
    }
}
