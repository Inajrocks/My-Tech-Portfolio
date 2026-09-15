import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages task storage and operations
 */
public class TaskManager {
    private List<Task> tasks;
    
    public TaskManager() {
        this.tasks = new ArrayList<>();
    }
    
    /**
     * Adds a new task to the list
     */
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    /**
     * Finds a task by ID
     */
    public Task findTaskById(int id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Gets all active (incomplete) tasks
     */
    public List<Task> getActiveTasks() {
        return tasks.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }
    
    /**
     * Gets all completed tasks
     */
    public List<Task> getCompletedTasks() {
        return tasks.stream()
                .filter(Task::isCompleted)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets overdue tasks
     */
    public List<Task> getOverdueTasks() {
        return tasks.stream()
                .filter(Task::isOverdue)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets tasks due soon (within next hour)
     */
    public List<Task> getTasksDueSoon() {
        return tasks.stream()
                .filter(Task::isDueSoon)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets tasks by category
     */
    public List<Task> getTasksByCategory(TaskCategory category) {
        return tasks.stream()
                .filter(task -> task.getCategory() == category)
                .collect(Collectors.toList());
    }
    
    /**
     * Removes a task from the list
     */
    public boolean removeTask(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }
    
    /**
     * Displays all active tasks
     */
    public void displayActiveTasks() {
        List<Task> activeTasks = getActiveTasks();
        
        if (activeTasks.isEmpty()) {
            System.out.println("🎉 Wow! No active tasks. You're absolutely crushing it!");
            return;
        }
        
        System.out.println("\n📋 ACTIVE TASKS:");
        System.out.println("-".repeat(70));
        
        for (Task task : activeTasks) {
            System.out.println(task.toString());
        }
        
        // Show overdue tasks prominently
        List<Task> overdue = getOverdueTasks();
        if (!overdue.isEmpty()) {
            System.out.println("\n⚠️  NEEDS ATTENTION (Overdue):");
            System.out.println("-".repeat(40));
            for (Task task : overdue) {
                System.out.println("   " + task.toString());
            }
        }
        
        // Show tasks due soon
        List<Task> dueSoon = getTasksDueSoon();
        if (!dueSoon.isEmpty()) {
            System.out.println("\n⏰ COMING UP SOON:");
            System.out.println("-".repeat(30));
            for (Task task : dueSoon) {
                System.out.println("   " + task.toString());
            }
        }
    }
    
    /**
     * Displays completed tasks
     */
    public void displayCompletedTasks() {
        List<Task> completedTasks = getCompletedTasks();
        
        if (completedTasks.isEmpty()) {
            System.out.println("No completed tasks yet. But you've got this! 💪");
            return;
        }
        
        System.out.println("\n✅ COMPLETED TASKS:");
        System.out.println("-".repeat(50));
        
        for (Task task : completedTasks) {
            System.out.println(task.toString());
        }
    }
    
    /**
     * Sends reminders for tasks due soon
     */
    public void sendReminders() {
        List<Task> dueSoon = getTasksDueSoon();
        List<Task> overdue = getOverdueTasks();
        
        if (dueSoon.isEmpty() && overdue.isEmpty()) {
            return;
        }
        
        System.out.println("\n🔔 REMINDERS:");
        System.out.println("=".repeat(40));
        
        for (Task task : dueSoon) {
            System.out.println(task.getReminderMessage());
        }
        
        for (Task task : overdue) {
            System.out.println("⚠️ OVERDUE: " + task.getReminderMessage());
        }
        
        System.out.println("=".repeat(40));
    }
    
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
}
