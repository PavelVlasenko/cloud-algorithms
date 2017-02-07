package cloud.algorithms.utils;

public class Logger {
    public static void debug(String s) {
        if(Config.logLevel == LogLevel.DEBUG || Config.logLevel == LogLevel.TRACE)
        System.out.println(s);
    }

    public static void info(String s) {
        if(Config.logLevel == LogLevel.INFO || Config.logLevel == LogLevel.DEBUG || Config.logLevel == LogLevel.TRACE)
            System.out.println(s);
    }

    public static void trace(String s) {
        if(Config.logLevel == LogLevel.TRACE )
            System.out.println(s);
    }
}
