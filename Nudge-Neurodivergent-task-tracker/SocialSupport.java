import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Manages body doubling sessions, accountability, and social support features
 */
public class SocialSupport {
    public enum SessionType {
        BODY_DOUBLING("👥 Body Doubling"),
        ACCOUNTABILITY("📞 Accountability Check-in"),
        CELEBRATION("🎉 Achievement Celebration"),
        SUPPORT("🤗 Support Session");
        
        private final String displayName;
        
        SessionType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
    
    public static class BodyDoublingSession {
        private LocalDateTime startTime;
        private int plannedDurationMinutes;
        private SessionType type;
        private String description;
        private Task associatedTask;
        private boolean isActive;
        private LocalDateTime endTime;
        
        public BodyDoublingSession(SessionType type, int durationMinutes, String description, Task task) {
            this.startTime = LocalDateTime.now();
            this.plannedDurationMinutes = durationMinutes;
            this.type = type;
            this.description = description;
            this.associatedTask = task;
            this.isActive = true;
        }
        
        public void endSession() {
            this.isActive = false;
            this.endTime = LocalDateTime.now();
        }
        
        public int getActualDurationMinutes() {
            LocalDateTime end = isActive ? LocalDateTime.now() : endTime;
            return (int) Duration.between(startTime, end).toMinutes();
        }
        
        public String getTimeRemaining() {
            if (!isActive) return "Session ended";
            
            int elapsed = getActualDurationMinutes();
            int remaining = Math.max(0, plannedDurationMinutes - elapsed);
            return remaining + " minutes remaining";
        }
        
        // Getters
        public LocalDateTime getStartTime() { return startTime; }
        public int getPlannedDurationMinutes() { return plannedDurationMinutes; }
        public SessionType getType() { return type; }
        public String getDescription() { return description; }
        public Task getAssociatedTask() { return associatedTask; }
        public boolean isActive() { return isActive; }
        public LocalDateTime getEndTime() { return endTime; }
    }
    
    public static class AccountabilityPartner {
        private String name;
        private String relationshipType; // friend, family, colleague, etc.
        private List<String> checkInSchedule; // days of week
        private LocalDateTime lastCheckIn;
        private int totalCheckIns;
        private boolean isActive;
        
        public AccountabilityPartner(String name, String relationshipType) {
            this.name = name;
            this.relationshipType = relationshipType;
            this.checkInSchedule = new ArrayList<>();
            this.totalCheckIns = 0;
            this.isActive = true;
        }
        
        public void recordCheckIn() {
            this.lastCheckIn = LocalDateTime.now();
            this.totalCheckIns++;
        }
        
        public boolean isCheckInDue() {
            if (lastCheckIn == null) return true;
            
            Duration timeSinceCheck = Duration.between(lastCheckIn, LocalDateTime.now());
            return timeSinceCheck.toDays() >= 7; // Weekly check-ins
        }
        
        // Getters and setters
        public String getName() { return name; }
        public String getRelationshipType() { return relationshipType; }
        public List<String> getCheckInSchedule() { return new ArrayList<>(checkInSchedule); }
        public LocalDateTime getLastCheckIn() { return lastCheckIn; }
        public int getTotalCheckIns() { return totalCheckIns; }
        public boolean isActive() { return isActive; }
        
        public void setActive(boolean active) { this.isActive = active; }
        public void addCheckInDay(String day) { checkInSchedule.add(day); }
        public void removeCheckInDay(String day) { checkInSchedule.remove(day); }
    }
    
    public static class Achievement {
        private String title;
        private String description;
        private LocalDateTime achievedDate;
        private boolean isShared;
        
        public Achievement(String title, String description) {
            this.title = title;
            this.description = description;
            this.achievedDate = LocalDateTime.now();
            this.isShared = false;
        }
        
        public void markAsShared() { this.isShared = true; }
        
        // Getters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public LocalDateTime getAchievedDate() { return achievedDate; }
        public boolean isShared() { return isShared; }
        
        @Override
        public String toString() {
            String shareStatus = isShared ? "📢 Shared" : "🤐 Private";
            return String.format("🏆 %s - %s (%s)", title, description, shareStatus);
        }
    }
    
