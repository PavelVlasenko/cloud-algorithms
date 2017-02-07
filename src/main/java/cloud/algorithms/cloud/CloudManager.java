package cloud.algorithms.cloud;

import cloud.algorithms.app.AppType;
import cloud.algorithms.app.Task;
import cloud.algorithms.utils.Config;
import com.google.common.collect.Table;

import java.util.LinkedList;
import java.util.Map;

public class CloudManager {
    public static Table<Integer, Cloud, Integer> ETM;
    public static LinkedList<Task> beTaskPool;
    public static LinkedList<Task> arTaskPool;

    public void addTask(Task task)
    {
        if(task.getType() == AppType.BEST_EFFORT) {
            beTaskPool.add(task);
        }
        else {
            arTaskPool.add(task);
        }
    }

    public void processTasks() {
        while(!Config.isFinished) {
            if(!arTaskPool.isEmpty()) {
                Task t = arTaskPool.poll();
                Cloud cloud = calculateCloudForArTask(t);
                cloud.getArTasks().add(t);
            }
            else {
                Task t = beTaskPool.poll();
                Cloud cloud = calculateCloudForBeTask(t);

            }
        }
    }

    private Cloud calculateCloudForArTask(Task t) {
        Integer min = 0;
        Cloud result = null;
        for(Map.Entry<Cloud, Integer> entry : ETM.row(t.getTaskId()).entrySet()) {
            if(entry.getValue() > min) {
                result = entry.getKey();
            }
        }
        return result;
    }

    private Cloud calculateCloudForBeTask(Task t) {
        Integer min = 0;
        Cloud result = null;
        for(Map.Entry<Cloud, Integer> entry : ETM.row(t.getTaskId()).entrySet()) {
            Cloud c = entry.getKey();
            Integer time = entry.getValue();
            if(c.getEAT() + time > min) {
                result = c;
            }
        }
        t.setCloud(result);
        t.setExecutionTime(min);
        result.setEAT(result.getEAT() + min);
        return result;
    }
}
