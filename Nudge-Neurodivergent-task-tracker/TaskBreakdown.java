import java.util.List;
import java.util.ArrayList;

/**
 * Helps break down large tasks into manageable subtasks for executive function support
 */
public class TaskBreakdown {
    public enum Priority {
        URGENT_IMPORTANT("🔴 Urgent & Important", 1),
        IMPORTANT_NOT_URGENT("🟡 Important, Not Urgent", 2),
        URGENT_NOT_IMPORTANT("🟠 Urgent, Not Important", 3),
        NEITHER("🟢 Neither Urgent nor Important", 4);
        
        private final String displayName;
        private final int value;
        
        Priority(String displayName, int value) {
            this.displayName = displayName;
            this.value = value;
        }
        
        public String getDisplayName() { return displayName; }
        public int getValue() { return value; }
    }
    
    public enum Difficulty {
        VERY_EASY("🟢 Very Easy - 5 min or less"),
        EASY("🔵 Easy - 15 min"),
        MODERATE("🟡 Moderate - 30-45 min"),
        HARD("🟠 Hard - 1-2 hours"),
        VERY_HARD("🔴 Very Hard - Multiple sessions");
        
        private final String displayName;
        
        Difficulty(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
    
    public static class Subtask {
        private String description;
        private Difficulty difficulty;
        private int estimatedMinutes;
        private boolean isCompleted;
        private String context; // What tools/materials needed
        
        public Subtask(String description, Difficulty difficulty, int estimatedMinutes, String context) {
            this.description = description;
            this.difficulty = difficulty;
            this.estimatedMinutes = estimatedMinutes;
            this.context = context;
            this.isCompleted = false;
        }
        
        public void markCompleted() { this.isCompleted = true; }
        
        // Getters
        public String getDescription() { return description; }
        public Difficulty getDifficulty() { return difficulty; }
        public int getEstimatedMinutes() { return estimatedMinutes; }
        public boolean isCompleted() { return isCompleted; }
        public String getContext() { return context; }
        
        @Override
        public String toString() {
            String status = isCompleted ? "✅" : "⬜";
            return String.format("%s %s (%s, ~%d min) - %s", 
                status, description, difficulty.getDisplayName(), estimatedMinutes, context);
        }
    }
    
    public static class TaskPlan {
        private Task originalTask;
        private Priority priority;
        private Difficulty overallDifficulty;
        private List<Subtask> subtasks;
        private String preparationNotes;
        private String contextSwitchNotes;
        
        public TaskPlan(Task originalTask) {
            this.originalTask = originalTask;
            this.subtasks = new ArrayList<>();
            this.priority = Priority.IMPORTANT_NOT_URGENT;
            this.overallDifficulty = Difficulty.MODERATE;
        }
        
        public void addSubtask(String description, Difficulty difficulty, int estimatedMinutes, String context) {
            subtasks.add(new Subtask(description, difficulty, estimatedMinutes, context));
        }
        
        public void removeSubtask(int index) {
            if (index >= 0 && index < subtasks.size()) {
                subtasks.remove(index);
            }
        }
        
        public void completeSubtask(int index) {
            if (index >= 0 && index < subtasks.size()) {
                subtasks.get(index).markCompleted();
            }
        }
        
        public int getCompletionPercentage() {
            if (subtasks.isEmpty()) return 0;
            
            long completed = subtasks.stream().mapToLong(subtask -> subtask.isCompleted() ? 1 : 0).sum();
            return (int) ((completed * 100) / subtasks.size());
        }
        
        public int getTotalEstimatedTime() {
            return subtasks.stream().mapToInt(Subtask::getEstimatedMinutes).sum();
        }
        
        public List<Subtask> getNextActions(int maxItems) {
            return subtasks.stream()
                .filter(subtask -> !subtask.isCompleted())
                .limit(maxItems)
                .toList();
        }
        
        public boolean isFullyCompleted() {
            return !subtasks.isEmpty() && subtasks.stream().allMatch(Subtask::isCompleted);
        }
        
        public void displayPlan() {
            System.out.println("\n📋 TASK BREAKDOWN: " + originalTask.getName());
            System.out.println("🎯 Priority: " + priority.getDisplayName());
            System.out.println("📊 Difficulty: " + overallDifficulty.getDisplayName());
            System.out.println("⏱️ Total estimated time: " + getTotalEstimatedTime() + " minutes");
            System.out.println("📈 Progress: " + getCompletionPercentage() + "%");
            
            if (preparationNotes != null && !preparationNotes.isEmpty()) {
                System.out.println("\n🎒 Preparation needed:");
                System.out.println("   " + preparationNotes);
            }
            
            if (contextSwitchNotes != null && !contextSwitchNotes.isEmpty()) {
                System.out.println("\n🔄 Context switching notes:");
                System.out.println("   " + contextSwitchNotes);
            }
            
            System.out.println("\n📝 Subtasks:");
            System.out.println("-".repeat(50));
            for (int i = 0; i < subtasks.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, subtasks.get(i));
            }
            
            List<Subtask> nextActions = getNextActions(3);
            if (!nextActions.isEmpty()) {
                System.out.println("\n⭐ Next 3 actions to focus on:");
                for (int i = 0; i < nextActions.size(); i++) {
                    System.out.printf("   %d. %s\n", i + 1, nextActions.get(i).getDescription());
                }
            }
        }
        
        // Getters and setters
        public Task getOriginalTask() { return originalTask; }
        public Priority getPriority() { return priority; }
        public Difficulty getOverallDifficulty() { return overallDifficulty; }
        public List<Subtask> getSubtasks() { return new ArrayList<>(subtasks); }
        public String getPreparationNotes() { return preparationNotes; }
        public String getContextSwitchNotes() { return contextSwitchNotes; }
        
        public void setPriority(Priority priority) { this.priority = priority; }
        public void setOverallDifficulty(Difficulty difficulty) { this.overallDifficulty = difficulty; }
        public void setPreparationNotes(String notes) { this.preparationNotes = notes; }
        public void setContextSwitchNotes(String notes) { this.contextSwitchNotes = notes; }
    }
    
