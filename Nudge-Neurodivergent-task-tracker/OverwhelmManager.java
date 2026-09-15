import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Manages sensory overwhelm and provides coping strategies for neurodivergent users
 */
public class OverwhelmManager {
    public enum OverwhelmLevel {
        CALM("😌 Calm - Feeling balanced"),
        SLIGHT("😐 Slight - Minor stress, manageable"),
        MODERATE("😰 Moderate - Feeling scattered"),
        HIGH("😵 High - Very overwhelmed"),
        CRISIS("🆘 Crisis - Need immediate support");
        
        private final String description;
        
        OverwhelmLevel(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
    }
    
    public enum OverwhelmTrigger {
        TOO_MANY_TASKS("Too many tasks at once"),
        TIME_PRESSURE("Time pressure/deadlines"),
        SENSORY_OVERLOAD("Sensory overload (noise, lights, etc.)"),
        DECISION_FATIGUE("Too many decisions to make"),
        INTERRUPTIONS("Constant interruptions"),
        PERFECTIONISM("Perfectionist thoughts"),
        SOCIAL_EXHAUSTION("Social interaction exhaustion"),
        ENVIRONMENT("Cluttered/chaotic environment"),
        UNCERTAINTY("Uncertainty about what to do"),
        ENERGY_MISMATCH("Tasks don't match current energy");
        
        private final String description;
        
        OverwhelmTrigger(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
    }
    
    public static class OverwhelmCheckin {
        private LocalDateTime timestamp;
        private OverwhelmLevel level;
        private List<OverwhelmTrigger> triggers;
        private String notes;
        
        public OverwhelmCheckin(OverwhelmLevel level, List<OverwhelmTrigger> triggers, String notes) {
            this.timestamp = LocalDateTime.now();
            this.level = level;
            this.triggers = new ArrayList<>(triggers);
            this.notes = notes;
        }
        
        // Getters
        public LocalDateTime getTimestamp() { return timestamp; }
        public OverwhelmLevel getLevel() { return level; }
        public List<OverwhelmTrigger> getTriggers() { return new ArrayList<>(triggers); }
        public String getNotes() { return notes; }
    }
    
    private OverwhelmLevel currentLevel;
    private List<OverwhelmCheckin> checkinHistory;
    private LocalDateTime lastSensoryBreak;
    
    public OverwhelmManager() {
        this.currentLevel = OverwhelmLevel.CALM;
        this.checkinHistory = new ArrayList<>();
        this.lastSensoryBreak = LocalDateTime.now().minusHours(2);
    }
    
    public void recordCheckin(OverwhelmLevel level, List<OverwhelmTrigger> triggers, String notes) {
        this.currentLevel = level;
        checkinHistory.add(new OverwhelmCheckin(level, triggers, notes));
        
        if (level.ordinal() >= OverwhelmLevel.MODERATE.ordinal()) {
            System.out.println("\n💙 Thank you for checking in. Let's work through this together.");
        }
    }
    
    public List<String> getCopingStrategies() {
        List<String> strategies = new ArrayList<>();
        
        switch (currentLevel) {
            case CALM:
                strategies.add("🌱 Great time to prepare for potential overwhelm");
                strategies.add("📋 Consider doing some planning or prep work");
                strategies.add("🧘 Maybe practice some mindfulness techniques");
                break;
                
            case SLIGHT:
                strategies.add("🌸 Take 3 deep breaths");
                strategies.add("💧 Drink some water");
                strategies.add("📝 Write down what's on your mind");
                strategies.add("⏰ Set a gentle timer for current task");
                break;
                
            case MODERATE:
                strategies.add("🛑 STOP - pause whatever you're doing");
                strategies.add("🧘 5-minute breathing exercise or meditation");
                strategies.add("📱 Put devices on silent/do not disturb");
                strategies.add("🗂️ Brain dump everything onto paper");
                strategies.add("🎯 Pick ONE thing to focus on");
                strategies.add("🌿 Consider a sensory break");
                break;
                
            case HIGH:
                strategies.add("🆘 IMMEDIATE BREAK NEEDED");
                strategies.add("🛁 Find a quiet, safe space");
                strategies.add("🎧 Use noise-canceling headphones or earplugs");
                strategies.add("🌙 Dim the lights if possible");
                strategies.add("🧸 Use comfort items (weighted blanket, fidget toy)");
                strategies.add("📞 Consider reaching out to someone supportive");
                strategies.add("⏳ This feeling will pass - you're safe");
                break;
                
            case CRISIS:
                strategies.add("🆘 CRISIS MODE - Immediate support needed");
                strategies.add("📞 Reach out to support person/helpline NOW");
                strategies.add("🛡️ Focus only on basic needs (safety, breathing, water)");
                strategies.add("🏥 Consider professional support if thoughts of harm");
                strategies.add("💛 You are not alone - this will pass");
                break;
        }
        
        return strategies;
    }
    
    public List<String> getSensoryBreakSuggestions() {
        List<String> suggestions = Arrays.asList(
            "🎧 Listen to calming music or nature sounds",
            "🌿 Step outside for fresh air",
            "🧸 Use a fidget toy or stress ball",
            "🛁 Splash cool water on your face",
            "🌙 Sit in a dimly lit, quiet space",
            "🤗 Wrap yourself in a soft blanket",
            "👃 Use a calming scent (lavender, peppermint)",
            "🧘 Practice progressive muscle relaxation",
            "🌊 Watch calming videos (waves, rain, etc.)",
            "📱 Use a meditation or breathing app"
        );
        
        // Return 3-5 random suggestions
        List<String> randomSuggestions = new ArrayList<>(suggestions);
        java.util.Collections.shuffle(randomSuggestions);
        return randomSuggestions.subList(0, Math.min(5, randomSuggestions.size()));
    }
    
