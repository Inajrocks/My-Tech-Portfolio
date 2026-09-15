import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Tracks habit chains and provides flexible habit building support
 */
public class HabitTracker {
    public static class Habit {
        private String name;
        private String description;
        private TaskCategory category;
        private Map<LocalDate, Boolean> completionHistory;
        private int targetFrequency; // times per week
        private boolean allowFlexibility;
        private LocalDate startDate;
        private int totalCompletions;
        
        public Habit(String name, String description, TaskCategory category, int targetFrequency) {
            this.name = name;
            this.description = description;
            this.category = category;
            this.targetFrequency = targetFrequency;
            this.completionHistory = new HashMap<>();
            this.allowFlexibility = true;
            this.startDate = LocalDate.now();
            this.totalCompletions = 0;
        }
        
        public void markCompleted(LocalDate date) {
            if (!completionHistory.getOrDefault(date, false)) {
                completionHistory.put(date, true);
                totalCompletions++;
            }
        }
        
        public void markSkipped(LocalDate date) {
            completionHistory.put(date, false);
        }
        
        public int getCurrentStreak() {
            int streak = 0;
            LocalDate checkDate = LocalDate.now();
            
            while (completionHistory.getOrDefault(checkDate, false)) {
                streak++;
                checkDate = checkDate.minusDays(1);
            }
            
            return streak;
        }
        
        public int getLongestStreak() {
            int maxStreak = 0;
            int currentStreak = 0;
            
            LocalDate checkDate = startDate;
            LocalDate today = LocalDate.now();
            
            while (!checkDate.isAfter(today)) {
                if (completionHistory.getOrDefault(checkDate, false)) {
                    currentStreak++;
                    maxStreak = Math.max(maxStreak, currentStreak);
                } else {
                    currentStreak = 0;
                }
                checkDate = checkDate.plusDays(1);
            }
            
            return maxStreak;
        }
        
        public double getWeeklyCompletionRate() {
            LocalDate weekStart = LocalDate.now().minusDays(6);
            int completions = 0;
            
            for (int i = 0; i < 7; i++) {
                LocalDate checkDate = weekStart.plusDays(i);
                if (completionHistory.getOrDefault(checkDate, false)) {
                    completions++;
                }
            }
            
            return targetFrequency > 0 ? (double) completions / targetFrequency : 0;
        }
        
        public String getProgressVisualization() {
            StringBuilder visual = new StringBuilder();
            LocalDate start = LocalDate.now().minusDays(6);
            
            for (int i = 0; i < 7; i++) {
                LocalDate checkDate = start.plusDays(i);
                if (completionHistory.getOrDefault(checkDate, false)) {
                    visual.append("🟢");
                } else if (completionHistory.containsKey(checkDate)) {
                    visual.append("🔴"); // Explicitly skipped
                } else {
                    visual.append("⚪"); // Not tracked yet
                }
            }
            
            return visual.toString();
        }
        
        public boolean isOnTrack() {
            double completionRate = getWeeklyCompletionRate();
            return allowFlexibility ? completionRate >= 0.7 : completionRate >= 1.0;
        }
        
        public String getEncouragementMessage() {
            int streak = getCurrentStreak();
            double rate = getWeeklyCompletionRate();
            
            if (streak >= 7) {
                return "🔥 You're on fire! " + streak + " days strong!";
            } else if (streak >= 3) {
                return "⭐ Great momentum! " + streak + " days in a row!";
            } else if (rate >= 0.8) {
                return "🎯 You're hitting your targets beautifully!";
            } else if (rate >= 0.5) {
                return "📈 Steady progress! Keep building that habit!";
            } else {
                return "🌱 Every day is a chance to restart. You've got this!";
            }
        }
        
        // Getters and setters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public TaskCategory getCategory() { return category; }
        public int getTargetFrequency() { return targetFrequency; }
        public boolean isAllowFlexibility() { return allowFlexibility; }
        public LocalDate getStartDate() { return startDate; }
        public int getTotalCompletions() { return totalCompletions; }
        
        public void setAllowFlexibility(boolean allowFlexibility) {
            this.allowFlexibility = allowFlexibility;
        }
        
        public void setTargetFrequency(int targetFrequency) {
            this.targetFrequency = targetFrequency;
        }
        
        @Override
        public String toString() {
            return String.format("%s %s - %s (streak: %d)", 
                getProgressVisualization(), name, getEncouragementMessage(), getCurrentStreak());
        }
    }
    
    private List<Habit> habits;
    
    public HabitTracker() {
        this.habits = new ArrayList<>();
        createDefaultHabits();
    }
    
    private void createDefaultHabits() {
        // Common neurodivergent-friendly habits
        addHabit("Morning Medication", "Take prescribed medication", TaskCategory.MEDICATION, 7);
        addHabit("Hydration Check", "Drink a full glass of water", TaskCategory.HEALTH, 7);
        addHabit("5-Min Tidy", "Quick room/space organization", TaskCategory.CHORES, 5);
        addHabit("Gratitude Note", "Write one thing you're grateful for", TaskCategory.PERSONAL, 4);
        addHabit("Movement Break", "Any kind of physical movement", TaskCategory.HEALTH, 5);
    }
    
