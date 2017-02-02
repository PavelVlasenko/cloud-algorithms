package cloud.algorithms.cloud;

import cloud.algorithms.app.App;
import cloud.algorithms.app.AppScheduler;
import cloud.algorithms.app.AppType;
import cloud.algorithms.app.Task;
import cloud.algorithms.settings.Algorithm;
import cloud.algorithms.settings.Settings;
import com.google.common.collect.Table;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Queue;

public class CloudScheduler {

    public static Table<Integer, Cloud, Integer> ETM;
    public static Queue<Task> tasks;

    private void distributeTask() {

    }

    public Queue<Task> getTasks() {
        return tasks;
    }

    public void processTask() {
        while(!tasks.isEmpty()) {
            if(Settings.algorithm == Algorithm.CLS) {
                Task task = tasks.poll();
                Cloud cloud = getCloudByMinERAT(task.getTaskId());
                cloud.executor.execute(task);
            }
        }
    }

    private Cloud getCloudByMinERAT(int taskId) {


    }
}
