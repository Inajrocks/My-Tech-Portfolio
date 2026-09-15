import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Manages morning, evening, and custom routines with flexible scheduling
 */
public class RoutineManager {
    public enum RoutineType {
        MORNING("🌅 Morning Routine"),
        EVENING("🌙 Evening Routine"),
        WORK_START("💼 Work Start Routine"),
        WORK_END("🏠 Work End Routine"),
        CUSTOM("⭐ Custom Routine");
        
        private final String displayName;
        
        RoutineType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
    
    public static class RoutineStep {
        private String description;
        private int estimatedMinutes;
        private boolean isCompleted;
        private boolean isOptional;
        
        public RoutineStep(String description, int estimatedMinutes, boolean isOptional) {
            this.description = description;
            this.estimatedMinutes = estimatedMinutes;
            this.isOptional = isOptional;
            this.isCompleted = false;
        }
        
        public void markCompleted() { this.isCompleted = true; }
        public void reset() { this.isCompleted = false; }
        
        // Getters
        public String getDescription() { return description; }
        public int getEstimatedMinutes() { return estimatedMinutes; }
        public boolean isCompleted() { return isCompleted; }
        public boolean isOptional() { return isOptional; }
        
        @Override
        public String toString() {
            String status = isCompleted ? "✅" : "⬜";
            String optionalMark = isOptional ? " (optional)" : "";
            return String.format("%s %s - %d min%s", status, description, estimatedMinutes, optionalMark);
        }
    }
    
    public static class Routine {
        private String name;
        private RoutineType type;
        private LocalTime preferredTime;
        private List<RoutineStep> steps;
        private boolean isActive;
        private int completionCount;
        
        public Routine(String name, RoutineType type, LocalTime preferredTime) {
            this.name = name;
            this.type = type;
            this.preferredTime = preferredTime;
            this.steps = new ArrayList<>();
            this.isActive = true;
            this.completionCount = 0;
        }
        
        public void addStep(String description, int minutes, boolean isOptional) {
            steps.add(new RoutineStep(description, minutes, isOptional));
        }
        
        public void removeStep(int index) {
            if (index >= 0 && index < steps.size()) {
                steps.remove(index);
            }
        }
        
        public void completeStep(int index) {
            if (index >= 0 && index < steps.size()) {
                steps.get(index).markCompleted();
            }
        }
        
        public void resetRoutine() {
            steps.forEach(RoutineStep::reset);
        }
        
        public boolean isFullyCompleted() {
            return steps.stream()
                .filter(step -> !step.isOptional())
                .allMatch(RoutineStep::isCompleted);
        }
        
        public int getCompletionPercentage() {
            if (steps.isEmpty()) return 100;
            
            long completed = steps.stream()
                .filter(step -> !step.isOptional())
                .mapToInt(step -> step.isCompleted() ? 1 : 0)
                .sum();
            
            long total = steps.stream()
                .mapToInt(step -> step.isOptional() ? 0 : 1)
                .sum();
            
            return total == 0 ? 100 : (int) ((completed * 100) / total);
        }
        
        public int getTotalEstimatedTime() {
            return steps.stream().mapToInt(RoutineStep::getEstimatedMinutes).sum();
        }
        
        public void markCompleted() {
            completionCount++;
            resetRoutine();
        }
        
        public void displayRoutine() {
            System.out.println("\n" + type.getDisplayName() + " - " + name);
            System.out.println("⏰ Preferred time: " + preferredTime);
            System.out.println("📊 Completion: " + getCompletionPercentage() + "%");
            System.out.println("⏱️ Total time: ~" + getTotalEstimatedTime() + " minutes");
            System.out.println("-".repeat(40));
            
            for (int i = 0; i < steps.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, steps.get(i));
            }
        }
        
        // Getters and setters
        public String getName() { return name; }
        public RoutineType getType() { return type; }
        public LocalTime getPreferredTime() { return preferredTime; }
        public List<RoutineStep> getSteps() { return new ArrayList<>(steps); }
        public boolean isActive() { return isActive; }
        public int getCompletionCount() { return completionCount; }
        
        public void setActive(boolean active) { this.isActive = active; }
        public void setPreferredTime(LocalTime time) { this.preferredTime = time; }
    }
    
    private Map<RoutineType, List<Routine>> routines;
    
    public RoutineManager() {
        this.routines = new HashMap<>();
        for (RoutineType type : RoutineType.values()) {
            routines.put(type, new ArrayList<>());
        }
        
        createDefaultRoutines();
    }
    
