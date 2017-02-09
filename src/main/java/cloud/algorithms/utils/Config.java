package cloud.algorithms.utils;

public class Config {
    public static Algorithm algorithm = Algorithm.DLS;
    public static double arPercentage = 50;
    public static double alpha;
    public static int arrivalGap;

    public static long minTimeUnit = 500;
    public static boolean isFinished;
    public static LogLevel logLevel = LogLevel.TRACE;

    public static int taskNumb = 64;
    public static int allTasks;

    public static int timeDelimeter = 500;

    public static int finishedTasks;

    public static void incrementFinishedTasks() {
        finishedTasks++;
        Logger.debug("== Finished " + finishedTasks + " tasks");
        if(finishedTasks == allTasks) {
            isFinished = true;
            Logger.info("*** ALL APPLICATION ARE FINISHED ***");
        }
    }
}
