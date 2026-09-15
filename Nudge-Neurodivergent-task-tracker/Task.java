import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a task in the Nudge application
 */
public class Task {
    private static int nextId = 1;
    
    private final int id;
    private String name;
    private LocalDateTime deadline;
    private ReminderType reminderType;
    private TaskCategory category;
    protected boolean isCompleted;
    private int pointValue;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private int estimatedDurationMinutes;
    private String contextNotes;
    private List<LocalDateTime> reminderTimes;
    private boolean isHighPriority;
    
    public Task(String name, LocalDateTime deadline, ReminderType reminderType, 
                TaskCategory category, int pointValue) {
        this.id = nextId++;
        this.name = name;
        this.deadline = deadline;
        this.reminderType = reminderType;
        this.category = category;
        this.pointValue = pointValue;
        this.isCompleted = false;
        this.createdAt = LocalDateTime.now();
        this.estimatedDurationMinutes = 30; // default
        this.contextNotes = "";
        this.reminderTimes = new ArrayList<>();
        this.isHighPriority = false;
        
        // Set default reminder times (30 min and 10 min before)
        this.reminderTimes.add(deadline.minusMinutes(30));
        this.reminderTimes.add(deadline.minusMinutes(10));
    }
    
    public void markCompleted() {
        this.isCompleted = true;
        this.completedAt = LocalDateTime.now();
    }
    
    public boolean isOverdue() {
        return DateUtils.isOverdue(deadline) && !isCompleted;
    }
    
    public boolean isDueSoon() {
        return DateUtils.isDueSoon(deadline) && !isCompleted;
    }
    
    public String getReminderMessage() {
        String taskInfo = "⏰ " + name + " is due " + DateUtils.formatForDisplay(deadline);
        
        switch (reminderType) {
            case TEXT:
                return "📝 Reminder: " + taskInfo;
            case EMOJI:
                return getEmojiReminder() + " " + taskInfo;
            case VIBEY:
                return getVibeyReminder() + " " + taskInfo;
            case GENTLE:
                return getGentleReminder() + " " + taskInfo;
            default:
                return taskInfo;
        }
    }
    
    private String getEmojiReminder() {
        String[] emojis = {"🌟", "✨", "💫", "🎯", "🚀", "💪", "🔥"};
        return emojis[(int) (Math.random() * emojis.length)];
    }
    
    private String getVibeyReminder() {
        String[] vibes = {
            "✨ Gentle vibe check -",
            "🌸 Soft reminder energy -",
            "💫 Just checking in -",
            "🌈 Friendly nudge -",
            "☀️ Sunshine reminder -"
        };
        return vibes[(int) (Math.random() * vibes.length)];
    }
    
    private String getGentleReminder() {
        String[] gentle = {
            "🌸 Hey, just a gentle reminder:",
            "💛 No pressure, but maybe:",
            "🤗 When you're ready:",
            "☁️ Soft nudge:",
            "🌺 Gentle check-in:"
        };
        return gentle[(int) (Math.random() * gentle.length)];
    }
    
    // Enhanced methods for time management
    public void addReminderTime(LocalDateTime reminderTime) {
        reminderTimes.add(reminderTime);
    }
    
    public void removeReminderTime(LocalDateTime reminderTime) {
        reminderTimes.remove(reminderTime);
    }
    
    public boolean hasActiveReminders() {
        LocalDateTime now = LocalDateTime.now();
        return reminderTimes.stream().anyMatch(time -> 
            time.isAfter(now.minusMinutes(5)) && time.isBefore(now.plusMinutes(5))
        );
    }
    
    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }
    public ReminderType getReminderType() { return reminderType; }
    public void setReminderType(ReminderType reminderType) { this.reminderType = reminderType; }
    public TaskCategory getCategory() { return category; }
    public void setCategory(TaskCategory category) { this.category = category; }
    public boolean isCompleted() { return isCompleted; }
    public int getPointValue() { return pointValue; }
    public void setPointValue(int pointValue) { this.pointValue = pointValue; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public int getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(int minutes) { this.estimatedDurationMinutes = minutes; }
    public String getContextNotes() { return contextNotes; }
    public void setContextNotes(String notes) { this.contextNotes = notes; }
    public List<LocalDateTime> getReminderTimes() { return new ArrayList<>(reminderTimes); }
    public boolean isHighPriority() { return isHighPriority; }
    public void setHighPriority(boolean highPriority) { this.isHighPriority = highPriority; }
    
    @Override
    public String toString() {
        String status = isCompleted ? "✅" : (isOverdue() ? "⚠️" : "📋");
        String dueDateStr = DateUtils.formatForDisplay(deadline);
        
        return String.format("%s [%d] %s (%s) - %s | %d pts | %s", 
            status, id, name, category.getDisplayName(), dueDateStr, pointValue, reminderType.getDisplayName());
    }
}
