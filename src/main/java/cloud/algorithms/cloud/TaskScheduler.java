package cloud.algorithms.cloud;

import cloud.algorithms.app.App;
import cloud.algorithms.app.Task;
import cloud.algorithms.settings.Config;
import org.springframework.scheduling.annotation.Async;

public class TaskScheduler {

    CloudManager cloudManager;

    @Async
    public void processApp(App app) {
        while(!app.tasks.isEmpty()) {
            Task task = app.tasks.poll();
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
