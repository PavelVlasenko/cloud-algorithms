package cloud.algorithms.cloud;

import cloud.algorithms.app.Task;
import cloud.algorithms.utils.Algorithm;
import cloud.algorithms.Config;
import cloud.algorithms.utils.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
@Scope("prototype")
public class Cloud {

    private int cloudId;
    private LinkedList<Task> arTasks = new LinkedList<Task>();
    private LinkedList<Task> beTasks = new LinkedList<Task>();

    private Integer EAT = 0;
    private double feedbackFactor = 0.0d;

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
        Logger.trace("= Start AR task id " + t.getTaskId() + " in cloud " + getCloudId() + ", execution time " + t.getExecutionTime());
        try {
            Thread.sleep(t.getExecutionTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Logger.trace("= AR Task " + t.getTaskId() + " isFinished");
        t.setFinished(true);
        Config.incrementFinishedTasks();
    }

    private void processBeTask(Task t) {
        Logger.trace("= Start BE task id " + t.getTaskId() + " in cloud " + getCloudId()  + ", execution time " + t.getExecutionTime());
        long startTime = System.currentTimeMillis();
        long expectedFinishTime = startTime + t.getExecutionTime();
        long actualFinishTime = expectedFinishTime;
        while(true) {
            if(!arTasks.isEmpty()) {
                Task arTask = arTasks.poll();
                Logger.trace("= BE task " + t.getTaskId() +  " is interrupted by AR task " + arTask.getTaskId());
                actualFinishTime+= arTask.getExecutionTime();
                processArTask(arTask);
            }
            else if(System.currentTimeMillis() > actualFinishTime) {
                Logger.trace("= BE Task " + t.getTaskId() + " isFinished");
                t.setFinished(true);
                Config.incrementFinishedTasks();
                break;
            }
            else {
                try {
                    Thread.sleep(Config.minTimeUnit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if(Config.algorithm == Algorithm.FDMMS || Config.algorithm == Algorithm.FDLS) {
            calculateFeedBackFactor(startTime, expectedFinishTime, actualFinishTime);
        }
    }

    private void calculateFeedBackFactor(long startTime, long expectedFinishTime, long actualFinishTime) {
        double expectedTime = (double)(expectedFinishTime - startTime);
        double result = Config.alpha * ((actualFinishTime - startTime - expectedTime) / expectedTime);
        if(result > 3) {
            return;
        }
        feedbackFactor = result;
        Logger.trace("= Set feedback factor " + feedbackFactor + " in cloud " + cloudId);
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
