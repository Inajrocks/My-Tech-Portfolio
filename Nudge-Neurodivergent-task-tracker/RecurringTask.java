import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.Set;
import java.util.HashSet;

/**
 * Represents a recurring task with flexible scheduling patterns
 */
public class RecurringTask extends Task {
    public enum RecurrenceType {
        DAILY("Daily"),
        WEEKLY("Weekly"),
        WEEKDAYS("Weekdays Only"),
        CUSTOM("Custom Pattern");
        
        private final String displayName;
        
        RecurrenceType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    private RecurrenceType recurrenceType;
    private Set<DayOfWeek> customDays;
    private int intervalDays;
    private LocalDateTime nextDue;
    private int completionCount;
    private boolean isActive;
    
    public RecurringTask(String name, LocalDateTime firstDue, ReminderType reminderType,
                        TaskCategory category, int pointValue, RecurrenceType recurrenceType) {
        super(name, firstDue, reminderType, category, pointValue);
        this.recurrenceType = recurrenceType;
        this.customDays = new HashSet<>();
        this.intervalDays = 1;
        this.nextDue = firstDue;
        this.completionCount = 0;
        this.isActive = true;
        
        setupRecurrencePattern();
    }
    
    private void setupRecurrencePattern() {
        switch (recurrenceType) {
            case DAILY:
                intervalDays = 1;
                break;
            case WEEKLY:
                intervalDays = 7;
                break;
            case WEEKDAYS:
                customDays.add(DayOfWeek.MONDAY);
                customDays.add(DayOfWeek.TUESDAY);
                customDays.add(DayOfWeek.WEDNESDAY);
                customDays.add(DayOfWeek.THURSDAY);
                customDays.add(DayOfWeek.FRIDAY);
                break;
            case CUSTOM:
                // Will be set separately
                break;
        }
    }
    
    @Override
    public void markCompleted() {
        super.markCompleted();
        completionCount++;
        scheduleNext();
    }
    
    public void scheduleNext() {
        if (!isActive) return;
        
        switch (recurrenceType) {
            case DAILY:
            case WEEKLY:
                nextDue = nextDue.plusDays(intervalDays);
                break;
            case WEEKDAYS:
            case CUSTOM:
                nextDue = findNextCustomDay();
                break;
        }
        
        // Reset completion status for next occurrence
        setCompleted(false);
        updateDeadline(nextDue);
    }
    
    private LocalDateTime findNextCustomDay() {
        LocalDateTime candidate = nextDue.plusDays(1);
        
        while (!customDays.contains(candidate.getDayOfWeek())) {
            candidate = candidate.plusDays(1);
        }
        
        return candidate;
    }
    
    public void addCustomDay(DayOfWeek day) {
        customDays.add(day);
    }
    
    public void removeCustomDay(DayOfWeek day) {
        customDays.remove(day);
    }
    
    public void setIntervalDays(int days) {
        this.intervalDays = days;
    }
    
    public void deactivate() {
        this.isActive = false;
    }
    
    public void reactivate() {
        this.isActive = true;
    }
    
    // Getters
    public RecurrenceType getRecurrenceType() { return recurrenceType; }
    public Set<DayOfWeek> getCustomDays() { return new HashSet<>(customDays); }
    public int getIntervalDays() { return intervalDays; }
    public LocalDateTime getNextDue() { return nextDue; }
    public int getCompletionCount() { return completionCount; }
    public boolean isActive() { return isActive; }
    
    @Override
    public String toString() {
        String baseString = super.toString();
        String recurrenceInfo = " | " + recurrenceType.getDisplayName();
        if (completionCount > 0) {
            recurrenceInfo += " (completed " + completionCount + " times)";
        }
        return baseString + recurrenceInfo;
    }
    
    private void setCompleted(boolean completed) {
        // Helper method to reset completion status
        try {
            java.lang.reflect.Field field = Task.class.getDeclaredField("isCompleted");
            field.setAccessible(true);
            field.set(this, completed);
        } catch (Exception e) {
            // Fallback - this shouldn't happen in normal operation
        }
    }
    
    public void updateDeadline(LocalDateTime deadline) {
        // Helper method to update deadline for recurring tasks
        setDeadline(deadline);
    }
}