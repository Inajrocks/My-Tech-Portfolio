import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Enhanced main application class for the Nudge task management system
 * with all advanced features for neurodivergent users
 */
public class EnhancedReminderApp {
    private TaskManager taskManager;
    private RewardSystem rewardSystem;
    private Scanner scanner;
    private boolean running;
    
    // Enhanced features
    private EnergyTracker energyTracker;
    private PomodoroTimer pomodoroTimer;
    private RoutineManager routineManager;
    private HabitTracker habitTracker;
    private TaskBreakdown taskBreakdown;
    private OverwhelmManager overwhelmManager;
    private SocialSupport socialSupport;
    
    public EnhancedReminderApp() {
        this.taskManager = new TaskManager();
        this.rewardSystem = new RewardSystem();
        this.scanner = new Scanner(System.in);
        this.running = true;
        
        // Initialize enhanced features
        this.energyTracker = new EnergyTracker();
        this.pomodoroTimer = new PomodoroTimer();
        this.routineManager = new RoutineManager();
        this.habitTracker = new HabitTracker();
        this.taskBreakdown = new TaskBreakdown();
        this.overwhelmManager = new OverwhelmManager();
        this.socialSupport = new SocialSupport();
    }
    
    /**
     * Main application loop
     */
    public void run() {
        displayWelcomeMessage();
        checkDailyCheckIn();
        
        while (running) {
            // Check for reminders and overwhelm
            displayContextualInfo();
            
            displayMainMenu();
            int choice = getMenuChoice();
            handleMenuChoice(choice);
        }
        
        displayGoodbyeMessage();
        scanner.close();
    }
    
    private void checkDailyCheckIn() {
        System.out.println("\n🌈 DAILY NEURODIVERGENT-FRIENDLY CHECK-IN");
        System.out.println("=".repeat(55));
        System.out.println("This helps personalize your experience based on how");
        System.out.println("your unique brain is working today. No judgment, just support!");
        System.out.println("=".repeat(55));
        
        // Enhanced energy check-in with more context
        System.out.println("\n⚡ ENERGY CHECK: How's your battery today?");
        System.out.println("Think about: physical energy, mental clarity, motivation");
        System.out.println("1. 😴 Very Low - Spoon theory: maybe 1-2 spoons left");
        System.out.println("2. 😌 Low - Running on fumes, need gentle approach");
        System.out.println("3. 🙂 Moderate - Steady baseline, can handle routine");
        System.out.println("4. 😊 Good - Feeling capable, ready for challenges");
        System.out.println("5. ⚡ High - Hyperfocus energy, but watch for burnout");
        
        int energyChoice = getMenuChoice(1, 5);
        EnergyTracker.EnergyLevel energy = EnergyTracker.EnergyLevel.values()[energyChoice - 1];
        
        // Comprehensive neurodivergent mood check-in
        System.out.println("\n💭 EMOTIONAL/NEUROLOGICAL STATE:");
        System.out.println("What best describes your internal experience right now?");
        System.out.println("=== CHALLENGING STATES ===");
        System.out.println("1. 😵 Overwhelmed - Too much input, need simplification");
        System.out.println("2. 😰 Anxious - Seeking safety and predictability");
        System.out.println("3. 🌪️ Sensory overload - Environment feels too intense");
        System.out.println("4. 💔 RSD activated - Rejection sensitivity heightened");
        System.out.println("5. 🪫 Post-hyperfocus crash - Energy completely depleted");
        System.out.println("6. 🧠 Executive dysfunction - Can't initiate or switch tasks");
        System.out.println("7. 🎭 Masking fatigue - Exhausted from social performance");
        
        System.out.println("\n=== NEUTRAL STATES ===");
        System.out.println("8. 😐 Neutral - Regular baseline day");
        System.out.println("9. 🤔 Processing - Need time to think things through");
        System.out.println("10. 🐌 Cautious - Taking things slow and careful");
        
        System.out.println("\n=== POSITIVE STATES ===");
        System.out.println("11. 😌 Content - Comfortable and steady");
        System.out.println("12. ✨ Special interest engaged - Deep passion mode");
        System.out.println("13. 🎯 Hyperfocus flow - In the productive zone");
        System.out.println("14. 🌈 Stimming happy - Self-regulation working well");
        System.out.println("15. 🔥 Motivated - Ready to channel energy effectively");
        
        int moodChoice = getMenuChoice(1, 15);
        EnergyTracker.MoodState mood = EnergyTracker.MoodState.values()[moodChoice - 1];
        
        // Contextual follow-up questions based on mood state
        String additionalContext = "";
        if (mood.isChallengingState()) {
            System.out.println("\n💙 I see you're having a tough moment. That's completely valid.");
            System.out.print("🤗 What would help you feel more supported today? ");
            additionalContext = scanner.nextLine().trim();
        } else if (mood.isPositiveState()) {
            System.out.println("\n✨ Wonderful! Let's make the most of this good energy.");
            System.out.print("🎯 Is there anything specific you'd like to focus on today? ");
            additionalContext = scanner.nextLine().trim();
        } else {
            System.out.print("💭 Any thoughts about what might help today? (or press Enter to skip): ");
            additionalContext = scanner.nextLine().trim();
        }
        
        // Optional sensory check for challenging states
        List<String> sensoryTriggers = new ArrayList<>();
        if (mood == EnergyTracker.MoodState.SENSORY_OVERLOAD || 
            mood == EnergyTracker.MoodState.OVERWHELMED ||
            mood == EnergyTracker.MoodState.ANXIOUS) {
            
            System.out.println("\n🌿 SENSORY CHECK (optional - press Enter to skip any):");
            System.out.print("Sounds bothering you? (traffic, voices, etc.): ");
            String sound = scanner.nextLine().trim();
            if (!sound.isEmpty()) sensoryTriggers.add("Sound: " + sound);
            
            System.out.print("Visual overwhelm? (bright lights, clutter, etc.): ");
            String visual = scanner.nextLine().trim();
            if (!visual.isEmpty()) sensoryTriggers.add("Visual: " + visual);
            
            System.out.print("Touch/texture issues? (clothing, temperature, etc.): ");
            String touch = scanner.nextLine().trim();
            if (!touch.isEmpty()) sensoryTriggers.add("Touch: " + touch);
        }
        
        // Log the comprehensive check-in
        energyTracker.logDailyCheckin(energy, mood, additionalContext);
        
        // Set overwhelm level based on mood with more nuanced mapping
        OverwhelmManager.OverwhelmLevel overwhelmLevel;
        switch (mood) {
            case OVERWHELMED:
            case SENSORY_OVERLOAD:
                overwhelmLevel = OverwhelmManager.OverwhelmLevel.HIGH;
                break;
            case ANXIOUS:
            case REJECTION_SENSITIVE:
            case MASKING_FATIGUE:
                overwhelmLevel = OverwhelmManager.OverwhelmLevel.MODERATE;
                break;
            case HYPERFOCUS_CRASH:
            case EXECUTIVE_DYSFUNCTION:
                overwhelmLevel = OverwhelmManager.OverwhelmLevel.SLIGHT;
                break;
            default:
                overwhelmLevel = OverwhelmManager.OverwhelmLevel.CALM;
                break;
        }
        
        // Convert sensory triggers to overwhelm triggers
        List<OverwhelmManager.OverwhelmTrigger> overwhelmTriggers = new ArrayList<>();
        if (!sensoryTriggers.isEmpty()) {
            overwhelmTriggers.add(OverwhelmManager.OverwhelmTrigger.SENSORY_OVERLOAD);
        }
        
        overwhelmManager.recordCheckin(overwhelmLevel, overwhelmTriggers, additionalContext);
        
        // Provide immediate mood-specific affirmation
        System.out.println("\n💝 " + rewardSystem.getMoodSpecificAffirmation(mood));
        
        // Personalized recommendations
        System.out.println("\n✨ PERSONALIZED RECOMMENDATIONS FOR TODAY:");
        List<String> recommendations = energyTracker.getTaskRecommendations();
        int maxRecs = mood.isChallengingState() ? 3 : 5; // Fewer suggestions when struggling
        for (int i = 0; i < Math.min(maxRecs, recommendations.size()); i++) {
            System.out.println("   " + recommendations.get(i));
        }
        
        // Special guidance for challenging states
        if (mood.isChallengingState()) {
            System.out.println("\n🌱 Remember: Managing a neurodivergent brain takes extra energy.");
            System.out.println("   Your struggles are valid, and small steps count as victories.");
        } else if (mood.isPositiveState()) {
            System.out.println("\n🚀 You're in a great space! Remember to:");
            System.out.println("   Set gentle boundaries to maintain this energy sustainably.");
        }
        
        System.out.println("\n" + "=".repeat(55));
    }
    
