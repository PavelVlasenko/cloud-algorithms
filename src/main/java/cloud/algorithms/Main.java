package cloud.algorithms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String ... args) {
        SpringApplication.run(Main.class, args);

        //Generate dag
//        DagGenerator generator = new DagGenerator();
//        Dag dag = generator.createDag(64);
//
//        dag.showDag();
    }
}