    private List<TaskPlan> taskPlans;
    
    public TaskBreakdown() {
        this.taskPlans = new ArrayList<>();
    }
    
    public TaskPlan createTaskPlan(Task task) {
        TaskPlan plan = new TaskPlan(task);
        taskPlans.add(plan);
        return plan;
    }
    
    public void removeTaskPlan(Task task) {
        taskPlans.removeIf(plan -> plan.getOriginalTask().getId() == task.getId());
    }
    
    public TaskPlan findTaskPlan(Task task) {
        return taskPlans.stream()
            .filter(plan -> plan.getOriginalTask().getId() == task.getId())
            .findFirst()
            .orElse(null);
    }
    
    public List<TaskPlan> getAllTaskPlans() {
        return new ArrayList<>(taskPlans);
    }
    
    public List<TaskPlan> getTaskPlansByPriority(Priority priority) {
        return taskPlans.stream()
            .filter(plan -> plan.getPriority() == priority)
            .toList();
    }
    
    public TaskPlan suggestBreakdown(Task task) {
        TaskPlan plan = createTaskPlan(task);
        
        // AI-assisted task breakdown based on task name and category
        String taskName = task.getName().toLowerCase();
        TaskCategory category = task.getCategory();
        
        if (taskName.contains("clean") || taskName.contains("organize")) {
            plan.addSubtask("Gather cleaning supplies", Difficulty.VERY_EASY, 5, "Cleaning supplies");
            plan.addSubtask("Clear surfaces", Difficulty.EASY, 15, "Boxes/containers for sorting");
            plan.addSubtask("Deep clean", Difficulty.MODERATE, 30, "Cleaning products");
            plan.addSubtask("Organize and put away", Difficulty.EASY, 20, "Storage solutions");
            plan.setPreparationNotes("Have cleaning supplies ready, put on energizing music");
            
        } else if (taskName.contains("call") || taskName.contains("phone")) {
            plan.addSubtask("Find contact information", Difficulty.VERY_EASY, 5, "Phone/contacts app");
            plan.addSubtask("Write down key points to discuss", Difficulty.EASY, 10, "Notepad/app");
            plan.addSubtask("Make the call", Difficulty.MODERATE, 15, "Quiet space, charged phone");
            plan.addSubtask("Follow up notes", Difficulty.VERY_EASY, 5, "Calendar/reminder app");
            plan.setContextSwitchNotes("Schedule for when you have good energy and quiet space");
            
        } else if (taskName.contains("email") || taskName.contains("write")) {
            plan.addSubtask("Open email/document", Difficulty.VERY_EASY, 2, "Computer/phone");
            plan.addSubtask("Draft main points", Difficulty.EASY, 15, "Notes or outline");
            plan.addSubtask("Write full content", Difficulty.MODERATE, 25, "Focused environment");
            plan.addSubtask("Review and edit", Difficulty.EASY, 10, "Fresh eyes");
            plan.addSubtask("Send/submit", Difficulty.VERY_EASY, 3, "Double-check recipients");
            
        } else if (taskName.contains("appointment") || taskName.contains("schedule")) {
            plan.addSubtask("Check available times", Difficulty.VERY_EASY, 5, "Calendar app");
            plan.addSubtask("Find contact information", Difficulty.VERY_EASY, 5, "Phone/website");
            plan.addSubtask("Make the call/booking", Difficulty.EASY, 10, "Phone/computer");
            plan.addSubtask("Add to calendar", Difficulty.VERY_EASY, 3, "Calendar app");
            plan.addSubtask("Set reminders", Difficulty.VERY_EASY, 2, "Phone/calendar");
            
        } else {
            // Generic breakdown
            plan.addSubtask("Gather materials/information", Difficulty.EASY, 10, "Whatever task requires");
            plan.addSubtask("Start main work", Difficulty.MODERATE, 30, "Focused environment");
            plan.addSubtask("Review and finalize", Difficulty.EASY, 15, "Fresh perspective");
            plan.setPreparationNotes("Make sure you have everything needed before starting");
        }
        
        // Set priority based on task deadline and category
        if (task.isOverdue()) {
            plan.setPriority(Priority.URGENT_IMPORTANT);
        } else if (task.isDueSoon()) {
            plan.setPriority(Priority.URGENT_NOT_IMPORTANT);
        } else if (category == TaskCategory.MEDICATION || category == TaskCategory.HEALTH) {
            plan.setPriority(Priority.IMPORTANT_NOT_URGENT);
        } else {
            plan.setPriority(Priority.NEITHER);
        }
        
        return plan;
    }
    
