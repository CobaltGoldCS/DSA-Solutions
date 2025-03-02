import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Assignment5 {
    public static void main(String[] args) throws FileNotFoundException {
        simpleQueueTest();
        scheduleTasks("taskset1.txt");
        scheduleTasks("taskset2.txt");
        scheduleTasks("taskset3.txt");
        scheduleTasks("taskset4.txt");
        scheduleTasks("taskset5.txt");
    }

    public static void scheduleTasks(String taskFile) throws FileNotFoundException {
        Queue<Task> tasksByDeadline = new LinkedList<>();
        Queue<Task> tasksByStart = new LinkedList<>();
        Queue<Task> tasksByDuration = new LinkedList<>();

        readTasks(taskFile, tasksByDeadline, tasksByStart, tasksByDuration);

        schedule("Deadline Priority : "+ taskFile, tasksByDeadline);
        schedule("Startime Priority : " + taskFile, tasksByStart);
        schedule("Duration priority : " + taskFile, tasksByDuration);
    }

    public static void simpleQueueTest() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.enqueue(9);
        queue.enqueue(7);
        queue.enqueue(5);
        queue.enqueue(3);
        queue.enqueue(1);
        queue.enqueue(10);

        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }
    }

    /**
     * Reads the task data from a file, and creates the three different sets of tasks for each
     */
    public static void readTasks(String filename,
                                 Queue<Task> tasksByDeadline,
                                 Queue<Task> tasksByStart,
                                 Queue<Task> tasksByDuration) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File(filename));

        int id = 0;
        while (fileScanner.hasNextLine()) {
            String[] splitString = fileScanner.nextLine().split("\t");

            int earliestStart = Integer.parseInt(splitString[0]);
            int deadline = Integer.parseInt(splitString[1]);
            int duration = Integer.parseInt(splitString[2]);
            id++;

            tasksByDeadline.add(new TaskByDeadline(id, earliestStart, deadline, duration));
            tasksByStart.add(new TaskByStart(id, earliestStart, deadline, duration));
            tasksByDuration.add(new TaskByDuration(id, earliestStart, deadline, duration));
        }
    }

    /**
     * Given a set of tasks, schedules them and reports the scheduling results
     */
    public static void schedule(String label, Queue<Task> tasks) {
        System.out.println(label);

        int clock = 1;
        int lateTasks = 0;
        int totalLateTime = 0;

        PriorityQueue<Task> queuedTasks = new PriorityQueue<>();
        Queue<Task> readyTasks = new LinkedList<>();

        while (!queuedTasks.isEmpty() || !tasks.isEmpty()) {
            // Remove all tasks that should be started
            for (Task task : tasks) {
                if (task.start <= clock) {
                    readyTasks.add(task);
                }
            }
            tasks.removeAll(readyTasks);
            for (Task task : readyTasks) {
                queuedTasks.enqueue(task);
            }
            readyTasks.clear();

            // No work can be done in this clock cycle
            if (queuedTasks.isEmpty()) {
                System.out.printf("Time %2d: ---\n", clock);
                clock++;
                continue;
            }

            Task task = queuedTasks.dequeue();

            String specialInformation = "";


            task.duration -= 1;
            if (task.duration == 0) {
                specialInformation += "**";

                // Late Work
                if (clock > task.deadline) {
                    int lateTime = clock - task.deadline;
                    specialInformation += " Late " + lateTime;

                    totalLateTime += lateTime;
                    lateTasks++;
                }
            } else {
                // Work still needs to be done on this task
                queuedTasks.enqueue(task);
            }


            System.out.printf("Time %2d: %s %s\n", clock, task, specialInformation);
            clock++;
        }

        System.out.printf("Tasks late %d total late %d\n\n", lateTasks, totalLateTime);
    }
}
