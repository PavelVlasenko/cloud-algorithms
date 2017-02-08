package cloud.algorithms;

import cloud.algorithms.app.App;
import cloud.algorithms.app.AppScheduler;
import cloud.algorithms.cloud.CloudManager;
import cloud.algorithms.cloud.TaskScheduler;
import cloud.algorithms.data.DataProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@SpringBootApplication
@EnableAsync
public class Main {
    public static void main(String ... args) {
        SpringApplication.run(Main.class, args);

        Starter starter = (Starter)SpringContext.appContext.getBean("starter");
        starter.start();
        Integer i = 9;
    }
}
