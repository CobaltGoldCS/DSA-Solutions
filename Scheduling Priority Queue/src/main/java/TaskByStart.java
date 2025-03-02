/**
 * A Task implementing comparable that is prioritized based on earliest start time
 */
public class TaskByStart extends Task {

    public TaskByStart(int ID, int start, int deadline, int duration) {
        super(ID, start, deadline, duration);
    }
    @Override
    public int compareTo(Task o) {
        int difference = Integer.compare(this.start, o.start);
        return difference != 0 ? difference : Integer.compare(this.deadline, o.deadline);
    }
}
