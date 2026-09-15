import java.util.ArrayList;
import java.util.List;

/**
 * Manages the reward system, points, streaks, and motivational feedback
 */
public class RewardSystem {
    private int totalPoints;
    private int currentStreak;
    private int longestStreak;
    private int level;
    private List<String> achievements;
    
    public RewardSystem() {
        this.totalPoints = 0;
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.level = 1;
        this.achievements = new ArrayList<>();
    }
    
    /**
     * Awards points for completing a task and updates streaks
     */
    public String awardPoints(int points) {
        totalPoints += points;
        currentStreak++;
        
        if (currentStreak > longestStreak) {
            longestStreak = currentStreak;
        }
        
        checkForLevelUp();
        checkForAchievements();
        
        return getCompletionMessage(points);
    }
    
    /**
     * Resets current streak when a task is missed
     */
    public String missedTask() {
        currentStreak = 0;
        return getSelfCheckInMessage();
    }
    
    /**
     * Checks if user has leveled up
     */
    private void checkForLevelUp() {
        int newLevel = (totalPoints / 100) + 1;
        if (newLevel > level) {
            level = newLevel;
            achievements.add("🎉 Reached Level " + level + "!");
        }
    }
    
    /**
     * Checks for various achievements
     */
    private void checkForAchievements() {
        if (currentStreak == 3 && !achievements.contains("First Strike!")) {
            achievements.add("🔥 First Strike! - 3 tasks in a row!");
        }
        if (currentStreak == 7 && !achievements.contains("Week Warrior!")) {
            achievements.add("⚡ Week Warrior! - 7 tasks in a row!");
        }
        if (currentStreak == 14 && !achievements.contains("Fortnight Fighter!")) {
            achievements.add("💪 Fortnight Fighter! - 14 tasks in a row!");
        }
        if (totalPoints >= 500 && !achievements.contains("Point Collector!")) {
            achievements.add("💎 Point Collector! - 500 total points!");
        }
        if (totalPoints >= 1000 && !achievements.contains("Point Master!")) {
            achievements.add("👑 Point Master! - 1000 total points!");
        }
    }
    
    /**
     * Returns an encouraging completion message
     */
    private String getCompletionMessage(int points) {
        String[] messages = {
            "🎉 You did that! +" + points + " points!",
            "✨ Straight royalty energy! +" + points + " points!",
            "🔥 That's how we win the day! +" + points + " points!",
            "💪 Look at you go! +" + points + " points!",
            "🌟 Absolutely crushing it! +" + points + " points!",
            "🚀 You're unstoppable! +" + points + " points!",
            "💫 Task conquered! +" + points + " points!",
            "👑 Royal execution! +" + points + " points!",
            "⚡ Power move! +" + points + " points!",
            "🎯 Bullseye! +" + points + " points!"
        };
        
        String baseMessage = messages[(int) (Math.random() * messages.length)];
        
        if (currentStreak > 1) {
            baseMessage += " 🔥 " + currentStreak + " streak!";
        }
        
        return baseMessage;
    }
    
    /**
     * Returns a supportive self-check-in message
     */
    private String getSelfCheckInMessage() {
        String[] messages = {
            "💛 No judgment here! What's making things tough today?",
            "🤗 Hey, it's okay. What's blocking you right now?",
            "🌸 Gentle check-in: How are you feeling about this task?",
            "☁️ No pressure! What would make this easier?",
            "💙 You're human. What support do you need today?",
            "🌈 Tomorrow's a fresh start. What happened today?",
            "🤝 We all have tough days. What's on your mind?",
            "🌻 Self-compassion time: What's challenging you?"
        };
        
        return messages[(int) (Math.random() * messages.length)];
    }
    
