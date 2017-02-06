package cloud.algorithms.cloud;

import cloud.algorithms.app.App;
import cloud.algorithms.app.Task;
import cloud.algorithms.settings.Algorithm;
import cloud.algorithms.settings.Config;
import com.google.common.collect.Table;
import org.springframework.scheduling.annotation.Async;

import java.util.LinkedList;
import java.util.Queue;

public class CloudScheduler {

    public static Table<Integer, Cloud, Integer> ETM;
    public static LinkedList<Task> beTaskPool;
    public static LinkedList<Task> beTaskPool;



    private void distributeTask() {
    }

    public Queue<Task> getTasks() {
        return tasks;
    }

    @Async
    public void processApp(App app) {
        while(!app.tasks.isEmpty()) {
            Task task = app.tasks.poll();
            distributeTask(task);
        }
        System.out.println("App is finished");
        Config.finishedAppCount++;
        beTaskPool.
        tasks.remove()
    }

    private boolean distributeTask(Task t) {

    }

    public void processTask() {
        while(!tasks.isEmpty()) {
            if(Config.algorithm == Algorithm.CLS) {
                Task task = tasks.poll();
                Cloud cloud = getCloudByMinERAT(task.getTaskId());
                cloud.executor.execute(task);
            }
        }
    }

}
