/**
 * Enum representing different categories of tasks
 */
public enum TaskCategory {
    MEDICATION("💊 Medication"),
    SELF_CARE("🛁 Self Care"),
    CHORES("🧹 Chores"),
    WORK("💼 Work"),
    PERSONAL("🌟 Personal"),
    SOCIAL("👥 Social"),
    HEALTH("🏃 Health"),
    OTHER("📋 Other");
    
    private final String displayName;
    
    TaskCategory(String displayName) {
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