    public void addHabit(String name, String description, TaskCategory category, int targetFrequency) {
        habits.add(new Habit(name, description, category, targetFrequency));
    }
    
    public void removeHabit(String name) {
        habits.removeIf(habit -> habit.getName().equals(name));
    }
    
    public Habit findHabit(String name) {
        return habits.stream()
            .filter(habit -> habit.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    public void markHabitCompleted(String habitName, LocalDate date) {
        Habit habit = findHabit(habitName);
        if (habit != null) {
            habit.markCompleted(date);
        }
    }
    
    public void markHabitSkipped(String habitName, LocalDate date) {
        Habit habit = findHabit(habitName);
        if (habit != null) {
            habit.markSkipped(date);
        }
    }
    
    public List<Habit> getHabitsForToday() {
        return new ArrayList<>(habits);
    }
    
    public List<Habit> getHabitsByCategory(TaskCategory category) {
        return habits.stream()
            .filter(habit -> habit.getCategory() == category)
            .toList();
    }
    
    public void displayHabitDashboard() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🌱 HABIT DASHBOARD");
        System.out.println("=".repeat(60));
        
        if (habits.isEmpty()) {
            System.out.println("💡 No habits tracked yet. Consider adding some small, manageable habits!");
            return;
        }
        
        System.out.println("📊 This Week's Progress (🟢 = done, 🔴 = skipped, ⚪ = not tracked)");
        System.out.println("-".repeat(60));
        
        for (Habit habit : habits) {
            System.out.println(habit.toString());
            
            double rate = habit.getWeeklyCompletionRate();
            String status = habit.isOnTrack() ? "✅ On track" : "⚠️ Needs attention";
            System.out.printf("   📈 Weekly rate: %.0f%% | %s\n", rate * 100, status);
            System.out.println();
        }
        
        displayHabitStats();
    }
    
    private void displayHabitStats() {
        int totalHabits = habits.size();
        long habitsOnTrack = habits.stream().mapToLong(habit -> habit.isOnTrack() ? 1 : 0).sum();
        int totalStreaks = habits.stream().mapToInt(Habit::getCurrentStreak).sum();
        int longestStreak = habits.stream().mapToInt(Habit::getLongestStreak).max().orElse(0);
        
        System.out.println("📊 OVERALL STATS:");
        System.out.printf("   🎯 Habits on track: %d/%d\n", habitsOnTrack, totalHabits);
        System.out.printf("   🔥 Active streaks: %d total days\n", totalStreaks);
        System.out.printf("   ⭐ Best streak ever: %d days\n", longestStreak);
        
        if (habitsOnTrack == totalHabits && totalHabits > 0) {
            System.out.println("   🎉 All habits on track - you're absolutely crushing it!");
        } else if (habitsOnTrack >= totalHabits * 0.7) {
            System.out.println("   💪 Most habits going strong - keep up the great work!");
        } else {
            System.out.println("   🌱 Room to grow - remember, progress over perfection!");
        }
    }
    
    public List<String> getTodaysHabitReminders() {
        List<String> reminders = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (Habit habit : habits) {
            if (!habit.completionHistory.getOrDefault(today, false)) {
                // Check if habit is due today based on frequency
                double weeklyRate = habit.getWeeklyCompletionRate();
                if (weeklyRate < (double) habit.getTargetFrequency() / 7) {
                    reminders.add("🌱 Habit reminder: " + habit.getName() + " - " + habit.getDescription());
                }
            }
        }
        
        return reminders;
    }
    
    public List<String> getHabitSuggestions(EnergyTracker.EnergyLevel energy) {
        List<String> suggestions = new ArrayList<>();
        
        switch (energy) {
            case VERY_LOW:
                suggestions.add("🌸 Focus only on essential habits (like medication)");
                suggestions.add("💙 Skip optional habits today - that's totally okay");
                break;
            case LOW:
                suggestions.add("🎯 Pick your top 2 most important habits");
                suggestions.add("⏰ Do them at the easiest time of day");
                break;
            case MODERATE:
                suggestions.add("📋 Good day for your regular habit routine");
                suggestions.add("🔄 Consider habit stacking (linking habits together)");
                break;
            case GOOD:
            case HIGH:
                suggestions.add("✨ Great energy for building new habit streaks!");
                suggestions.add("➕ Maybe add a small new habit to try");
                break;
        }
        
        return suggestions;
    }
    
    public String getMotivationalQuote() {
        String[] quotes = {
            "🌱 Small habits, big changes over time",
            "🔄 Consistency beats perfection every time", 
            "⭐ You're not building habits, you're becoming the person you want to be",
            "🎯 Focus on systems, not just goals",
            "💪 Every small action is a vote for the person you're becoming",
            "🌈 Progress is progress, no matter how small",
            "🔥 Habits are the compound interest of self-improvement"
        };
        
        return quotes[(int) (Math.random() * quotes.length)];
    }
    
    public List<Habit> getAllHabits() {
        return new ArrayList<>(habits);
    }
}