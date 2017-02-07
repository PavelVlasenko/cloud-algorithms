package cloud.algorithms.cloud;

import cloud.algorithms.app.App;
import cloud.algorithms.app.Task;
import cloud.algorithms.utils.Config;
import org.springframework.scheduling.annotation.Async;

public class TaskScheduler {

    CloudManager cloudManager;

    @Async
    public void processApp(App app) {
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
        return true;
    }
}
