package cloud.algorithms.utils;

public class Config {
    public static Algorithm algorithm = Algorithm.DLS;
    public static String dataFilePath;

    public static LogLevel logLevel = LogLevel.TRACE;

    public static double arPercentage = 50;
    public static double alpha = 1;
    public static int arrivalGap = 0;
    public static long minTimeUnit = 500;
    public static int taskNumb = 7;
    public static int timeDelimeter = 500;

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
}
