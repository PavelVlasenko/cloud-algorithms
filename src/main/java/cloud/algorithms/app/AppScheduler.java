package cloud.algorithms.app;

import cloud.algorithms.cloud.TaskScheduler;

import java.util.Map;

public class AppScheduler {

    TaskScheduler cloudScheduler;

    Map<Long, App> appShedule;

    public void startAppScheduler() {
        System.out.println("Start application scheduler");
        for(Map.Entry<Long, App> entry : appShedule.entrySet()) {
            try {
                Thread.sleep(entry.getKey());
                cloudScheduler.processApp(entry.getValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Finish application scheduler");
    }

//    @Async
//    public void sendTasksToCloudScheduler(App app) {
//        System.out.println("Start application");
//        if(app.getType() == AppType.BEST_EFFORT) {
//            while(!app.tasks.isEmpty()) {
//                Task task = app.tasks.poll();
//                while(!task.predecessors.isEmpty()) {
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                cloudScheduler.getTasks().add(task);
//            }
//        }
//        System.out.println("Application is finished");
//    }

}