    private void displayContextualInfo() {
        // Send reminders
        taskManager.sendReminders();
        
        // Check for routine reminders
        List<String> routineReminders = routineManager.getRoutineReminders();
        if (!routineReminders.isEmpty()) {
            System.out.println("\n⏰ ROUTINE REMINDERS:");
            for (String reminder : routineReminders) {
                System.out.println("   " + reminder);
            }
        }
        
        // Check for habit reminders
        List<String> habitReminders = habitTracker.getTodaysHabitReminders();
        if (!habitReminders.isEmpty() && habitReminders.size() <= 2) { // Don't overwhelm
            System.out.println("\n🌱 HABIT NUDGES:");
            for (String reminder : habitReminders) {
                System.out.println("   " + reminder);
            }
        }
        
        // Check for social check-ins
        List<String> socialReminders = socialSupport.getDueCheckIns();
        if (!socialReminders.isEmpty()) {
            System.out.println("\n👥 SOCIAL:");
            for (String reminder : socialReminders) {
                System.out.println("   " + reminder);
            }
        }
        
        // Show active Pomodoro session
        if (pomodoroTimer.getState() == PomodoroTimer.TimerState.RUNNING) {
            System.out.println("\n🍅 ACTIVE POMODORO: " + pomodoroTimer.getTimeRemaining());
        }
        
        // Show active body doubling session
        if (socialSupport.getCurrentSession() != null && socialSupport.getCurrentSession().isActive()) {
            System.out.println("👥 BODY DOUBLING: " + socialSupport.getCurrentSession().getTimeRemaining());
        }
    }
    
