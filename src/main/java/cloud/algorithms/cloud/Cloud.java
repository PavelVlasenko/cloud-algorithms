package cloud.algorithms.cloud;

import cloud.algorithms.app.Task;
import cloud.algorithms.utils.Config;
import cloud.algorithms.utils.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Scope("prototype")
public class Cloud {

    private int cloudId;
    private LinkedList<Task> arTasks = new LinkedList<Task>();
    private LinkedList<Task> beTasks = new LinkedList<Task>();

    private Integer EAT = new Integer(0);
    private double feedbackFactor;

    @Async
    public void runCloud() {
        Logger.info("=== Start Cloud processor in Thread " + Thread.currentThread().getId());
        while(!Config.isFinished) {
            if(!arTasks.isEmpty()) {
                Task t = arTasks.poll();
                processArTask(t);
            }
            else if(!beTasks.isEmpty()) {
                Task t = beTasks.poll();
                processBeTask(t);
            }
            else {
                try {
                    Thread.sleep(Config.minTimeUnit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processArTask(Task t) {
        long startTime = System.currentTimeMillis();
        long finishTime = System.currentTimeMillis() + t.getExecutionTime();
        boolean isFinished = false;
        while(!isFinished) {
            if(System.currentTimeMillis() > finishTime) {
                Logger.trace("AR Task " + t.getTaskId() + " isFinished");
                t.setFinished(true);
                return;
            }
            else {
                try {
                    Thread.sleep(Config.minTimeUnit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processBeTask(Task t) {
        long startTime = System.currentTimeMillis();
        long finishTime = System.currentTimeMillis() + t.getExecutionTime();
        boolean isFinished = false;
        while(!isFinished) {
            if(!arTasks.isEmpty()) {
                Task arTask = arTasks.poll();
                finishTime+= arTask.getExecutionTime();
                processArTask(arTask);
            }
            else if(System.currentTimeMillis() > finishTime) {
                Logger.trace("BE Task " + t.getTaskId() + " isFinished");
                t.setFinished(true);
                t.getCloud().setFeedbackFactor((finishTime - startTime)/t.getExecutionTime());
                return;
            }
            else {
                try {
                    Thread.sleep(Config.minTimeUnit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public LinkedList<Task> getArTasks() {
        return arTasks;
    }

    public LinkedList<Task> getBeTasks() {
        return beTasks;
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

    public int getCloudId() {
        return cloudId;
    }

    public void setCloudId(int cloudId) {
        this.cloudId = cloudId;
    }
}