    public List<String> getTaskAdjustmentSuggestions() {
        List<String> adjustments = new ArrayList<>();
        
        switch (currentLevel) {
            case CALM:
            case SLIGHT:
                adjustments.add("✅ Current capacity seems good for regular tasks");
                adjustments.add("📋 Consider tackling that challenging task you've been avoiding");
                break;
                
            case MODERATE:
                adjustments.add("📝 Break larger tasks into tiny steps");
                adjustments.add("⏰ Reduce time pressure - extend deadlines if possible");
                adjustments.add("🎯 Focus on one task at a time");
                adjustments.add("🗑️ Move non-essential tasks to tomorrow");
                adjustments.add("💡 Choose easier versions of planned tasks");
                break;
                
            case HIGH:
                adjustments.add("🛑 Cancel non-essential tasks immediately");
                adjustments.add("🧹 Focus only on basic needs (food, meds, safety)");
                adjustments.add("📞 Delegate what you can");
                adjustments.add("💙 Self-care is your only job right now");
                break;
                
            case CRISIS:
                adjustments.add("🆘 All tasks except safety needs are canceled");
                adjustments.add("💛 Your only job is to get through this moment");
                adjustments.add("🤝 Accept help from others");
                break;
        }
        
        return adjustments;
    }
    
    public boolean shouldTakeSensoryBreak() {
        if (currentLevel.ordinal() >= OverwhelmLevel.MODERATE.ordinal()) {
            return true;
        }
        
        // Suggest break if it's been more than 2 hours
        java.time.Duration timeSinceBreak = java.time.Duration.between(lastSensoryBreak, LocalDateTime.now());
        return timeSinceBreak.toHours() >= 2;
    }
    
    public void recordSensoryBreak() {
        this.lastSensoryBreak = LocalDateTime.now();
        System.out.println("🌿 Sensory break recorded. Great job taking care of yourself!");
    }
    
    public List<String> getEnvironmentSuggestions() {
        return Arrays.asList(
            "🧹 Clear your immediate workspace",
            "🌙 Adjust lighting (dimmer usually better)",
            "🔇 Reduce noise (headphones, quiet space)",
            "🌡️ Check temperature - adjust if needed",
            "🪑 Make sure you're physically comfortable",
            "📱 Put phone on silent or in another room",
            "🎵 Add calming background sounds if helpful",
            "🌱 Add something pleasant to look at (plant, photo)",
            "💧 Have water and snacks within reach",
            "🧸 Keep comfort items nearby"
        );
    }
    
    public void displayOverwhelmDashboard() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("💙 OVERWHELM CHECK-IN DASHBOARD");
        System.out.println("=".repeat(50));
        
        System.out.println("Current Status: " + currentLevel.getDescription());
        
        if (shouldTakeSensoryBreak()) {
            System.out.println("\n⚠️ SENSORY BREAK RECOMMENDED");
            System.out.println("It's been a while since your last break!");
        }
        
        System.out.println("\n🛠️ COPING STRATEGIES:");
        for (String strategy : getCopingStrategies()) {
            System.out.println("   " + strategy);
        }
        
        if (currentLevel.ordinal() >= OverwhelmLevel.MODERATE.ordinal()) {
            System.out.println("\n📝 TASK ADJUSTMENTS:");
            for (String adjustment : getTaskAdjustmentSuggestions()) {
                System.out.println("   " + adjustment);
            }
            
            System.out.println("\n🌿 SENSORY BREAK IDEAS:");
            for (String suggestion : getSensoryBreakSuggestions()) {
                System.out.println("   " + suggestion);
            }
        }
        
        System.out.println("\n💛 Remember: This feeling is temporary. You're doing great by checking in.");
        System.out.println("=".repeat(50));
    }
    
    public List<String> getPreventiveTips() {
        return Arrays.asList(
            "📅 Schedule regular check-ins with yourself",
            "⏰ Set gentle reminders for breaks",
            "🎯 Limit daily tasks to what feels manageable",
            "🌱 Practice saying no to non-essential requests",
            "🧘 Build in buffer time between activities",
            "📱 Use apps for white noise or calming sounds",
            "🗓️ Plan easier days after challenging ones",
            "💙 Prepare comfort strategies in advance",
            "🤝 Identify support people you can reach out to",
            "📝 Keep a list of what helps when overwhelmed"
        );
    }
    
    public String getCompassionateMessage() {
        String[] messages = {
            "💛 Your overwhelm is valid - your nervous system is trying to protect you",
            "🌊 Feelings are like waves - they rise and they fall",
            "🌱 You're not broken, you're just human with a sensitive nervous system",
            "💪 You've survived 100% of your difficult days so far",
            "🤗 It's okay to need more support than others - that's just how you're wired",
            "⭐ Your brain works differently, and that comes with both challenges and gifts",
            "🌈 Tomorrow is a fresh start with new energy",
            "💙 You deserve gentleness, especially from yourself"
        };
        
        return messages[(int) (Math.random() * messages.length)];
    }
    
    // Getters
    public OverwhelmLevel getCurrentLevel() { return currentLevel; }
    public List<OverwhelmCheckin> getCheckinHistory() { return new ArrayList<>(checkinHistory); }
    public LocalDateTime getLastSensoryBreak() { return lastSensoryBreak; }
}