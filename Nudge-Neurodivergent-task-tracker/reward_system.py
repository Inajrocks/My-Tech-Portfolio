"""
Reward system with evidence-based affirmations for neurodivergent users
"""

import random
from typing import List
from energy_tracker import MoodState


class RewardSystem:
    """Manages the reward system, points, streaks, and motivational feedback"""
    
    def __init__(self):
        self.total_points = 0
        self.current_streak = 0
        self.longest_streak = 0
        self.level = 1
        self.achievements: List[str] = []
    
    def award_points(self, points: int) -> str:
        """Awards points for completing a task and updates streaks"""
        self.total_points += points
        self.current_streak += 1
        
        if self.current_streak > self.longest_streak:
            self.longest_streak = self.current_streak
        
        self._check_for_level_up()
        self._check_for_achievements()
        
        return self._get_completion_message(points)
    
    def missed_task(self) -> str:
        """Resets current streak when a task is missed"""
        self.current_streak = 0
        return self._get_self_checkin_message()
    
    def _check_for_level_up(self):
        """Checks if user has leveled up"""
        new_level = (self.total_points // 100) + 1
        if new_level > self.level:
            self.level = new_level
            self.achievements.append(f"🎉 Reached Level {self.level}!")
    
    def _check_for_achievements(self):
        """Checks for various achievements"""
        if self.current_streak == 3 and "First Strike!" not in self.achievements:
            self.achievements.append("🔥 First Strike! - 3 tasks in a row!")
        if self.current_streak == 7 and "Week Warrior!" not in self.achievements:
            self.achievements.append("⚡ Week Warrior! - 7 tasks in a row!")
        if self.current_streak == 14 and "Fortnight Fighter!" not in self.achievements:
            self.achievements.append("💪 Fortnight Fighter! - 14 tasks in a row!")
        if self.total_points >= 500 and "Point Collector!" not in self.achievements:
            self.achievements.append("💎 Point Collector! - 500 total points!")
        if self.total_points >= 1000 and "Point Master!" not in self.achievements:
            self.achievements.append("👑 Point Master! - 1000 total points!")
    
    def _get_completion_message(self, points: int) -> str:
        """Returns an encouraging completion message"""
        messages = [
            f"🎉 You did that! +{points} points!",
            f"✨ Straight royalty energy! +{points} points!",
            f"🔥 That's how we win the day! +{points} points!",
            f"💪 Look at you go! +{points} points!",
            f"🌟 Absolutely crushing it! +{points} points!",
            f"🚀 You're unstoppable! +{points} points!",
            f"💫 Task conquered! +{points} points!",
            f"👑 Royal execution! +{points} points!",
            f"⚡ Power move! +{points} points!",
            f"🎯 Bullseye! +{points} points!"
        ]
        
        base_message = random.choice(messages)
        
        if self.current_streak > 1:
            base_message += f" 🔥 {self.current_streak} streak!"
        
        return base_message
    
    def _get_self_checkin_message(self) -> str:
        """Returns a supportive self-check-in message"""
        messages = [
            "💛 No judgment here! What's making things tough today?",
            "🤗 Hey, it's okay. What's blocking you right now?",
            "🌸 Gentle check-in: How are you feeling about this task?",
            "☁️ No pressure! What would make this easier?",
            "💙 You're human. What support do you need today?",
            "🌈 Tomorrow's a fresh start. What happened today?",
            "🤝 We all have tough days. What's on your mind?",
            "🌻 Self-compassion time: What's challenging you?"
        ]
        
        return random.choice(messages)
    
    def get_random_affirmation(self) -> str:
        """
        Returns evidence-based affirmations for neurodivergent individuals
        Based on research in positive psychology, self-compassion theory, and neurodivergent strengths
        """
        evidence_based_affirmations = [
            # Self-compassion research (Kristin Neff)
            "🌟 You're treating yourself with the same kindness you'd show a good friend",
            "💛 Self-compassion is proven to reduce anxiety and increase motivation",
            "🤗 Acknowledging your struggles is the first step to overcoming them",
            
            # Growth mindset (Carol Dweck)
            "🧠 Your brain can grow and change - neuroplasticity is real",
            "✨ Effort and practice matter more than natural ability",
            "📈 Every challenge is an opportunity for your brain to strengthen",
            
            # Neurodivergent strengths research
            "🎯 Hyperfocus is a superpower when channeled well",
            "🌈 Your pattern recognition abilities are exceptional",
            "⚡ Your ability to think outside the box creates innovation",
            "🔍 Your attention to detail catches what others miss",
            "💫 Your intense interests lead to deep expertise",
            
            # Executive function accommodation
            "🛠️ Using tools and systems isn't cheating - it's smart self-management",
            "📝 External structure supports your internal creativity",
            "⏰ Working with your natural rhythms, not against them, is wisdom",
            
            # RSD and emotional regulation
            "💔 Rejection sensitivity shows how deeply you care about connections",
            "🌊 Big emotions are signs of a rich inner life",
            "🎭 Masking takes energy - rest is necessary and valid",
            
            # Progress and perfectionism
            "📊 Progress isn't linear - setbacks are part of the journey",
            "🎯 'Good enough' is often better than perfect",
            "🚀 Done is better than perfect when it moves you forward",
            
            # Identity and belonging
            "🏠 You belong in spaces where your contributions are valued",
            "👥 Your neurodivergent perspective makes teams stronger",
            "🌍 The world needs minds that work like yours",
            
            # Daily life validation
            "☀️ Showing up is enough on difficult days",
            "💪 You're managing more complexity than most people realize",
            "🌱 Small steps still move you toward your goals",
            "🎨 Your unique way of processing the world is valuable",
            "⭐ You don't need to earn your worth - you already have it"
        ]
        
        return random.choice(evidence_based_affirmations)
    
    def get_mood_specific_affirmation(self, mood: MoodState) -> str:
        """Returns mood-specific affirmations based on current neurodivergent state"""
        mood_affirmations = {
            MoodState.OVERWHELMED: "🌊 Overwhelm means you care deeply. Breaking things down is a strength, not a weakness.",
            MoodState.ANXIOUS: "🛡️ Your anxiety is your brain trying to protect you. Gentle planning helps it feel safe.",
            MoodState.SENSORY_OVERLOAD: "🌿 Your nervous system is asking for care. Listening to these needs is self-awareness.",
            MoodState.REJECTION_SENSITIVE: "💝 RSD shows your capacity for deep connection. Your sensitivity is a gift to the world.",
            MoodState.HYPERFOCUS_CRASH: "🔋 Post-hyperfocus fatigue is real. Rest isn't lazy - it's necessary recharging.",
            MoodState.EXECUTIVE_DYSFUNCTION: "🧩 Executive function varies daily. External structure supports your brilliant mind.",
            MoodState.MASKING_FATIGUE: "🎭 Masking takes enormous energy. Authentic rest in safe spaces is essential.",
            MoodState.SPECIAL_INTEREST_MODE: "✨ Your passion projects demonstrate your capacity for expertise and dedication.",
            MoodState.HYPERFOCUS_FLOW: "🎯 This focused state is when your brain architecture truly shines.",
            MoodState.STIMMING_HAPPY: "🌈 Self-regulation through movement is your nervous system working beautifully."
        }
        
        return mood_affirmations.get(mood, self.get_random_affirmation())
    
    def display_stats(self):
        """Displays current stats"""
        print("\n" + "=" * 50)
        print("🏆 YOUR STATS")
        print("=" * 50)
        print(f"💎 Total Points: {self.total_points}")
        print(f"🌟 Current Level: {self.level}")
        print(f"🔥 Current Streak: {self.current_streak}")
        print(f"⚡ Longest Streak: {self.longest_streak}")
        
        if self.achievements:
            print("\n🏅 ACHIEVEMENTS:")
            for achievement in self.achievements:
                print(f"   {achievement}")
        
        print(f"\n{self.get_random_affirmation()}")
        print("=" * 50)
    
    # Getters
    def get_total_points(self) -> int:
        return self.total_points
    
    def get_current_streak(self) -> int:
        return self.current_streak
    
    def get_longest_streak(self) -> int:
        return self.longest_streak
    
    def get_level(self) -> int:
        return self.level
    
    def get_achievements(self) -> List[str]:
        return self.achievements.copy()