package cloud.algorithms.app;

import java.util.HashSet;
import java.util.Set;

public class Task {
    private int taskId;
    private int executionTime;
    private boolean isFinished;

    public Set<Task> predecessors = new HashSet<Task>();
    public Set<Task> successors = new HashSet<Task>();

}
