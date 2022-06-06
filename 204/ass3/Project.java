import java.util.*;

public class Project {
    private final String name;
    private final List<Task> tasks;
    private int[] schedule;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {
        HashMap<Integer,List<Integer>> adjList = new HashMap<>(); // dependencies adj list
        for (Task task : tasks) {
            adjList.put(task.getTaskID(),task.getDependencies());
        }

        Graph gr = new Graph(tasks); 
        List<Integer> order = gr.DFS(); // topological order
        int[] startTime = new int[tasks.size()]; 

        for (int i : order) {
            Task currentTask = tasks.get(i);
            List<Integer> adjs = currentTask.getDependencies();
            if (adjs.size() == 0) {
                startTime[i] = 0;
            }
            else{ // get start time as end of maximum of dependencies
                int max = startTime[adjs.get(0)] +tasks.get(adjs.get(0)).getDuration(); 
                for (int j = 0; j < adjs.size(); j++) {
                    if (startTime[adjs.get(j)] + tasks.get(adjs.get(j)).getDuration() > max) {
                        max =  startTime[adjs.get(j)] + tasks.get(adjs.get(j)).getDuration();
                    }
                }

                startTime[i] = max;
            }
        }

        schedule = startTime;
        return startTime;
    }


    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        int projectDuration = 0;
        getEarliestSchedule();
        projectDuration= tasks.get(schedule.length - 1).getDuration() + schedule[schedule.length - 1];
        return projectDuration;
    }

    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }

    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.println(String.format("Project name: %s", name));
        printlnDash(limit, symbol);

        // Print header
        System.out.println(String.format("%-10s%-45s%-7s%-5s", "Task ID", "Description", "Start", "End"));
        printlnDash(limit, symbol);
        for (int i = 0; i < schedule.length; i++) {
            Task t = tasks.get(i);
            System.out.println(String.format("%-10d%-45s%-7d%-5d", i, t.getDescription(), schedule[i], schedule[i] + t.getDuration()));
        }
        printlnDash(limit, symbol);
        System.out.println(String.format("Project will be completed in %d days.", tasks.get(schedule.length - 1).getDuration() + schedule[schedule.length - 1]));
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }

}