    /**
     * Returns evidence-based affirmations for neurodivergent individuals
     * Based on research in positive psychology, self-compassion theory, and neurodivergent strengths
     */
    public String getRandomAffirmation() {
        String[] evidenceBasedAffirmations = {
            // Self-compassion research (Kristin Neff)
            "🌟 You're treating yourself with the same kindness you'd show a good friend",
            "💛 Self-compassion is proven to reduce anxiety and increase motivation",
            "🤗 Acknowledging your struggles is the first step to overcoming them",
            
            // Growth mindset (Carol Dweck)
            "🧠 Your brain can grow and change - neuroplasticity is real",
            "✨ Effort and practice matter more than natural ability",
            "📈 Every challenge is an opportunity for your brain to strengthen",
            
            // Neurodivergent strengths research
            "🎯 Hyperfocus is a superpower when channeled well",
            "🌈 Your pattern recognition abilities are exceptional",
            "⚡ Your ability to think outside the box creates innovation",
            "🔍 Your attention to detail catches what others miss",
            "💫 Your intense interests lead to deep expertise",
            
            // Executive function accommodation
            "🛠️ Using tools and systems isn't cheating - it's smart self-management",
            "📝 External structure supports your internal creativity",
            "⏰ Working with your natural rhythms, not against them, is wisdom",
            
            // RSD and emotional regulation
            "💔 Rejection sensitivity shows how deeply you care about connections",
            "🌊 Big emotions are signs of a rich inner life",
            "🎭 Masking takes energy - rest is necessary and valid",
            
            // Progress and perfectionism
            "📊 Progress isn't linear - setbacks are part of the journey",
            "🎯 'Good enough' is often better than perfect",
            "🚀 Done is better than perfect when it moves you forward",
            
            // Identity and belonging
            "🏠 You belong in spaces where your contributions are valued",
            "👥 Your neurodivergent perspective makes teams stronger",
            "🌍 The world needs minds that work like yours",
            
            // Daily life validation
            "☀️ Showing up is enough on difficult days",
            "💪 You're managing more complexity than most people realize",
            "🌱 Small steps still move you toward your goals",
            "🎨 Your unique way of processing the world is valuable",
            "⭐ You don't need to earn your worth - you already have it"
        };
        
        return evidenceBasedAffirmations[(int) (Math.random() * evidenceBasedAffirmations.length)];
    }
    
    /**
     * Returns mood-specific affirmations based on current neurodivergent state
     */
    public String getMoodSpecificAffirmation(EnergyTracker.MoodState mood) {
        switch (mood) {
            case OVERWHELMED:
                return "🌊 Overwhelm means you care deeply. Breaking things down is a strength, not a weakness.";
            case ANXIOUS:
                return "🛡️ Your anxiety is your brain trying to protect you. Gentle planning helps it feel safe.";
            case SENSORY_OVERLOAD:
                return "🌿 Your nervous system is asking for care. Listening to these needs is self-awareness.";
            case REJECTION_SENSITIVE:
                return "💝 RSD shows your capacity for deep connection. Your sensitivity is a gift to the world.";
            case HYPERFOCUS_CRASH:
                return "🔋 Post-hyperfocus fatigue is real. Rest isn't lazy - it's necessary recharging.";
            case EXECUTIVE_DYSFUNCTION:
                return "🧩 Executive function varies daily. External structure supports your brilliant mind.";
            case MASKING_FATIGUE:
                return "🎭 Masking takes enormous energy. Authentic rest in safe spaces is essential.";
            case SPECIAL_INTEREST_MODE:
                return "✨ Your passion projects demonstrate your capacity for expertise and dedication.";
            case HYPERFOCUS_FLOW:
                return "🎯 This focused state is when your brain architecture truly shines.";
            case STIMMING_HAPPY:
                return "🌈 Self-regulation through movement is your nervous system working beautifully.";
            default:
                return getRandomAffirmation();
        }
    }
    
    /**
     * Displays current stats
     */
    public void displayStats() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🏆 YOUR STATS");
        System.out.println("=".repeat(50));
        System.out.printf("💎 Total Points: %d\n", totalPoints);
        System.out.printf("🌟 Current Level: %d\n", level);
        System.out.printf("🔥 Current Streak: %d\n", currentStreak);
        System.out.printf("⚡ Longest Streak: %d\n", longestStreak);
        
        if (!achievements.isEmpty()) {
            System.out.println("\n🏅 ACHIEVEMENTS:");
            for (String achievement : achievements) {
                System.out.println("   " + achievement);
            }
        }
        
        System.out.println("\n" + getRandomAffirmation());
        System.out.println("=".repeat(50));
    }
    
    // Getters
    public int getTotalPoints() { return totalPoints; }
    public int getCurrentStreak() { return currentStreak; }
    public int getLongestStreak() { return longestStreak; }
    public int getLevel() { return level; }
    public List<String> getAchievements() { return new ArrayList<>(achievements); }
}
