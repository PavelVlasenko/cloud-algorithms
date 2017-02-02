package cloud.algorithms.app;

import cloud.algorithms.cloud.CloudManager;
import cloud.algorithms.cloud.CloudScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

public class AppScheduler {

    CloudScheduler cloudScheduler;

    Map<Long, App> appShedule;

    public void startAppScheduler() {
        System.out.println("Start application scheduler");
        for(Map.Entry<Long, App> entry : appShedule.entrySet()) {
            try {
                Thread.sleep(entry.getKey());
                sendTasksToCloudScheduler(entry.getValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Finish application scheduler");
    }

    @Async
    public void sendTasksToCloudScheduler(App app) {
        System.out.println("Start application");
        if(app.getType() == AppType.BEST_EFFORT) {
            while(!app.tasks.isEmpty()) {
                Task task = app.tasks.poll();
                while(!task.predecessors.isEmpty()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }

        }
        System.out.println("Application is finished");
    }

}