    public void displayPriorityMatrix() {
        System.out.println("\n📊 EISENHOWER PRIORITY MATRIX");
        System.out.println("=".repeat(60));
        
        for (Priority priority : Priority.values()) {
            List<TaskPlan> plans = getTaskPlansByPriority(priority);
            if (!plans.isEmpty()) {
                System.out.println("\n" + priority.getDisplayName() + ":");
                for (TaskPlan plan : plans) {
                    System.out.printf("   📋 %s (%d%% complete)\n", 
                        plan.getOriginalTask().getName(), 
                        plan.getCompletionPercentage());
                }
            }
        }
        
        System.out.println("\n💡 FOCUS RECOMMENDATIONS:");
        System.out.println("   🔴 Do urgent & important tasks first");
        System.out.println("   🟡 Schedule time for important, non-urgent tasks");
        System.out.println("   🟠 Delegate or minimize urgent, unimportant tasks");
        System.out.println("   🟢 Eliminate or do in spare time");
    }
    
    public List<String> getExecutiveFunctionTips(EnergyTracker.EnergyLevel energy) {
        List<String> tips = new ArrayList<>();
        
        switch (energy) {
            case VERY_LOW:
                tips.add("🌸 Pick the tiniest possible next step");
                tips.add("📱 Use voice memos instead of writing");
                tips.add("🎵 Try body doubling or background sounds");
                break;
            case LOW:
                tips.add("⏰ Set a 10-minute timer and just start");
                tips.add("📝 Write down the very next physical action needed");
                tips.add("🔄 Use the 2-minute rule - if it takes less than 2 min, do it now");
                break;
            case MODERATE:
                tips.add("📋 Focus on one subtask at a time");
                tips.add("🍅 Try a Pomodoro session");
                tips.add("🎯 Set mini-goals within larger tasks");
                break;
            case GOOD:
            case HIGH:
                tips.add("🚀 Great time for tackling complex multi-step tasks");
                tips.add("📈 Consider batch processing similar subtasks");
                tips.add("🎪 Use this energy to prepare future low-energy days");
                break;
        }
        
        tips.add("🧠 Remember: your brain works differently, and that's okay");
        tips.add("💪 Progress over perfection, always");
        
        return tips;
    }
}