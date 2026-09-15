import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Main application class for the Nudge task management system
 */
public class ReminderApp {
    private TaskManager taskManager;
    private RewardSystem rewardSystem;
    private Scanner scanner;
    private boolean running;
    
    public ReminderApp() {
        this.taskManager = new TaskManager();
        this.rewardSystem = new RewardSystem();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    /**
     * Main application loop
     */
    public void run() {
        displayWelcomeMessage();
        
        while (running) {
            // Check for reminders at the start of each loop
            taskManager.sendReminders();
            
            displayMainMenu();
            int choice = getMenuChoice();
            handleMenuChoice(choice);
        }
        
        displayGoodbyeMessage();
        scanner.close();
    }
    
    /**
     * Displays the welcome message
     */
    private void displayWelcomeMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🌟 WELCOME TO NUDGE 🌟");
        System.out.println("Your neurodivergent-friendly task companion!");
        System.out.println("=".repeat(60));
        System.out.println("💛 Remember: Progress over perfection!");
        System.out.println("✨ Every small step is a victory!");
        System.out.println("🤗 Be kind to yourself today!");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Displays the main menu
     */
    private void displayMainMenu() {
        System.out.println("\n📋 MAIN MENU:");
        System.out.println("1. ➕ Add New Task");
        System.out.println("2. 📋 View Active Tasks");
        System.out.println("3. ✅ Mark Task Complete");
        System.out.println("4. ❌ Mark Task Missed");
        System.out.println("5. 🗑️  Remove Task");
        System.out.println("6. 📊 View Completed Tasks");
        System.out.println("7. 🏆 View Stats & Achievements");
        System.out.println("8. 💫 Get Affirmation");
        System.out.println("9. 🚪 Exit");
        System.out.print("\nChoose an option (1-9): ");
    }
    
    /**
     * Gets and validates menu choice
     */
    private int getMenuChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= 9) {
                return choice;
            } else {
                System.out.println("🌸 Please choose a number between 1 and 9!");
                return getMenuChoice();
            }
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid number!");
            return getMenuChoice();
        }
    }
    
    /**
     * Handles the selected menu choice
     */
    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                addNewTask();
                break;
            case 2:
                viewActiveTasks();
                break;
            case 3:
                markTaskComplete();
                break;
            case 4:
                markTaskMissed();
                break;
            case 5:
                removeTask();
                break;
            case 6:
                viewCompletedTasks();
                break;
            case 7:
                viewStats();
                break;
            case 8:
                showAffirmation();
                break;
            case 9:
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Adds a new task
     */
    private void addNewTask() {
        System.out.println("\n✨ CREATING A NEW TASK");
        System.out.println("-".repeat(30));
        
        // Get task name
        System.out.print("📝 Task name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("🌸 Task name cannot be empty. Try again!");
            return;
        }
        
        // Get deadline
        LocalDateTime deadline = getTaskDeadline();
        if (deadline == null) return;
        
        // Get category
        TaskCategory category = getTaskCategory();
        if (category == null) return;
        
        // Get reminder type
        ReminderType reminderType = getReminderType();
        if (reminderType == null) return;
        
        // Get point value
        int pointValue = getPointValue();
        if (pointValue == -1) return;
        
        // Create and add task
        Task task = new Task(name, deadline, reminderType, category, pointValue);
        taskManager.addTask(task);
        
        System.out.println("\n🎉 Task created successfully!");
        System.out.println("📋 " + task.toString());
        System.out.println("💪 You've got this!");
    }
    
    /**
     * Gets task deadline from user input
     */
    private LocalDateTime getTaskDeadline() {
        System.out.print("⏰ Deadline (YYYY-MM-DD HH:MM): ");
        String deadlineStr = scanner.nextLine().trim();
        
        try {
            return DateUtils.parseDateTime(deadlineStr);
        } catch (DateTimeParseException e) {
            System.out.println("🌸 Invalid date format. Please use YYYY-MM-DD HH:MM (e.g., 2025-07-15 14:30)");
            return null;
        }
    }
    
    /**
     * Gets task category from user selection
     */
    private TaskCategory getTaskCategory() {
        System.out.println("\n📂 Choose a category:");
        TaskCategory[] categories = TaskCategory.values();
        
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i].getDisplayName());
        }
        
        System.out.print("Select category (1-" + categories.length + "): ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= categories.length) {
                return categories[choice - 1];
            } else {
                System.out.println("🌸 Please choose a number between 1 and " + categories.length);
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid number!");
            return null;
        }
    }
    
    /**
     * Gets reminder type from user selection
     */
    private ReminderType getReminderType() {
        System.out.println("\n🔔 Choose reminder style:");
        ReminderType[] types = ReminderType.values();
        
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i].getDisplayName());
        }
        
        System.out.print("Select reminder type (1-" + types.length + "): ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= types.length) {
                return types[choice - 1];
            } else {
                System.out.println("🌸 Please choose a number between 1 and " + types.length);
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid number!");
            return null;
        }
    }
    
    /**
     * Gets point value for the task
     */
    private int getPointValue() {
        System.out.print("💎 Point value (1-100): ");
        
        try {
            int points = Integer.parseInt(scanner.nextLine().trim());
            if (points >= 1 && points <= 100) {
                return points;
            } else {
                System.out.println("🌸 Please enter a value between 1 and 100");
                return -1;
            }
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid number!");
            return -1;
        }
    }
    
    /**
     * Views active tasks
     */
    private void viewActiveTasks() {
        taskManager.displayActiveTasks();
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Marks a task as complete
     */
    private void markTaskComplete() {
        if (taskManager.getActiveTasks().isEmpty()) {
            System.out.println("🎉 No active tasks to complete! You're all caught up!");
            return;
        }
        
        System.out.println("\n✅ MARK TASK COMPLETE");
        taskManager.displayActiveTasks();
        
        System.out.print("\nEnter task ID to complete: ");
        
        try {
            int taskId = Integer.parseInt(scanner.nextLine().trim());
            Task task = taskManager.findTaskById(taskId);
            
            if (task == null) {
                System.out.println("🌸 Task not found. Please check the ID!");
                return;
            }
            
            if (task.isCompleted()) {
                System.out.println("🌸 This task is already completed!");
                return;
            }
            
            task.markCompleted();
            String rewardMessage = rewardSystem.awardPoints(task.getPointValue());
            
            System.out.println("\n" + rewardMessage);
            System.out.println("📋 Task completed: " + task.getName());
            
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid task ID!");
        }
    }
    
    /**
     * Marks a task as missed
     */
    private void markTaskMissed() {
        if (taskManager.getActiveTasks().isEmpty()) {
            System.out.println("🎉 No active tasks! Nothing to worry about!");
            return;
        }
        
        System.out.println("\n💙 MISSED TASK CHECK-IN");
        taskManager.displayActiveTasks();
        
        System.out.print("\nEnter task ID that was missed: ");
        
        try {
            int taskId = Integer.parseInt(scanner.nextLine().trim());
            Task task = taskManager.findTaskById(taskId);
            
            if (task == null) {
                System.out.println("🌸 Task not found. Please check the ID!");
                return;
            }
            
            if (task.isCompleted()) {
                System.out.println("🌸 This task is already completed!");
                return;
            }
            
            String checkInMessage = rewardSystem.missedTask();
            System.out.println("\n" + checkInMessage);
            
            System.out.print("💭 Want to share what's on your mind? (or press Enter to skip): ");
            String reflection = scanner.nextLine().trim();
            
            if (!reflection.isEmpty()) {
                System.out.println("💛 Thank you for sharing. Your feelings are valid.");
            }
            
            System.out.println("🌈 Tomorrow is a new day. You've got this!");
            
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid task ID!");
        }
    }
    
    /**
     * Removes a task
     */
    private void removeTask() {
        if (taskManager.getAllTasks().isEmpty()) {
            System.out.println("📋 No tasks to remove!");
            return;
        }
        
        System.out.println("\n🗑️ REMOVE TASK");
        taskManager.displayActiveTasks();
        
        System.out.print("\nEnter task ID to remove: ");
        
        try {
            int taskId = Integer.parseInt(scanner.nextLine().trim());
            
            if (taskManager.removeTask(taskId)) {
                System.out.println("✅ Task removed successfully!");
            } else {
                System.out.println("🌸 Task not found. Please check the ID!");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid task ID!");
        }
    }
    
    /**
     * Views completed tasks
     */
    private void viewCompletedTasks() {
        taskManager.displayCompletedTasks();
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Views stats and achievements
     */
    private void viewStats() {
        rewardSystem.displayStats();
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Shows a random affirmation
     */
    private void showAffirmation() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(rewardSystem.getRandomAffirmation());
        System.out.println("=".repeat(50));
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Displays goodbye message
     */
    private void displayGoodbyeMessage() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🌟 GOODBYE FROM NUDGE! 🌟");
        System.out.println("=".repeat(50));
        System.out.println("💛 You did great today!");
        System.out.println("✨ Remember to be kind to yourself!");
        System.out.println("🌈 See you next time!");
        System.out.println("=".repeat(50));
    }
    
    /**
     * Main method - entry point of the application
     */
    public static void main(String[] args) {
        ReminderApp app = new ReminderApp();
        app.run();
    }
}
