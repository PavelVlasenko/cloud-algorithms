package cloud.algorithms.app;

import java.util.Queue;

public class App {
    public Queue<Task> tasks;

    private AppType type;

    public AppType getType() {
        return type;
    }

    public void setType(AppType type) {
        this.type = type;
    }
}
