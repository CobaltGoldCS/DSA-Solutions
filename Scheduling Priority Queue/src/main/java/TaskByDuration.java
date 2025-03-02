/**
 * A Task implementing comparable that is prioritized based on shortest duration
 */
public class TaskByDuration extends Task {
    public TaskByDuration(int ID, int start, int deadline, int duration) {
        super(ID, start, deadline, duration);
    }
    @Override
    public int compareTo(Task o) {
        return Integer.compare(duration, o.duration);
    }
}
