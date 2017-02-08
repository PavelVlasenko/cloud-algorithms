package cloud.algorithms.app;

import cloud.algorithms.cloud.Cloud;
import cloud.algorithms.utils.Config;

import java.util.HashSet;
import java.util.Set;

public class Task implements Runnable {
    private int taskId;
    private int executionTime;
    private boolean isFinished;
    private AppType type;
    private Cloud cloud ;

    public Set<Task> predecessors = new HashSet<Task>();
    public Set<Task> successors = new HashSet<Task>();

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long finTime = startTime + executionTime;
        while (System.currentTimeMillis() < finTime) {
            try {
                Thread.sleep(Config.minTimeUnit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setFinished(true);
        getCloud().setFeedbackFactor((System.currentTimeMillis() - startTime)/executionTime);
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

    public AppType getType() {
        return type;
    }

    public void setType(AppType type) {
        this.type = type;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Cloud getCloud() {
        return cloud;
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public Set<Task> getPredecessors() {
        return predecessors;
    }

    public Set<Task> getSuccessors() {
        return successors;
    }
}
