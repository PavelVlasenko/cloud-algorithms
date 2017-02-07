package cloud.algorithms;

import cloud.algorithms.data.DataProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class Main {
    public static void main(String ... args) {
        //SpringApplication.run(Main.class, args);

        DataProcessor dp = new DataProcessor();
        dp.processFile("C:\\Users\\SBT-Vlasenko-PV\\Desktop\\test\\LLNL-Atlas-2006-0.txt");
    }
}
