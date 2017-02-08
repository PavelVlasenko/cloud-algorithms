package cloud.algorithms.cloud;

import cloud.algorithms.app.App;
import cloud.algorithms.app.Task;
import cloud.algorithms.utils.Config;
import cloud.algorithms.utils.Logger;
import org.springframework.scheduling.annotation.Async;

public class TaskScheduler {

    private CloudManager cloudManager = new CloudManager();

    @Async
    public void processApp(App app) {
        Logger.debug("==Task scheduler receive app");
        while(!app.getTasks().isEmpty()) {
            Task task = app.getTasks().poll();
            distributeTask(task);
        }
        System.out.println("App is finished");
        Config.finishedAppCount++;
    }

    private void distributeTask(Task task) {
        if(checkPredecessorsIsFinished(task)) {
            cloudManager.addTask(task);
            return;
        }
        try {
            Thread.sleep(Config.minTimeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        distributeTask(task);
    }

    private boolean checkPredecessorsIsFinished(Task task) {
        for(Task t : task.getPredecessors()) {
            if(!t.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public CloudManager getCloudManager() {
        return cloudManager;
    }
}