    private void createDefaultRoutines() {
        // Default morning routine
        Routine morningRoutine = new Routine("Basic Morning", RoutineType.MORNING, LocalTime.of(8, 0));
        morningRoutine.addStep("💊 Take medication", 2, false);
        morningRoutine.addStep("🚿 Shower or wash face", 10, false);
        morningRoutine.addStep("🥣 Eat breakfast", 15, false);
        morningRoutine.addStep("📋 Review today's priorities", 5, true);
        morningRoutine.addStep("🧘 5-minute mindfulness", 5, true);
        addRoutine(morningRoutine);
        
        // Default evening routine
        Routine eveningRoutine = new Routine("Wind Down", RoutineType.EVENING, LocalTime.of(21, 0));
        eveningRoutine.addStep("📱 Put devices away", 2, false);
        eveningRoutine.addStep("🦷 Brush teeth", 3, false);
        eveningRoutine.addStep("📝 Tomorrow's top 3 tasks", 5, true);
        eveningRoutine.addStep("📖 Read or listen to calming audio", 20, true);
        eveningRoutine.addStep("😴 Prepare sleep environment", 5, false);
        addRoutine(eveningRoutine);
    }
    
    public void addRoutine(Routine routine) {
        routines.get(routine.getType()).add(routine);
    }
    
    public void removeRoutine(RoutineType type, String name) {
        routines.get(type).removeIf(routine -> routine.getName().equals(name));
    }
    
    public List<Routine> getRoutinesByType(RoutineType type) {
        return new ArrayList<>(routines.get(type));
    }
    
    public List<Routine> getAllActiveRoutines() {
        List<Routine> allActive = new ArrayList<>();
        for (List<Routine> typeRoutines : routines.values()) {
            typeRoutines.stream()
                .filter(Routine::isActive)
                .forEach(allActive::add);
        }
        return allActive;
    }
    
    public Routine findRoutine(RoutineType type, String name) {
        return routines.get(type).stream()
            .filter(routine -> routine.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
    
    public List<String> getRoutineReminders() {
        List<String> reminders = new ArrayList<>();
        LocalTime now = LocalTime.now();
        
        for (Routine routine : getAllActiveRoutines()) {
            LocalTime routineTime = routine.getPreferredTime();
            
            // Check if routine time is within 30 minutes
            if (Math.abs(now.toSecondOfDay() - routineTime.toSecondOfDay()) <= 1800) {
                if (routine.getCompletionPercentage() < 100) {
                    reminders.add("⏰ " + routine.getType().getDisplayName() + 
                                " time! (" + routine.getName() + ")");
                }
            }
        }
        
        return reminders;
    }
    
    public void displayAllRoutines() {
        System.out.println("\n📋 YOUR ROUTINES");
        System.out.println("=".repeat(50));
        
        for (RoutineType type : RoutineType.values()) {
            List<Routine> typeRoutines = routines.get(type);
            if (!typeRoutines.isEmpty()) {
                System.out.println("\n" + type.getDisplayName() + ":");
                for (Routine routine : typeRoutines) {
                    if (routine.isActive()) {
                        System.out.printf("  📌 %s - %d%% complete (%d times done)\n", 
                                        routine.getName(), 
                                        routine.getCompletionPercentage(),
                                        routine.getCompletionCount());
                    }
                }
            }
        }
    }
    
    public String getRoutineStats() {
        int totalRoutines = (int) getAllActiveRoutines().size();
        int completedToday = (int) getAllActiveRoutines().stream()
            .mapToInt(Routine::getCompletionPercentage)
            .filter(percentage -> percentage == 100)
            .count();
        
        return String.format("📊 Routines: %d/%d completed today", completedToday, totalRoutines);
    }
    
    public List<String> getRoutineSuggestions(EnergyTracker.EnergyLevel energy) {
        List<String> suggestions = new ArrayList<>();
        
        switch (energy) {
            case VERY_LOW:
                suggestions.add("🌸 Keep routines super minimal today");
                suggestions.add("🛁 Focus on just the self-care essentials");
                suggestions.add("💊 Don't skip medications though!");
                break;
            case LOW:
                suggestions.add("📝 Pick just 2-3 routine items that matter most");
                suggestions.add("⏰ Maybe do them slower than usual");
                suggestions.add("🤗 Be extra gentle with yourself");
                break;
            case GOOD:
            case HIGH:
                suggestions.add("✨ Great day to tackle full routines!");
                suggestions.add("➕ Maybe add an optional step you've been skipping");
                suggestions.add("🎯 Consider optimizing your routine flow");
                break;
        }
        
        return suggestions;
    }
}