    /**
     * Displays the welcome message
     */
    private void displayWelcomeMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🌟 WELCOME TO NUDGE - ENHANCED EDITION 🌟");
        System.out.println("Your comprehensive neurodivergent-friendly task companion!");
        System.out.println("=".repeat(60));
        System.out.println("💛 Remember: Progress over perfection!");
        System.out.println("✨ Every small step is a victory!");
        System.out.println("🤗 Be kind to yourself today!");
        System.out.println("🧠 Your brain works differently, and that's your superpower!");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Displays the enhanced main menu
     */
    private void displayMainMenu() {
        System.out.println("\n📋 MAIN MENU:");
        System.out.println("=== TASKS ===");
        System.out.println("1. ➕ Add New Task");
        System.out.println("2. 📋 View Active Tasks");
        System.out.println("3. ✅ Mark Task Complete");
        System.out.println("4. ❌ Mark Task Missed");
        System.out.println("5. 🗑️  Remove Task");
        System.out.println("6. 📊 View Completed Tasks");
        System.out.println("7. 🔄 Add Recurring Task");
        System.out.println("8. 📝 Break Down Task");
        
        System.out.println("\n=== FOCUS & TIME ===");
        System.out.println("9. 🍅 Pomodoro Timer");
        System.out.println("10. 👥 Body Doubling Session");
        System.out.println("11. 📅 Routine Management");
        System.out.println("12. 🌱 Habit Tracker");
        
        System.out.println("\n=== WELLNESS ===");
        System.out.println("13. ⚡ Energy & Mood Dashboard");
        System.out.println("14. 😰 Overwhelm Check-in");
        System.out.println("15. 🌿 Sensory Break");
        
        System.out.println("\n=== SOCIAL & REWARDS ===");
        System.out.println("16. 🤝 Social Support");
        System.out.println("17. 🏆 View Stats & Achievements");
        System.out.println("18. 💫 Get Affirmation");
        
        System.out.println("\n=== OTHER ===");
        System.out.println("19. ⚙️ Settings");
        System.out.println("20. 🚪 Exit");
        System.out.print("\nChoose an option (1-20): ");
    }
    
    /**
     * Gets and validates menu choice
     */
    private int getMenuChoice() {
        return getMenuChoice(1, 20);
    }
    
