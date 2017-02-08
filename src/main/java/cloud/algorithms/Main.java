package cloud.algorithms;

import cloud.algorithms.app.App;
import cloud.algorithms.app.AppScheduler;
import cloud.algorithms.cloud.CloudManager;
import cloud.algorithms.cloud.TaskScheduler;
import cloud.algorithms.data.DataProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String ... args) {
        SpringApplication.run(Main.class, args);

//        DataProcessor dp = new DataProcessor();
//        List<App> appList = dp.processFile("C:\\Users\\SBT-Vlasenko-PV\\Desktop\\test\\LLNL-Atlas-2006-0.txt");
//
//        AppScheduler appScheduler = new AppScheduler();
//        TaskScheduler taskScheduler = appScheduler.getTaskScheduler();
//        CloudManager cloudManager = taskScheduler.getCloudManager();
//        cloudManager.processTasks();
//
//        appScheduler.setAppList(appList);
//        appScheduler.startAppScheduler();
    }
}
