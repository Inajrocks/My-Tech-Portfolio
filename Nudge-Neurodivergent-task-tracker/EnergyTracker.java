import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Tracks daily energy levels and mood to help with task management
 */
public class EnergyTracker {
    public enum EnergyLevel {
        VERY_LOW(1, "😴 Very Low - Rest day vibes"),
        LOW(2, "😌 Low - Gentle tasks only"),
        MODERATE(3, "🙂 Moderate - Steady pace"),
        GOOD(4, "😊 Good - Ready for action"),
        HIGH(5, "⚡ High - Let's tackle everything!");
        
        private final int value;
        private final String description;
        
        EnergyLevel(int value, String description) {
            this.value = value;
            this.description = description;
        }
        
        public int getValue() { return value; }
        public String getDescription() { return description; }
    }
    
    public enum MoodState {
        // Challenging states common in neurodivergent experiences
        OVERWHELMED("😵 Overwhelmed - Need support and simplification"),
        ANXIOUS("😰 Anxious - Seeking safety and predictability"),
        SENSORY_OVERLOAD("🌪️ Sensory overload - Need calm environment"),
        REJECTION_SENSITIVE("💔 RSD activated - Feeling vulnerable"),
        HYPERFOCUS_CRASH("🪫 Post-hyperfocus crash - Energy depleted"),
        EXECUTIVE_DYSFUNCTION("🧠 Executive dysfunction - Can't get started"),
        MASKING_FATIGUE("🎭 Masking fatigue - Socially drained"),
        
        // Neutral and transitional states
        NEUTRAL("😐 Neutral - Regular baseline day"),
        PROCESSING("🤔 Processing - Need time to think"),
        CAUTIOUS("🐌 Cautious - Taking things slow"),
        
        // Positive neurodivergent states
        CONTENT("😌 Content - Comfortable and steady"),
        SPECIAL_INTEREST_MODE("✨ Special interest engaged - Deep focus"),
        HYPERFOCUS_FLOW("🎯 Hyperfocus flow - In the zone"),
        STIMMING_HAPPY("🌈 Stimming happy - Self-regulation working"),
        MOTIVATED("🔥 Motivated - Ready to channel energy");
        
        private final String description;
        
        MoodState(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
        
        public boolean isChallengingState() {
            return this.ordinal() <= 6; // First 7 are challenging states
        }
        
        public boolean isPositiveState() {
            return this.ordinal() >= 10; // Last 5 are positive states
        }
    }
    
    private Map<LocalDate, EnergyLevel> energyHistory;
    private Map<LocalDate, MoodState> moodHistory;
    private Map<LocalDate, String> reflectionNotes;
    private EnergyLevel currentEnergy;
    private MoodState currentMood;
    
    public EnergyTracker() {
        this.energyHistory = new HashMap<>();
        this.moodHistory = new HashMap<>();
        this.reflectionNotes = new HashMap<>();
        this.currentEnergy = EnergyLevel.MODERATE;
        this.currentMood = MoodState.NEUTRAL;
    }
    
    public void logDailyCheckin(EnergyLevel energy, MoodState mood, String reflection) {
        LocalDate today = LocalDate.now();
        energyHistory.put(today, energy);
        moodHistory.put(today, mood);
        
        if (reflection != null && !reflection.trim().isEmpty()) {
            reflectionNotes.put(today, reflection.trim());
        }
        
        this.currentEnergy = energy;
        this.currentMood = mood;
    }
    