    private List<BodyDoublingSession> sessions;
    private List<AccountabilityPartner> partners;
    private List<Achievement> achievements;
    private BodyDoublingSession currentSession;
    
    public SocialSupport() {
        this.sessions = new ArrayList<>();
        this.partners = new ArrayList<>();
        this.achievements = new ArrayList<>();
    }
    
    public BodyDoublingSession startBodyDoublingSession(int durationMinutes, String description, Task task) {
        if (currentSession != null && currentSession.isActive()) {
            currentSession.endSession();
        }
        
        currentSession = new BodyDoublingSession(SessionType.BODY_DOUBLING, durationMinutes, description, task);
        sessions.add(currentSession);
        
        System.out.println("\n👥 BODY DOUBLING SESSION STARTED");
        System.out.println("=".repeat(40));
        System.out.println("⏰ Duration: " + durationMinutes + " minutes");
        System.out.println("📋 Focus: " + description);
        if (task != null) {
            System.out.println("🎯 Task: " + task.getName());
        }
        System.out.println("\n💡 Body Doubling Tips:");
        System.out.println("   • You're not alone - others are working too");
        System.out.println("   • Focus on your own work, don't compare");
        System.out.println("   • Take breaks when you need them");
        System.out.println("   • Celebrate small progress");
        System.out.println("=".repeat(40));
        
        return currentSession;
    }
    
    public void endCurrentSession() {
        if (currentSession != null && currentSession.isActive()) {
            currentSession.endSession();
            
            System.out.println("\n🎉 BODY DOUBLING SESSION COMPLETE!");
            System.out.println("⏰ Actual duration: " + currentSession.getActualDurationMinutes() + " minutes");
            System.out.println("✨ " + getSessionCompletionMessage());
            
            currentSession = null;
        }
    }
    
    public void addAccountabilityPartner(String name, String relationshipType) {
        partners.add(new AccountabilityPartner(name, relationshipType));
        System.out.println("🤝 Added " + name + " as accountability partner!");
    }
    
    public void recordCheckIn(String partnerName) {
        AccountabilityPartner partner = findPartner(partnerName);
        if (partner != null) {
            partner.recordCheckIn();
            System.out.println("📞 Check-in recorded with " + partnerName + "!");
            System.out.println("💬 " + getCheckInPrompt());
        }
    }
    
