package cloud.algorithms.app;

import cloud.algorithms.cloud.TaskScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppScheduler {

    private TaskScheduler taskScheduler;

    private List<App> appList = new ArrayList<App>();

    public void startAppScheduler() {
        System.out.println("Start application scheduler");
        for(App app : appList) {
            try {
                Thread.sleep(app.getArrivalTime());
                taskScheduler.processApp(entry.getValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Finish application scheduler");
    }

    public List<App> getAppList() {
        return appList;
    }

    public void setAppList(List<App> appList) {
        this.appList = appList;
    }
}
