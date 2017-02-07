package cloud.algorithms.app;

import cloud.algorithms.cloud.TaskScheduler;

import java.util.Map;

public class AppScheduler {

    TaskScheduler taskScheduler;

    Map<Long, App> appShedule;

    public void startAppScheduler() {
        System.out.println("Start application scheduler");
        for(Map.Entry<Long, App> entry : appShedule.entrySet()) {
            try {
                Thread.sleep(entry.getKey());
                taskScheduler.processApp(entry.getValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Finish application scheduler");
    }

}