    private int getMenuChoice(int min, int max) {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= min && choice <= max) {
                return choice;
            } else {
                System.out.println("🌸 Please choose a number between " + min + " and " + max + "!");
                return getMenuChoice(min, max);
            }
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid number!");
            return getMenuChoice(min, max);
        }
    }
    
    /**
     * Handles the selected menu choice
     */
    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1: addNewTask(); break;
            case 2: viewActiveTasks(); break;
            case 3: markTaskComplete(); break;
            case 4: markTaskMissed(); break;
            case 5: removeTask(); break;
            case 6: viewCompletedTasks(); break;
            case 7: addRecurringTask(); break;
            case 8: breakDownTask(); break;
            case 9: managePomodoroTimer(); break;
            case 10: manageBodyDoubling(); break;
            case 11: manageRoutines(); break;
            case 12: manageHabits(); break;
            case 13: viewEnergyDashboard(); break;
            case 14: overwhelmCheckIn(); break;
            case 15: takeSensoryBreak(); break;
            case 16: manageSocialSupport(); break;
            case 17: viewStats(); break;
            case 18: showAffirmation(); break;
            case 19: manageSettings(); break;
            case 20: running = false; break;
            default: System.out.println("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Enhanced task creation with more options
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
        
        // Get estimated duration
        System.out.print("⏰ Estimated duration (minutes, or press Enter for 30): ");
        String durationStr = scanner.nextLine().trim();
        int duration = 30;
        if (!durationStr.isEmpty()) {
            try {
                duration = Integer.parseInt(durationStr);
            } catch (NumberFormatException e) {
                System.out.println("🌸 Using default duration of 30 minutes");
            }
        }
        
        // Get context notes
        System.out.print("📋 Context/preparation notes (optional): ");
        String contextNotes = scanner.nextLine().trim();
        
        // Create and add task
        Task task = new Task(name, deadline, reminderType, category, pointValue);
        task.setEstimatedDurationMinutes(duration);
        if (!contextNotes.isEmpty()) {
            task.setContextNotes(contextNotes);
        }
        
        taskManager.addTask(task);
        
        System.out.println("\n🎉 Task created successfully!");
        System.out.println("📋 " + task.toString());
        
        // Ask if they want to break it down
        System.out.print("🔍 Would you like to break this task into smaller steps? (y/n): ");
        String breakdownChoice = scanner.nextLine().trim().toLowerCase();
        if (breakdownChoice.equals("y") || breakdownChoice.equals("yes")) {
            TaskBreakdown.TaskPlan plan = taskBreakdown.suggestBreakdown(task);
            plan.displayPlan();
            System.out.println("💡 Task breakdown suggestions created! You can view and modify them later.");
        }
        
        System.out.println("💪 You've got this!");
    }
    
    private void addRecurringTask() {
        System.out.println("\n🔄 CREATING A RECURRING TASK");
        System.out.println("-".repeat(30));
        
        // Get basic task info
        System.out.print("📝 Task name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("🌸 Task name cannot be empty. Try again!");
            return;
        }
        
        // Get first occurrence
        LocalDateTime firstDue = getTaskDeadline();
        if (firstDue == null) return;
        
        TaskCategory category = getTaskCategory();
        if (category == null) return;
        
        ReminderType reminderType = getReminderType();
        if (reminderType == null) return;
        
        int pointValue = getPointValue();
        if (pointValue == -1) return;
        
        // Get recurrence type
        System.out.println("\n🔄 How often should this repeat?");
        RecurringTask.RecurrenceType[] types = RecurringTask.RecurrenceType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i].getDisplayName());
        }
        
        int recurrenceChoice = getMenuChoice(1, types.length);
        RecurringTask.RecurrenceType recurrenceType = types[recurrenceChoice - 1];
        
        // Create recurring task
        RecurringTask recurringTask = new RecurringTask(name, firstDue, reminderType, category, pointValue, recurrenceType);
        
        // Handle custom days for custom recurrence
        if (recurrenceType == RecurringTask.RecurrenceType.CUSTOM) {
            System.out.println("📅 Select days for custom recurrence:");
            DayOfWeek[] days = DayOfWeek.values();
            for (int i = 0; i < days.length; i++) {
                System.out.print((i + 1) + ". " + days[i] + " - Include? (y/n): ");
                String include = scanner.nextLine().trim().toLowerCase();
                if (include.equals("y") || include.equals("yes")) {
                    recurringTask.addCustomDay(days[i]);
                }
            }
        }
        
        taskManager.addTask(recurringTask);
        
        System.out.println("\n🎉 Recurring task created successfully!");
        System.out.println("📋 " + recurringTask.toString());
        System.out.println("🔄 This task will automatically reschedule when completed!");
    }
    
    private void breakDownTask() {
        if (taskManager.getActiveTasks().isEmpty()) {
            System.out.println("📋 No active tasks to break down!");
            return;
        }
        
        System.out.println("\n📝 TASK BREAKDOWN");
        taskManager.displayActiveTasks();
        
        System.out.print("\nEnter task ID to break down: ");
        try {
            int taskId = Integer.parseInt(scanner.nextLine().trim());
            Task task = taskManager.findTaskById(taskId);
            
            if (task == null) {
                System.out.println("🌸 Task not found. Please check the ID!");
                return;
            }
            
            TaskBreakdown.TaskPlan existingPlan = taskBreakdown.findTaskPlan(task);
            if (existingPlan != null) {
                existingPlan.displayPlan();
                System.out.print("📋 Task already has a breakdown. View subtask management? (y/n): ");
                String viewChoice = scanner.nextLine().trim().toLowerCase();
                if (viewChoice.equals("y") || viewChoice.equals("yes")) {
                    manageTaskBreakdown(existingPlan);
                }
            } else {
                TaskBreakdown.TaskPlan plan = taskBreakdown.suggestBreakdown(task);
                plan.displayPlan();
                System.out.println("\n💡 AI-generated breakdown created! You can customize it further.");
                manageTaskBreakdown(plan);
            }
            
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid task ID!");
        }
    }
    
    private void manageTaskBreakdown(TaskBreakdown.TaskPlan plan) {
        boolean managing = true;
        
        while (managing) {
            System.out.println("\n📝 BREAKDOWN MANAGEMENT:");
            System.out.println("1. ✅ Complete subtask");
            System.out.println("2. ➕ Add subtask");
            System.out.println("3. 🗑️ Remove subtask");
            System.out.println("4. 📊 View plan");
            System.out.println("5. 🔙 Back to main menu");
            
            int choice = getMenuChoice(1, 5);
            
            switch (choice) {
                case 1:
                    List<TaskBreakdown.Subtask> incomplete = plan.getSubtasks().stream()
                        .filter(subtask -> !subtask.isCompleted())
                        .toList();
                    if (incomplete.isEmpty()) {
                        System.out.println("🎉 All subtasks completed!");
                        break;
                    }
                    
                    System.out.println("Select subtask to complete:");
                    for (int i = 0; i < incomplete.size(); i++) {
                        System.out.println((i + 1) + ". " + incomplete.get(i).getDescription());
                    }
                    
                    int subtaskChoice = getMenuChoice(1, incomplete.size());
                    int originalIndex = plan.getSubtasks().indexOf(incomplete.get(subtaskChoice - 1));
                    plan.completeSubtask(originalIndex);
                    
                    System.out.println("✅ Subtask completed! Progress: " + plan.getCompletionPercentage() + "%");
                    
                    if (plan.isFullyCompleted()) {
                        System.out.println("🎉 All subtasks completed! Consider marking the main task as complete too!");
                    }
                    break;
                    
                case 2:
                    System.out.print("📝 Subtask description: ");
                    String desc = scanner.nextLine().trim();
                    if (!desc.isEmpty()) {
                        System.out.print("⏰ Estimated minutes: ");
                        int minutes = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("📋 Context/materials needed: ");
                        String context = scanner.nextLine().trim();
                        
                        plan.addSubtask(desc, TaskBreakdown.Difficulty.MODERATE, minutes, context);
                        System.out.println("✅ Subtask added!");
                    }
                    break;
                    
                case 4:
                    plan.displayPlan();
                    break;
                    
                case 5:
                    managing = false;
                    break;
            }
        }
    }
    
    private void managePomodoroTimer() {
        System.out.println("\n🍅 POMODORO TIMER");
        pomodoroTimer.displayStats();
        
        if (pomodoroTimer.getState() == PomodoroTimer.TimerState.IDLE) {
            System.out.println("\n1. ▶️ Start work session");
            System.out.println("2. ⚙️ Customize settings");
            System.out.println("3. 🔙 Back to main menu");
            
            int choice = getMenuChoice(1, 3);
            
            switch (choice) {
                case 1:
                    // Select task for focus session
                    List<Task> activeTasks = taskManager.getActiveTasks();
                    Task selectedTask = null;
                    
                    if (!activeTasks.isEmpty()) {
                        System.out.println("🎯 Select a task to focus on (or 0 for general session):");
                        System.out.println("0. General focus session");
                        for (int i = 0; i < activeTasks.size(); i++) {
                            System.out.println((i + 1) + ". " + activeTasks.get(i).getName());
                        }
                        
                        int taskChoice = getMenuChoice(0, activeTasks.size());
                        if (taskChoice > 0) {
                            selectedTask = activeTasks.get(taskChoice - 1);
                        }
                    }
                    
                    pomodoroTimer.startSession(selectedTask);
                    break;
                    
                case 2:
                    System.out.print("Work duration (minutes): ");
                    int work = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Short break (minutes): ");
                    int shortBreak = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Long break (minutes): ");
                    int longBreak = Integer.parseInt(scanner.nextLine().trim());
                    System.out.print("Sessions until long break: ");
                    int sessionsForLong = Integer.parseInt(scanner.nextLine().trim());
                    
                    pomodoroTimer.customizeSettings(work, shortBreak, longBreak, sessionsForLong);
                    break;
            }
        } else {
            System.out.println("\n1. ⏸️ Pause session");
            System.out.println("2. ▶️ Resume session");
            System.out.println("3. ✅ Complete session");
            System.out.println("4. ⏭️ Skip session");
            System.out.println("5. 🔙 Back to main menu");
            
            int choice = getMenuChoice(1, 5);
            
            switch (choice) {
                case 1: pomodoroTimer.pauseSession(); break;
                case 2: pomodoroTimer.resumeSession(); break;
                case 3: pomodoroTimer.completeSession(); break;
                case 4: pomodoroTimer.skipSession(); break;
            }
        }
    }
    
    private void manageBodyDoubling() {
        System.out.println("\n👥 BODY DOUBLING");
        socialSupport.displaySocialDashboard();
        
        if (socialSupport.getCurrentSession() == null || !socialSupport.getCurrentSession().isActive()) {
            System.out.println("\n1. ▶️ Start body doubling session");
            System.out.println("2. 🔙 Back to main menu");
            
            int choice = getMenuChoice(1, 2);
            
            if (choice == 1) {
                System.out.print("⏰ Session duration (minutes): ");
                int duration = Integer.parseInt(scanner.nextLine().trim());
                
                System.out.print("📝 What will you work on? ");
                String description = scanner.nextLine().trim();
                
                // Optional task selection
                List<Task> activeTasks = taskManager.getActiveTasks();
                Task selectedTask = null;
                
                if (!activeTasks.isEmpty()) {
                    System.out.println("🎯 Link to a specific task? (or 0 for general session):");
                    System.out.println("0. General session");
                    for (int i = 0; i < activeTasks.size(); i++) {
                        System.out.println((i + 1) + ". " + activeTasks.get(i).getName());
                    }
                    
                    int taskChoice = getMenuChoice(0, activeTasks.size());
                    if (taskChoice > 0) {
                        selectedTask = activeTasks.get(taskChoice - 1);
                    }
                }
                
                socialSupport.startBodyDoublingSession(duration, description, selectedTask);
            }
        } else {
            System.out.println("\n1. ✅ End current session");
            System.out.println("2. 🔙 Back to main menu");
            
            int choice = getMenuChoice(1, 2);
            if (choice == 1) {
                socialSupport.endCurrentSession();
            }
        }
    }
    
    private void manageRoutines() {
        System.out.println("\n📅 ROUTINE MANAGEMENT");
        routineManager.displayAllRoutines();
        
        System.out.println("\n1. ▶️ Start routine");
        System.out.println("2. ✅ Complete routine step");
        System.out.println("3. ➕ Add new routine");
        System.out.println("4. 📊 View routine suggestions");
        System.out.println("5. 🔙 Back to main menu");
        
        int choice = getMenuChoice(1, 5);
        
        switch (choice) {
            case 1:
                // Start routine implementation
                List<RoutineManager.Routine> allRoutines = routineManager.getAllActiveRoutines();
                if (allRoutines.isEmpty()) {
                    System.out.println("📋 No routines available!");
                    break;
                }
                
                System.out.println("Select routine to start:");
                for (int i = 0; i < allRoutines.size(); i++) {
                    System.out.println((i + 1) + ". " + allRoutines.get(i).getName());
                }
                
                int routineChoice = getMenuChoice(1, allRoutines.size());
                RoutineManager.Routine selectedRoutine = allRoutines.get(routineChoice - 1);
                selectedRoutine.displayRoutine();
                
                System.out.println("💪 Routine displayed! Use 'Complete routine step' to track progress.");
                break;
                
            case 4:
                List<String> suggestions = routineManager.getRoutineSuggestions(energyTracker.getCurrentEnergy());
                System.out.println("\n💡 ROUTINE SUGGESTIONS:");
                for (String suggestion : suggestions) {
                    System.out.println("   " + suggestion);
                }
                break;
        }
    }
    
    private void manageHabits() {
        System.out.println("\n🌱 HABIT TRACKER");
        habitTracker.displayHabitDashboard();
        
        System.out.println("\n1. ✅ Mark habit complete");
        System.out.println("2. ❌ Mark habit skipped");
        System.out.println("3. ➕ Add new habit");
        System.out.println("4. 🗑️ Remove habit");
        System.out.println("5. 💡 Get habit suggestions");
        System.out.println("6. 🔙 Back to main menu");
        
        int choice = getMenuChoice(1, 6);
        
        switch (choice) {
            case 1:
                List<HabitTracker.Habit> allHabits = habitTracker.getAllHabits();
                if (allHabits.isEmpty()) {
                    System.out.println("📋 No habits to track!");
                    break;
                }
                
                System.out.println("Select habit to mark complete:");
                for (int i = 0; i < allHabits.size(); i++) {
                    System.out.println((i + 1) + ". " + allHabits.get(i).getName());
                }
                
                int habitChoice = getMenuChoice(1, allHabits.size());
                String habitName = allHabits.get(habitChoice - 1).getName();
                habitTracker.markHabitCompleted(habitName, java.time.LocalDate.now());
                
                System.out.println("✅ Habit completed! " + allHabits.get(habitChoice - 1).getEncouragementMessage());
                break;
                
            case 3:
                System.out.print("🌱 Habit name: ");
                String name = scanner.nextLine().trim();
                System.out.print("📝 Description: ");
                String description = scanner.nextLine().trim();
                System.out.print("🎯 Target times per week (1-7): ");
                int frequency = getMenuChoice(1, 7);
                
                TaskCategory category = getTaskCategory();
                if (category != null) {
                    habitTracker.addHabit(name, description, category, frequency);
                    System.out.println("🎉 Habit added! Start building that streak!");
                }
                break;
                
            case 5:
                List<String> habitSuggestions = habitTracker.getHabitSuggestions(energyTracker.getCurrentEnergy());
                System.out.println("\n💡 HABIT SUGGESTIONS:");
                for (String suggestion : habitSuggestions) {
                    System.out.println("   " + suggestion);
                }
                System.out.println("\n🌟 " + habitTracker.getMotivationalQuote());
                break;
        }
    }
    
    private void viewEnergyDashboard() {
        energyTracker.displayDashboard();
        
        System.out.println("\n1. 🔄 Update energy/mood");
        System.out.println("2. 📊 View executive function tips");
        System.out.println("3. 🔙 Back to main menu");
        
        int choice = getMenuChoice(1, 3);
        
        if (choice == 1) {
            checkDailyCheckIn(); // Re-run check-in
        } else if (choice == 2) {
            List<String> tips = taskBreakdown.getExecutiveFunctionTips(energyTracker.getCurrentEnergy());
            System.out.println("\n🧠 EXECUTIVE FUNCTION TIPS:");
            for (String tip : tips) {
                System.out.println("   " + tip);
            }
        }
    }
    
    private void overwhelmCheckIn() {
        overwhelmManager.displayOverwhelmDashboard();
        
        System.out.println("\n1. 🔄 Update overwhelm level");
        System.out.println("2. 🌿 View coping strategies");
        System.out.println("3. 📝 Get task adjustments");
        System.out.println("4. 🏠 Environment suggestions");
        System.out.println("5. 🔙 Back to main menu");
        
        int choice = getMenuChoice(1, 5);
        
        switch (choice) {
            case 1:
                System.out.println("😰 How overwhelmed are you feeling right now?");
                OverwhelmManager.OverwhelmLevel[] levels = OverwhelmManager.OverwhelmLevel.values();
                for (int i = 0; i < levels.length; i++) {
                    System.out.println((i + 1) + ". " + levels[i].getDescription());
                }
                
                int levelChoice = getMenuChoice(1, levels.length);
                OverwhelmManager.OverwhelmLevel newLevel = levels[levelChoice - 1];
                
                System.out.print("💭 What's contributing to this feeling? (optional): ");
                String notes = scanner.nextLine().trim();
                
                overwhelmManager.recordCheckin(newLevel, new ArrayList<>(), notes);
                overwhelmManager.displayOverwhelmDashboard();
                break;
                
            case 2:
                List<String> strategies = overwhelmManager.getCopingStrategies();
                System.out.println("\n🛠️ COPING STRATEGIES:");
                for (String strategy : strategies) {
                    System.out.println("   " + strategy);
                }
                break;
                
            case 3:
                List<String> adjustments = overwhelmManager.getTaskAdjustmentSuggestions();
                System.out.println("\n📝 TASK ADJUSTMENTS:");
                for (String adjustment : adjustments) {
                    System.out.println("   " + adjustment);
                }
                break;
                
            case 4:
                List<String> envSuggestions = overwhelmManager.getEnvironmentSuggestions();
                System.out.println("\n🏠 ENVIRONMENT SUGGESTIONS:");
                for (String suggestion : envSuggestions.subList(0, Math.min(5, envSuggestions.size()))) {
                    System.out.println("   " + suggestion);
                }
                break;
        }
    }
    
    private void takeSensoryBreak() {
        System.out.println("\n🌿 SENSORY BREAK TIME");
        System.out.println("Taking regular breaks is essential for neurodivergent brains!");
        
        List<String> suggestions = overwhelmManager.getSensoryBreakSuggestions();
        System.out.println("\n💡 Here are some suggestions:");
        for (String suggestion : suggestions) {
            System.out.println("   " + suggestion);
        }
        
        System.out.print("\n⏰ How long will you take for this break? (minutes): ");
        try {
            int minutes = Integer.parseInt(scanner.nextLine().trim());
            System.out.println("⏱️ Great! Take " + minutes + " minutes for yourself.");
            System.out.println("💛 " + overwhelmManager.getCompassionateMessage());
            
            overwhelmManager.recordSensoryBreak();
        } catch (NumberFormatException e) {
            System.out.println("🌸 No worries about the time - just take what you need!");
            overwhelmManager.recordSensoryBreak();
        }
        
        System.out.println("\nPress Enter when you're ready to continue...");
        scanner.nextLine();
    }
    
    private void manageSocialSupport() {
        socialSupport.displaySocialDashboard();
        
        System.out.println("\n1. 👥 Start body doubling session");
        System.out.println("2. 🤝 Add accountability partner");
        System.out.println("3. 📞 Record check-in");
        System.out.println("4. 🏆 Add achievement");
        System.out.println("5. 📢 Share achievement");
        System.out.println("6. 💡 View motivation tips");
        System.out.println("7. 🔙 Back to main menu");
        
        int choice = getMenuChoice(1, 7);
        
        switch (choice) {
            case 1:
                manageBodyDoubling();
                break;
                
            case 2:
                System.out.print("👤 Partner name: ");
                String name = scanner.nextLine().trim();
                System.out.print("🤝 Relationship (friend/family/colleague/etc.): ");
                String relationship = scanner.nextLine().trim();
                
                socialSupport.addAccountabilityPartner(name, relationship);
                break;
                
            case 4:
                System.out.print("🏆 Achievement title: ");
                String title = scanner.nextLine().trim();
                System.out.print("📝 Description: ");
                String description = scanner.nextLine().trim();
                
                socialSupport.addAchievement(title, description);
                break;
                
            case 6:
                List<String> tips = socialSupport.getSocialMotivationTips();
                System.out.println("\n💡 SOCIAL MOTIVATION TIPS:");
                for (String tip : tips) {
                    System.out.println("   " + tip);
                }
                break;
        }
    }
    
    private void manageSettings() {
        System.out.println("\n⚙️ SETTINGS");
        System.out.println("1. 🍅 Pomodoro timer settings");
        System.out.println("2. 📊 View all stats");
        System.out.println("3. 💡 View all tips");
        System.out.println("4. 🔙 Back to main menu");
        
        int choice = getMenuChoice(1, 4);
        
        switch (choice) {
            case 2:
                System.out.println("\n📊 COMPREHENSIVE STATS");
                System.out.println("=".repeat(50));
                rewardSystem.displayStats();
                pomodoroTimer.displayStats();
                System.out.println(routineManager.getRoutineStats());
                break;
                
            case 3:
                List<String> allTips = new ArrayList<>();
                allTips.addAll(taskBreakdown.getExecutiveFunctionTips(energyTracker.getCurrentEnergy()));
                allTips.addAll(habitTracker.getHabitSuggestions(energyTracker.getCurrentEnergy()));
                allTips.addAll(socialSupport.getSocialMotivationTips());
                
                System.out.println("\n💡 ALL TIPS & SUGGESTIONS:");
                for (int i = 0; i < Math.min(10, allTips.size()); i++) {
                    System.out.println("   " + allTips.get(i));
                }
                break;
        }
    }
    
    // Original methods adapted for enhanced features
    private void viewActiveTasks() {
        taskManager.displayActiveTasks();
        
        // Add energy-based suggestions
        if (energyTracker.getCurrentEnergy().getValue() <= 2) {
            System.out.println("\n💙 Energy is low today - consider:");
            System.out.println("   🌸 Pick just 1-2 easy tasks");
            System.out.println("   ⏰ Extend deadlines if possible");
            System.out.println("   🤗 Be extra gentle with yourself");
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
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
            
            // Handle recurring tasks
            if (task instanceof RecurringTask) {
                RecurringTask recurringTask = (RecurringTask) task;
                recurringTask.scheduleNext();
                System.out.println("🔄 Next occurrence scheduled: " + DateUtils.formatForDisplay(recurringTask.getNextDue()));
            }
            
            // Check if this completes a task breakdown
            TaskBreakdown.TaskPlan plan = taskBreakdown.findTaskPlan(task);
            if (plan != null && plan.isFullyCompleted()) {
                System.out.println("🎯 All subtasks were completed - excellent breakdown execution!");
                socialSupport.addAchievement("Task Master", "Completed a fully broken-down task: " + task.getName());
            }
            
            // Add to social achievements
            if (rewardSystem.getCurrentStreak() >= 7) {
                socialSupport.addAchievement("Week Warrior", "Maintained a 7-day completion streak!");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid task ID!");
        }
    }
    
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
            
            // Enhanced overwhelm check
            System.out.println("\n💭 Let's understand what happened:");
            System.out.println("1. 😴 Low energy/tired");
            System.out.println("2. 😰 Feeling overwhelmed");
            System.out.println("3. ⏰ Ran out of time");
            System.out.println("4. 🤔 Forgot about it");
            System.out.println("5. 😑 Just didn't want to do it");
            System.out.println("6. 🔄 Something else came up");
            
            int reasonChoice = getMenuChoice(1, 6);
            
            // Provide targeted support based on reason
            switch (reasonChoice) {
                case 1:
                    System.out.println("💙 Low energy is totally valid. Consider:");
                    System.out.println("   🌸 Breaking this task into smaller pieces");
                    System.out.println("   ⏰ Scheduling it for a higher-energy time");
                    break;
                case 2:
                    System.out.println("🤗 Overwhelm is real. Let's help:");
                    overwhelmManager.recordCheckin(OverwhelmManager.OverwhelmLevel.MODERATE, new ArrayList<>(), "Task overwhelm");
                    List<String> strategies = overwhelmManager.getCopingStrategies();
                    for (String strategy : strategies.subList(0, 3)) {
                        System.out.println("   " + strategy);
                    }
                    break;
                case 3:
                    System.out.println("⏰ Time management is tricky! Consider:");
                    System.out.println("   📅 Adding buffer time to estimates");
                    System.out.println("   🍅 Using Pomodoro technique");
                    break;
                case 4:
                    System.out.println("🧠 ADHD brains forget things! Consider:");
                    System.out.println("   ⏰ Setting multiple reminders");
                    System.out.println("   📱 Using phone alarms");
                    break;
            }
            
            System.out.print("\n💭 Want to share what's on your mind? (or press Enter to skip): ");
            String reflection = scanner.nextLine().trim();
            
            if (!reflection.isEmpty()) {
                System.out.println("💛 Thank you for sharing. Your feelings are valid.");
            }
            
            System.out.println("🌈 Tomorrow is a new day. You've got this!");
            
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid task ID!");
        }
    }
    
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
            Task task = taskManager.findTaskById(taskId);
            
            if (task == null) {
                System.out.println("🌸 Task not found. Please check the ID!");
                return;
            }
            
            // Remove associated task breakdown
            taskBreakdown.removeTaskPlan(task);
            
            if (taskManager.removeTask(taskId)) {
                System.out.println("✅ Task removed successfully!");
            } else {
                System.out.println("🌸 Task not found. Please check the ID!");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("🌸 Please enter a valid task ID!");
        }
    }
    
    private void viewCompletedTasks() {
        taskManager.displayCompletedTasks();
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void viewStats() {
        System.out.println("\n🏆 COMPREHENSIVE STATISTICS");
        System.out.println("=".repeat(60));
        
        rewardSystem.displayStats();
        energyTracker.displayDashboard();
        
        // Additional integrated stats
        System.out.println("\n📊 INTEGRATED INSIGHTS:");
        System.out.printf("🍅 Pomodoro sessions: %d (%.1f hours focused)\n", 
                         pomodoroTimer.getCompletedSessions(), 
                         pomodoroTimer.getTotalFocusTime() / 60.0);
        
        System.out.printf("👥 Body doubling sessions: %d\n", 
                         socialSupport.getAllSessions().size());
        
        System.out.printf("🌱 Active habits: %d\n", 
                         habitTracker.getAllHabits().size());
        
        System.out.printf("📅 Active routines: %d\n", 
                         routineManager.getAllActiveRoutines().size());
        
        System.out.println("\n🎉 " + rewardSystem.getRandomAffirmation());
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void showAffirmation() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("💫 AFFIRMATIONS & ENCOURAGEMENT");
        System.out.println("=".repeat(60));
        
        // Mood-specific affirmation first
        System.out.println("🎯 For your current state:");
        System.out.println("   " + rewardSystem.getMoodSpecificAffirmation(energyTracker.getCurrentMood()));
        
        // General evidence-based affirmation
        System.out.println("\n✨ Universal truth:");
        System.out.println("   " + rewardSystem.getRandomAffirmation());
        
        // Add contextual encouragement
        if (energyTracker.getCurrentEnergy().getValue() <= 2) {
            System.out.println("\n💙 GENTLE REMINDER FOR LOW ENERGY DAYS:");
            System.out.println("   🌱 Rest is productive - your brain needs recovery time");
            System.out.println("   🐌 Slow progress is still progress");
            System.out.println("   💤 Honoring your limits is self-respect, not laziness");
        } else if (energyTracker.getCurrentMood().isPositiveState()) {
            System.out.println("\n🌟 CELEBRATING YOUR POSITIVE ENERGY:");
            System.out.println("   ⚡ This feeling is proof of your resilience");
            System.out.println("   🎯 Channel this energy with gentle boundaries");
            System.out.println("   💫 You've earned these good moments");
        }
        
        if (rewardSystem.getCurrentStreak() > 0) {
            System.out.println("\n🔥 STREAK CELEBRATION:");
            System.out.println("   You're on a " + rewardSystem.getCurrentStreak() + " day completion streak!");
            if (rewardSystem.getCurrentStreak() >= 7) {
                System.out.println("   🏆 This consistency shows incredible strength");
            }
        }
        
        // Random neurodivergent-specific encouragement
        String[] specificEncouragement = {
            "🧠 Your brain's unique wiring creates perspectives others can't see",
            "⚡ Hyperfocus and deep interests are superpowers when channeled well",
            "🌈 Your sensitivity to the world makes you incredibly empathetic",
            "🎯 Pattern recognition and attention to detail are your strengths",
            "💫 The world needs minds that work differently - like yours",
            "🔍 Your ability to see details others miss is invaluable",
            "🌊 Your emotional depth creates authentic connections",
            "⭐ Neurodivergent minds drive innovation and creativity"
        };
        
        System.out.println("\n🌈 NEURODIVERGENT PRIDE:");
        System.out.println("   " + specificEncouragement[(int) (Math.random() * specificEncouragement.length)]);
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
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
    
    private void displayGoodbyeMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🌟 GOODBYE FROM ENHANCED NUDGE! 🌟");
        System.out.println("=".repeat(60));
        System.out.println("💛 You did great today!");
        System.out.println("✨ Remember to be kind to yourself!");
        System.out.println("🧠 Your neurodivergent brain is amazing!");
        System.out.println("🌈 See you next time!");
        System.out.println("💪 You've got this - always!");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Main method - entry point of the enhanced application
     */
    public static void main(String[] args) {
        EnhancedReminderApp app = new EnhancedReminderApp();
        app.run();
    }
}