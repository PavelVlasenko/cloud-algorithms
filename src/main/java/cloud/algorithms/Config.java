package cloud.algorithms;

import cloud.algorithms.utils.Algorithm;
import cloud.algorithms.utils.LogLevel;
import cloud.algorithms.utils.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
    public static Algorithm algorithm = Algorithm.DMMS;
    public static String dataFilePath;

    public static LogLevel logLevel = LogLevel.TRACE;

    public static int arPercentage = 50;
    public static double alpha = 1.0;
    public static int arrivalGap = 200;
    public static int taskNumb = 7;
    public static long minTimeUnit = 100;
    public static long exTimeDelimeter = 200;

    public static int allTasks;
    public static int finishedTasks;
    public static boolean isFinished;

    public static void incrementFinishedTasks() {
        finishedTasks++;
        Logger.debug("== Finished " + finishedTasks + " tasks");
        if(finishedTasks == allTasks) {
            isFinished = true;
            Logger.info("*** ALL APPLICATION ARE FINISHED ***");
        }
    }
    @Value("${alpha}")
    public void setAlpha(double alpha) {
        Config.alpha = alpha;
    }
    @Value("${arrival.gap}")
    public void setArrivalGap(int arrivalGap) {
        Config.arrivalGap = arrivalGap;
    }
    @Value("${task.numb}")
    public void setTaskNumb(int taskNumb) {
        Config.taskNumb = taskNumb;
    }
    @Value("${min.time.unit}")
    public void setMinTimeUnit(long minTimeUnit) {
        Config.minTimeUnit = minTimeUnit;
    }
    @Value("${ex.time.delimeter}")
    public void setExTimeDelimeter(long exTimeDelimeter) {
        Config.exTimeDelimeter = exTimeDelimeter;
    }
    @Value("${ar.percentage}")
    public void setArPercentage(int arPercentage) {
        Config.arPercentage = arPercentage;
    }
}
