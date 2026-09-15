/**
 * Enum representing different types of reminders for tasks
 */
public enum ReminderType {
    TEXT("📝 Text Reminder"),
    EMOJI("😊 Emoji Reminder"),
    VIBEY("✨ Vibe Check"),
    GENTLE("🌸 Gentle Nudge");
    
    private final String displayName;
    
    ReminderType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
