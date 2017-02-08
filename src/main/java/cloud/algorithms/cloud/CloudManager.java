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
                Cloud cloud = calculateCloudForArTask(t);
                cloud.getArTasks().add(t);
            }
            else  if(!beTaskPool.isEmpty()){
                if(Config.algorithm == Algorithm.DLS) {
                    Task t = beTaskPool.poll();
                    Cloud cloud = calculateCloudForBeTask(t);
                    cloud.getBeTasks().add(t);
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
        Integer min = 0;
        Cloud cloud = null;
        for(Map.Entry<Cloud, Integer> entry : ETM.row(t.getTaskId()).entrySet()) {
            if(entry.getValue() > min) {
                cloud = entry.getKey();
                min = entry.getValue();
            }
        }
        return cloud;
    }

    private Cloud calculateCloudForBeTask(Task t) {
        Integer min = 0;
        Cloud result = null;
        for(Map.Entry<Cloud, Integer> entry : ETM.row(t.getTaskId()).entrySet()) {
            Cloud c = entry.getKey();
            Integer time = entry.getValue();
            if(c.getEAT() + time > min) {
                result = c;
                min = c.getEAT() + time;
            }
        }
        t.setCloud(result);
        t.setExecutionTime(min);
        result.setEAT(result.getEAT() + min);
        return result;
    }
}
