package cloud.algorithms;

import cloud.algorithms.dag.Dag;
import cloud.algorithms.dag.DagGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Main {
    public static void main(String ... args) {
        SpringApplication.run(Main.class, args);

        ExecutorService executor = Executors.newSingleThreadExecutor();


        //Generate dag
//        DagGenerator generator = new DagGenerator();
//        Dag dag = generator.createDag(64);
//
//        dag.showDag();
    }
}
