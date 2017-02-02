package cloud.algorithms.app;

import java.util.HashSet;
import java.util.Set;

public class Task implements Runnable {
    private int taskId;
    private int executionTime;
    private boolean isFinished;

    public Set<Task> predecessors = new HashSet<Task>();
    public Set<Task> successors = new HashSet<Task>();

    @Override
    public void run() {

    }

    public int getTaskId() {
        return taskId;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
