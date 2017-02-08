package cloud.algorithms.app;

import cloud.algorithms.cloud.TaskScheduler;
import cloud.algorithms.utils.Config;
import cloud.algorithms.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppScheduler {

    private TaskScheduler taskScheduler = new TaskScheduler();

    private List<App> appList = new ArrayList<App>();

    public void startAppScheduler() {
        Logger.info("Start application scheduler");
        for(App app : appList) {
            try {
                Thread.sleep(app.getArrivalTime());
                taskScheduler.processApp(app);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(!Config.isFinished) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Logger.info("Finish application scheduler");
    }

    public List<App> getAppList() {
        return appList;
    }

    public void setAppList(List<App> appList) {
        this.appList = appList;
    }

    public TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }
}