    private AccountabilityPartner findPartner(String name) {
        return partners.stream()
            .filter(p -> p.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    public void addAchievement(String title, String description) {
        achievements.add(new Achievement(title, description));
        System.out.println("\n🏆 NEW ACHIEVEMENT UNLOCKED!");
        System.out.println("✨ " + title + " - " + description);
        System.out.println("🎉 " + getCelebrationMessage());
    }
    
    public void shareAchievement(String title) {
        Achievement achievement = achievements.stream()
            .filter(a -> a.getTitle().equalsIgnoreCase(title))
            .findFirst()
            .orElse(null);
        
        if (achievement != null) {
            achievement.markAsShared();
            System.out.println("📢 Achievement shared: " + title);
            System.out.println("💪 Sharing your wins helps inspire others!");
        }
    }
    
    public List<String> getBodyDoublingPrompts() {
        return List.of(
            "👥 Working alongside others, even virtually",
            "🎯 Stay focused on your own task",
            "💡 Others are tackling their challenges too",
            "⏰ Take breaks when your brain needs them",
            "🌟 Your presence helps others stay motivated",
            "🤝 You're part of a supportive community",
            "📈 Every minute of focus is progress",
            "💪 You're building great work habits"
        );
    }
    
    public List<String> getDueCheckIns() {
        List<String> dueCheckIns = new ArrayList<>();
        
        for (AccountabilityPartner partner : partners) {
            if (partner.isActive() && partner.isCheckInDue()) {
                dueCheckIns.add("📞 Check-in due with " + partner.getName());
            }
        }
        
        return dueCheckIns;
    }
    
    public void displaySocialDashboard() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🤝 SOCIAL SUPPORT DASHBOARD");
        System.out.println("=".repeat(50));
        
        // Current session
        if (currentSession != null && currentSession.isActive()) {
            System.out.println("🟢 ACTIVE SESSION:");
            System.out.println("   " + currentSession.getType().getDisplayName());
            System.out.println("   " + currentSession.getTimeRemaining());
            if (currentSession.getAssociatedTask() != null) {
                System.out.println("   Working on: " + currentSession.getAssociatedTask().getName());
            }
        } else {
            System.out.println("⚪ No active session");
        }
        
        // Accountability partners
        System.out.println("\n👥 ACCOUNTABILITY PARTNERS:");
        if (partners.isEmpty()) {
            System.out.println("   💡 Consider adding someone for regular check-ins!");
        } else {
            for (AccountabilityPartner partner : partners) {
                if (partner.isActive()) {
                    String status = partner.isCheckInDue() ? "📞 Due" : "✅ Current";
                    System.out.printf("   %s %s (%s) - %d check-ins\n", 
                        status, partner.getName(), partner.getRelationshipType(), partner.getTotalCheckIns());
                }
            }
        }
        
        // Recent achievements
        System.out.println("\n🏆 RECENT ACHIEVEMENTS:");
        List<Achievement> recentAchievements = achievements.stream()
            .sorted((a, b) -> b.getAchievedDate().compareTo(a.getAchievedDate()))
            .limit(3)
            .toList();
        
        if (recentAchievements.isEmpty()) {
            System.out.println("   🌟 Your achievements will appear here!");
        } else {
            for (Achievement achievement : recentAchievements) {
                System.out.println("   " + achievement);
            }
        }
        
        // Session stats
        System.out.println("\n📊 SESSION STATS:");
        int totalSessions = sessions.size();
        int totalMinutes = sessions.stream().mapToInt(BodyDoublingSession::getActualDurationMinutes).sum();
        System.out.printf("   Total sessions: %d\n", totalSessions);
        System.out.printf("   Total focus time: %d minutes (%.1f hours)\n", totalMinutes, totalMinutes / 60.0);
        
        System.out.println("=".repeat(50));
    }
    
    private String getSessionCompletionMessage() {
        String[] messages = {
            "You showed up and did the work - that's what matters!",
            "Look at you building that focus muscle!",
            "Every session makes the next one easier!",
            "You're proving to yourself that you can do this!",
            "That's how we build sustainable work habits!",
            "Your brain is learning to focus better each time!"
        };
        
        return messages[(int) (Math.random() * messages.length)];
    }
    
    private String getCheckInPrompt() {
        String[] prompts = {
            "What's one win you want to share?",
            "What's been challenging lately?",
            "What support do you need this week?",
            "What are you proud of recently?",
            "What's your focus for the next few days?",
            "How are you taking care of yourself?"
        };
        
        return prompts[(int) (Math.random() * prompts.length)];
    }
    
    private String getCelebrationMessage() {
        String[] messages = {
            "This is exactly the kind of progress we love to see!",
            "You're building something amazing, one step at a time!",
            "Look at you showing up for yourself!",
            "This achievement represents real growth!",
            "You should feel proud of this accomplishment!",
            "Every achievement builds momentum for the next one!"
        };
        
        return messages[(int) (Math.random() * messages.length)];
    }
    
    public List<String> getSocialMotivationTips() {
        return List.of(
            "🤝 Connection reduces the isolation that often comes with ADHD",
            "👥 Body doubling works because it provides gentle accountability",
            "🗣️ Sharing struggles makes them feel less overwhelming",
            "🎉 Celebrating wins together amplifies the positive feelings",
            "💪 Others believe in you even when you don't believe in yourself",
            "🌟 Your success inspires others in their journey too",
            "🤗 It's okay to need support - that's what community is for"
        );
    }
    
    // Getters
    public List<BodyDoublingSession> getAllSessions() { return new ArrayList<>(sessions); }
    public List<AccountabilityPartner> getPartners() { return new ArrayList<>(partners); }
    public List<Achievement> getAchievements() { return new ArrayList<>(achievements); }
    public BodyDoublingSession getCurrentSession() { return currentSession; }
}