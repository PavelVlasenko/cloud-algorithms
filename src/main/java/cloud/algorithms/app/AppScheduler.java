package cloud.algorithms.app;

import cloud.algorithms.cloud.TaskScheduler;
import cloud.algorithms.utils.Config;
import cloud.algorithms.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class AppScheduler {

    @Autowired
    private TaskScheduler taskScheduler;

    private List<App> appList = new ArrayList<App>();

    public void startAppScheduler() {
        Date start = new Date();
        Logger.info("=== Start applications at " + start);
        Config.allTasks = appList.size()*appList.get(0).getTasks().size();
        for(App app : appList) {
            try {
                //Thread.sleep(app.getArrivalTime());
                Thread.sleep(0);
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
        Date finish = new Date();
        Logger.info("*** Finish applications at " + finish);
        Logger.info("*** APPLICATION PROCESSING TAKES " + (finish.getTime() - start.getTime())/1000 + " seconds");
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