    public List<String> getTaskRecommendations() {
        List<String> recommendations = new ArrayList<>();
        
        // Energy-based recommendations
        switch (currentEnergy) {
            case VERY_LOW:
                recommendations.add("🛁 Focus on basic self-care tasks only");
                recommendations.add("🍵 Keep task load minimal - maybe just 1-2 things");
                recommendations.add("🌸 Consider rescheduling non-urgent items");
                recommendations.add("💤 Rest is productive too");
                break;
            case LOW:
                recommendations.add("📝 Tackle simple, low-energy tasks");
                recommendations.add("⏰ Break larger tasks into micro-steps");
                recommendations.add("🎵 Add mood-boosting activities between tasks");
                recommendations.add("🐌 Work at your own gentle pace");
                break;
            case MODERATE:
                recommendations.add("📋 Good day for routine maintenance tasks");
                recommendations.add("⚖️ Balance work and personal items");
                recommendations.add("🎯 Aim for steady, sustainable progress");
                break;
            case GOOD:
                recommendations.add("💪 Great time for challenging or avoided tasks");
                recommendations.add("📞 Handle communication tasks you've been putting off");
                recommendations.add("🧹 Tackle some of those backlogged chores");
                break;
            case HIGH:
                recommendations.add("🚀 Perfect for big projects and hyperfocus sessions");
                recommendations.add("📈 Consider batch processing similar tasks");
                recommendations.add("⚠️ Set timers to avoid burnout from overcommitting");
                break;
        }
        
        // Comprehensive mood-based recommendations
        switch (currentMood) {
            case OVERWHELMED:
                recommendations.add("🫂 Priority: self-soothing and grounding activities");
                recommendations.add("📱 Reach out to someone supportive if possible");
                recommendations.add("🌱 Only do what feels immediately manageable");
                recommendations.add("📋 Consider using a brain dump to externalize thoughts");
                break;
            case ANXIOUS:
                recommendations.add("🧘 Start with calming, predictable activities");
                recommendations.add("📝 Write down worries to clear mental space");
                recommendations.add("🎧 Use familiar background sounds or music");
                recommendations.add("✅ Focus on small, completable tasks for confidence");
                break;
            case SENSORY_OVERLOAD:
                recommendations.add("🌿 Seek quiet, low-stimulation environment");
                recommendations.add("🎧 Use noise-canceling headphones if available");
                recommendations.add("💡 Dim bright lights, reduce visual chaos");
                recommendations.add("🧸 Engage in soothing sensory activities");
                break;
            case REJECTION_SENSITIVE:
                recommendations.add("💝 Avoid tasks requiring vulnerable communication today");
                recommendations.add("🛡️ Focus on solo work where you feel competent");
                recommendations.add("🤗 Practice self-compassion and gentle self-talk");
                recommendations.add("📖 Engage with supportive content or communities");
                break;
            case HYPERFOCUS_CRASH:
                recommendations.add("🔋 Prioritize rest and gentle recharging");
                recommendations.add("🍎 Focus on basic needs: food, water, movement");
                recommendations.add("⏰ Avoid starting new intensive projects");
                recommendations.add("🌸 Be patient with your recovery process");
                break;
            case EXECUTIVE_DYSFUNCTION:
                recommendations.add("🧩 Use external structure: timers, lists, body doubling");
                recommendations.add("⚡ Start with the tiniest possible first step");
                recommendations.add("🔄 Use task-switching if stuck on one thing");
                recommendations.add("📱 Voice memos instead of writing if needed");
                break;
            case MASKING_FATIGUE:
                recommendations.add("🎭 Minimize social demands and interactions");
                recommendations.add("🏠 Create space for authentic self-expression");
                recommendations.add("🌊 Allow natural stims and self-regulation");
                recommendations.add("🔇 Reduce performance pressure on yourself");
                break;
            case SPECIAL_INTEREST_MODE:
                recommendations.add("✨ Channel this focus into relevant tasks if possible");
                recommendations.add("⏰ Set boundaries to prevent complete time loss");
                recommendations.add("🎯 Use this energy for learning or skill-building");
                recommendations.add("💫 Enjoy this state while being mindful of balance");
                break;
            case HYPERFOCUS_FLOW:
                recommendations.add("🎯 Ride this wave but set gentle boundaries");
                recommendations.add("⏰ Use timers for breaks and basic needs");
                recommendations.add("🚀 Tackle your most important or challenging tasks");
                recommendations.add("💧 Remember: hydration, food, and bathroom breaks");
                break;
            case STIMMING_HAPPY:
                recommendations.add("🌈 Maintain this regulation with movement breaks");
                recommendations.add("🎵 Incorporate rhythmic or repetitive tasks");
                recommendations.add("✨ Use this calm alertness for focus work");
                recommendations.add("🔄 Honor your body's need for movement");
                break;
            case PROCESSING:
                recommendations.add("🤔 Give yourself space for slow, thoughtful work");
                recommendations.add("📝 Use written processing over verbal when possible");
                recommendations.add("⏳ Avoid rushed decisions or time pressure");
                recommendations.add("🧠 Trust your need for processing time");
                break;
            case CAUTIOUS:
                recommendations.add("🐌 Start with familiar, low-risk tasks");
                recommendations.add("📋 Make detailed plans to increase confidence");
                recommendations.add("🛡️ Build in extra time and backup plans");
                recommendations.add("✅ Celebrate small wins to build momentum");
                break;
        }
        
        return recommendations;
    }
    
    public String getEnergyTrend() {
        if (energyHistory.size() < 3) {
            return "📊 Not enough data yet - keep tracking!";
        }
        
        List<LocalDate> recentDates = energyHistory.keySet().stream()
            .sorted((d1, d2) -> d2.compareTo(d1))
            .limit(7)
            .toList();
        
        double avgEnergy = recentDates.stream()
            .mapToInt(date -> energyHistory.get(date).getValue())
            .average()
            .orElse(3.0);
        
        if (avgEnergy >= 4.0) {
            return "📈 Your energy has been great lately! Keep up what you're doing!";
        } else if (avgEnergy >= 3.0) {
            return "⚖️ Your energy has been steady. Good balance!";
        } else {
            return "💙 Your energy has been lower lately. Be extra gentle with yourself.";
        }
    }
    
    public void displayDashboard() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🌈 ENERGY & MOOD DASHBOARD");
        System.out.println("=".repeat(50));
        
        System.out.println("Current Status:");
        System.out.println("⚡ Energy: " + currentEnergy.getDescription());
        System.out.println("💭 Mood: " + currentMood.getDescription());
        
        System.out.println("\n📊 " + getEnergyTrend());
        
        System.out.println("\n🎯 Today's Recommendations:");
        List<String> recommendations = getTaskRecommendations();
        for (String rec : recommendations) {
            System.out.println("   " + rec);
        }
        
        // Show recent reflection if available
        String todayReflection = reflectionNotes.get(LocalDate.now());
        if (todayReflection != null) {
            System.out.println("\n💭 Today's Reflection:");
            System.out.println("   \"" + todayReflection + "\"");
        }
        
        System.out.println("=".repeat(50));
    }
    
    public boolean shouldReduceTaskLoad() {
        return currentEnergy.getValue() <= 2 || currentMood == MoodState.OVERWHELMED;
    }
    
    public int getRecommendedTaskLimit() {
        switch (currentEnergy) {
            case VERY_LOW: return 1;
            case LOW: return 3;
            case MODERATE: return 5;
            case GOOD: return 7;
            case HIGH: return 10;
            default: return 5;
        }
    }
    
    // Getters
    public EnergyLevel getCurrentEnergy() { return currentEnergy; }
    public MoodState getCurrentMood() { return currentMood; }
    public Map<LocalDate, EnergyLevel> getEnergyHistory() { return new HashMap<>(energyHistory); }
    public Map<LocalDate, MoodState> getMoodHistory() { return new HashMap<>(moodHistory); }
}