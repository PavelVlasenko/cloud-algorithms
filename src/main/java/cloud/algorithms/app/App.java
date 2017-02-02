package cloud.algorithms.app;

import cloud.algorithms.dag.Edge;

import java.util.List;
import java.util.Queue;
import java.util.Set;

public class App {
    public Queue<Task> tasks;
    public Set<Edge> taskDependencies;

    private AppType type;

    public AppType getType() {
        return type;
    }

    public void setType(AppType type) {
        this.type = type;
    }
}
