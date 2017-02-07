package cloud.algorithms.app;

import java.util.LinkedList;
import java.util.Queue;

public class App {
    private LinkedList<Task> tasks = new LinkedList<Task>();
    private long arrivalTime;
    private AppType type;

    public AppType getType() {
        return type;
    }

    public void setType(AppType type) {
        this.type = type;
    }

    public LinkedList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(LinkedList<Task> tasks) {
        this.tasks = tasks;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
