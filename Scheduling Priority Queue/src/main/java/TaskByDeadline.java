/**
 * A Task implementing comparable that is prioritized based on earliest deadline
 */
public class TaskByDeadline extends Task {
    public TaskByDeadline(int ID, int start, int deadline, int duration) {
        super(ID, start, deadline, duration);
    }
    @Override
    public int compareTo(Task o) {
        return Integer.compare(this.deadline, o.deadline);
    }
}
