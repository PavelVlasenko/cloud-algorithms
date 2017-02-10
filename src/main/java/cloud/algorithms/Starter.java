package cloud.algorithms;

import cloud.algorithms.app.App;
import cloud.algorithms.app.AppScheduler;
import cloud.algorithms.cloud.CloudManager;
import cloud.algorithms.data.DataProcessor;
import cloud.algorithms.utils.Algorithm;
import cloud.algorithms.utils.Config;
import cloud.algorithms.utils.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Scanner;

@Component
public class Starter {

    @Autowired
    private DataProcessor dp;

    @Autowired
    private AppScheduler appScheduler;

    @Autowired
    private CloudManager cloudManager;

    public void start() {
        //TODO
        //enterParams();
        printParams();
        DataProcessor dp = new DataProcessor();
        //TODO
        List<App> appList = dp.processFile("C:\\Users\\SBT-Vlasenko-PV\\Desktop\\test\\LLNL-Atlas-2006-01.txt");

        cloudManager.processTasks();
        appScheduler.setAppList(appList);
        appScheduler.startAppScheduler();
    }

    private void printParams() {
        System.out.println("======================== CONFIGURATION ==================\r\nAlgorithm type  " + Config.algorithm + "\r\n" +
                "Data file path  " + Config.dataFilePath + "\r\n" +
                "Logger level  " + Config.logLevel + "\r\n" +
                "Advanced reservation applications percentage  " + Config.arPercentage + "\r\n" +
                "Alpha " + Config.alpha + "\r\n" +
                "Arrival gap between applications " + Config.arrivalGap + "\r\n" +
                "The number of tasks in each application " + Config.taskNumb + "\r\n" +
                "======================================================");
    }

    private void enterParams() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the algorithm (enter number):\r\n" +
                "    1. DLS\r\n" +
                "    2. FDLS\r\n" +
                "    3. DMMS\r\n" +
                "    4. FCMMS\r\n");
        int algorithm = Integer.valueOf(scanner.nextLine());
        switch (algorithm) {
            case 1:
                Config.algorithm = Algorithm.DLS;
                break;
            case 2:
                Config.algorithm = Algorithm.FDLS;
                break;
            case 3:
                Config.algorithm = Algorithm.DMMS;
                break;
            case 4:
                Config.algorithm = Algorithm.FCMMS;
                break;
            default:
                Config.algorithm = Algorithm.DLS;
                break;
        }

        System.out.println("Enter data file path:\r\n");
        Config.dataFilePath = scanner.nextLine();
        scanner.close();
    }
}
