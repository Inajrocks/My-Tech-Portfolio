import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Manages Pomodoro-style work sessions with breaks and time management
 */
public class PomodoroTimer {
    public enum SessionType {
        WORK("🍅 Work Session"),
        SHORT_BREAK("☕ Short Break"),
        LONG_BREAK("🌿 Long Break");
        
        private final String displayName;
        
        SessionType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
    
    public enum TimerState {
        IDLE, RUNNING, PAUSED, COMPLETED
    }
    
    private int workDuration; // minutes
    private int shortBreakDuration; // minutes
    private int longBreakDuration; // minutes
    private int sessionsUntilLongBreak;
    
    private SessionType currentSessionType;
    private TimerState state;
    private LocalDateTime sessionStartTime;
    private int currentDuration;
    private int completedSessions;
    private int totalFocusTime; // total minutes
    
    private Task currentTask;
    
    public PomodoroTimer() {
        this.workDuration = 25;
        this.shortBreakDuration = 5;
        this.longBreakDuration = 15;
        this.sessionsUntilLongBreak = 4;
        
        this.currentSessionType = SessionType.WORK;
        this.state = TimerState.IDLE;
        this.completedSessions = 0;
        this.totalFocusTime = 0;
    }
    
    public void startSession(Task task) {
        this.currentTask = task;
        this.sessionStartTime = LocalDateTime.now();
        this.state = TimerState.RUNNING;
        
        switch (currentSessionType) {
            case WORK:
                currentDuration = workDuration;
                break;
            case SHORT_BREAK:
                currentDuration = shortBreakDuration;
                break;
            case LONG_BREAK:
                currentDuration = longBreakDuration;
                break;
        }
        
        System.out.println("\n🍅 " + currentSessionType.getDisplayName() + " started!");
        if (task != null && currentSessionType == SessionType.WORK) {
            System.out.println("📋 Working on: " + task.getName());
        }
        System.out.println("⏰ Duration: " + currentDuration + " minutes");
        System.out.println("💡 Focus tip: " + getFocusTip());
    }
    
    public void pauseSession() {
        if (state == TimerState.RUNNING) {
            state = TimerState.PAUSED;
            System.out.println("⏸️ Session paused. Take a moment to breathe.");
        }
    }
    
    public void resumeSession() {
        if (state == TimerState.PAUSED) {
            state = TimerState.RUNNING;
            System.out.println("▶️ Session resumed. You've got this!");
        }
    }
    
    public void completeSession() {
        if (state != TimerState.RUNNING && state != TimerState.PAUSED) {
            return;
        }
        
        state = TimerState.COMPLETED;
        completedSessions++;
        
        if (currentSessionType == SessionType.WORK) {
            totalFocusTime += workDuration;
            System.out.println("\n🎉 Work session completed!");
            System.out.println("✨ " + getCompletionEncouragement());
            
            // Determine next session type
            if (completedSessions % sessionsUntilLongBreak == 0) {
                currentSessionType = SessionType.LONG_BREAK;
                System.out.println("🌿 Time for a long break! You've earned it!");
            } else {
                currentSessionType = SessionType.SHORT_BREAK;
                System.out.println("☕ Time for a short break!");
            }
        } else {
            System.out.println("\n🔄 Break completed! Ready to focus again?");
            currentSessionType = SessionType.WORK;
        }
        
        state = TimerState.IDLE;
    }
    
    public void skipSession() {
        System.out.println("⏭️ Session skipped. That's okay - listen to your brain!");
        state = TimerState.IDLE;
        
        if (currentSessionType != SessionType.WORK) {
            currentSessionType = SessionType.WORK;
        }
    }
    
    public String getTimeRemaining() {
        if (state != TimerState.RUNNING) {
            return "Timer not running";
        }
        
        Duration elapsed = Duration.between(sessionStartTime, LocalDateTime.now());
        long elapsedMinutes = elapsed.toMinutes();
        long remaining = Math.max(0, currentDuration - elapsedMinutes);
        
        return remaining + " minutes remaining";
    }
    
    public boolean isSessionComplete() {
        if (state != TimerState.RUNNING) {
            return false;
        }
        
        Duration elapsed = Duration.between(sessionStartTime, LocalDateTime.now());
        return elapsed.toMinutes() >= currentDuration;
    }
    
    private String getFocusTip() {
        String[] workTips = {
            "Put your phone in another room",
            "Close unnecessary browser tabs",
            "Take three deep breaths before starting",
            "Set your intention for this session",
            "Remember: progress over perfection",
            "One task at a time is plenty"
        };
        
        String[] breakTips = {
            "Step away from your workspace",
            "Do some gentle stretching",
            "Look at something far away",
            "Drink some water",
            "Take deep breaths",
            "Move your body a little"
        };
        
        String[] tips = (currentSessionType == SessionType.WORK) ? workTips : breakTips;
        return tips[(int) (Math.random() * tips.length)];
    }
    
    private String getCompletionEncouragement() {
        String[] encouragements = {
            "Your brain did amazing work!",
            "Look at you staying focused!",
            "That's the kind of energy we love!",
            "You're building such great habits!",
            "Your future self is so grateful!",
            "Progress is progress - celebrate it!"
        };
        
        return encouragements[(int) (Math.random() * encouragements.length)];
    }
    
    public void displayStats() {
        System.out.println("\n🍅 POMODORO STATS");
        System.out.println("-".repeat(30));
        System.out.printf("📊 Completed Sessions: %d\n", completedSessions);
        System.out.printf("⏰ Total Focus Time: %d minutes (%.1f hours)\n", 
                         totalFocusTime, totalFocusTime / 60.0);
        System.out.printf("🎯 Current Session: %s\n", currentSessionType.getDisplayName());
        System.out.printf("📱 Status: %s\n", state);
        
        if (state == TimerState.RUNNING) {
            System.out.printf("⏳ %s\n", getTimeRemaining());
        }
    }
    
    public void customizeSettings(int work, int shortBreak, int longBreak, int sessionsForLong) {
        this.workDuration = work;
        this.shortBreakDuration = shortBreak;
        this.longBreakDuration = longBreak;
        this.sessionsUntilLongBreak = sessionsForLong;
        
        System.out.println("⚙️ Pomodoro settings updated!");
        System.out.printf("   Work: %d min | Short Break: %d min | Long Break: %d min\n",
                         work, shortBreak, longBreak);
    }
    
    // Getters
    public SessionType getCurrentSessionType() { return currentSessionType; }
    public TimerState getState() { return state; }
    public int getCompletedSessions() { return completedSessions; }
    public int getTotalFocusTime() { return totalFocusTime; }
    public Task getCurrentTask() { return currentTask; }
    public int getWorkDuration() { return workDuration; }
    public int getShortBreakDuration() { return shortBreakDuration; }
    public int getLongBreakDuration() { return longBreakDuration; }
}