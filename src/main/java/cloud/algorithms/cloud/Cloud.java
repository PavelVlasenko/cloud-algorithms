package cloud.algorithms.cloud;

import cloud.algorithms.app.Task;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cloud {

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private LinkedList<Task> arTasks;
    private LinkedList<Task> beTasks;

    private Integer EAT = new Integer(0);
    private double feedbackFactor;

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public LinkedList<Task> getArTasks() {
        return arTasks;
    }

    public void setArTasks(LinkedList<Task> arTasks) {
        this.arTasks = arTasks;
    }

    public Integer getEAT() {
        return EAT;
    }

    public void setEAT(Integer EAT) {
        this.EAT = EAT;
    }

    public double getFeedbackFactor() {
        return feedbackFactor;
    }

    public void setFeedbackFactor(double feedbackFactor) {
        this.feedbackFactor = feedbackFactor;
    }
}
