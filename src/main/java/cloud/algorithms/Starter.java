package cloud.algorithms;

import cloud.algorithms.app.App;
import cloud.algorithms.app.AppScheduler;
import cloud.algorithms.cloud.CloudManager;
import cloud.algorithms.data.DataProcessor;
import cloud.algorithms.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Starter {

    @Autowired
    private DataProcessor dp;

    @Autowired
    private AppScheduler appScheduler;

    @Autowired
    private CloudManager cloudManager;

    //@PostConstruct
    public void start() {
        Logger.debug("== Main starter in thread " + Thread.currentThread().getId());
        DataProcessor dp = new DataProcessor();
        List<App> appList = dp.processFile("C:\\Users\\SBT-Vlasenko-PV\\Desktop\\test\\LLNL-Atlas-2006-0.txt");

        cloudManager.processTasks();

        appScheduler.setAppList(appList);
        appScheduler.startAppScheduler();

        Integer i = 9;
    }
}
