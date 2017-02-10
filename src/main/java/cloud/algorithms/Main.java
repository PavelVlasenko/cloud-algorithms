package cloud.algorithms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Main {
    public static void main(String ... args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        Starter starter = (Starter)SpringContext.appContext.getBean("starter");
        starter.start();
    }
}
