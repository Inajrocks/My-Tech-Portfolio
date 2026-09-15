"""
Energy and mood tracking system for neurodivergent users
"""

from datetime import date
from enum import Enum
from typing import Dict, List, Optional
from dataclasses import dataclass


class EnergyLevel(Enum):
    """Energy levels with spoon theory reference"""
    VERY_LOW = (1, "😴 Very Low - Rest day vibes")
    LOW = (2, "😌 Low - Gentle tasks only") 
    MODERATE = (3, "🙂 Moderate - Steady pace")
    GOOD = (4, "😊 Good - Ready for action")
    HIGH = (5, "⚡ High - Let's tackle everything!")
    
    def __init__(self, level_value: int, description: str):
        self.level_value = level_value
        self.description = description


class MoodState(Enum):
    """Comprehensive neurodivergent mood states"""
    # Challenging states common in neurodivergent experiences
    OVERWHELMED = ("😵 Overwhelmed - Need support and simplification", True, False)
    ANXIOUS = ("😰 Anxious - Seeking safety and predictability", True, False)
    SENSORY_OVERLOAD = ("🌪️ Sensory overload - Need calm environment", True, False)
    REJECTION_SENSITIVE = ("💔 RSD activated - Feeling vulnerable", True, False)
    HYPERFOCUS_CRASH = ("🪫 Post-hyperfocus crash - Energy depleted", True, False)
    EXECUTIVE_DYSFUNCTION = ("🧠 Executive dysfunction - Can't get started", True, False)
    MASKING_FATIGUE = ("🎭 Masking fatigue - Socially drained", True, False)
    
    # Neutral and transitional states
    NEUTRAL = ("😐 Neutral - Regular baseline day", False, False)
    PROCESSING = ("🤔 Processing - Need time to think", False, False)
    CAUTIOUS = ("🐌 Cautious - Taking things slow", False, False)
    
    # Positive neurodivergent states
    CONTENT = ("😌 Content - Comfortable and steady", False, True)
    SPECIAL_INTEREST_MODE = ("✨ Special interest engaged - Deep focus", False, True)
    HYPERFOCUS_FLOW = ("🎯 Hyperfocus flow - In the zone", False, True)
    STIMMING_HAPPY = ("🌈 Stimming happy - Self-regulation working", False, True)
    MOTIVATED = ("🔥 Motivated - Ready to channel energy", False, True)
    
    def __init__(self, description: str, is_challenging: bool, is_positive: bool):
        self.description = description
        self._is_challenging = is_challenging
        self._is_positive = is_positive
    
    def is_challenging_state(self) -> bool:
        return self._is_challenging
    
    def is_positive_state(self) -> bool:
        return self._is_positive


@dataclass
class DailyCheckin:
    """Daily check-in data"""
    date: date
    energy: EnergyLevel
    mood: MoodState
    reflection: Optional[str] = None


class EnergyTracker:
    """Tracks daily energy levels and mood to help with task management"""
    
    def __init__(self):
        self.energy_history: Dict[date, EnergyLevel] = {}
        self.mood_history: Dict[date, MoodState] = {}
        self.reflection_notes: Dict[date, str] = {}
        self.current_energy = EnergyLevel.MODERATE
        self.current_mood = MoodState.NEUTRAL
    
    def log_daily_checkin(self, energy: EnergyLevel, mood: MoodState, reflection: Optional[str] = None):
        """Log daily check-in data"""
        today = date.today()
        self.energy_history[today] = energy
        self.mood_history[today] = mood
        
        if reflection and reflection.strip():
            self.reflection_notes[today] = reflection.strip()
        
        self.current_energy = energy
        self.current_mood = mood
    
    def get_task_recommendations(self) -> List[str]:
        """Get personalized task recommendations based on current state"""
        recommendations = []
        
        # Energy-based recommendations
        if self.current_energy.level_value == 1:  # VERY_LOW
            recommendations.extend([
                "🛁 Focus on basic self-care tasks only",
                "🍵 Keep task load minimal - maybe just 1-2 things",
                "🌸 Consider rescheduling non-urgent items",
                "💤 Rest is productive too"
            ])
        elif self.current_energy.level_value == 2:  # LOW
            recommendations.extend([
                "📝 Tackle simple, low-energy tasks",
                "⏰ Break larger tasks into micro-steps",
                "🎵 Add mood-boosting activities between tasks",
                "🐌 Work at your own gentle pace"
            ])
        elif self.current_energy.level_value == 3:  # MODERATE
            recommendations.extend([
                "📋 Good day for routine maintenance tasks",
                "⚖️ Balance work and personal items",
                "🎯 Aim for steady, sustainable progress"
            ])
        elif self.current_energy.level_value == 4:  # GOOD
            recommendations.extend([
                "💪 Great time for challenging or avoided tasks",
                "📞 Handle communication tasks you've been putting off",
                "🧹 Tackle some of those backlogged chores"
            ])
        elif self.current_energy.level_value == 5:  # HIGH
            recommendations.extend([
                "🚀 Perfect for big projects and hyperfocus sessions",
                "📈 Consider batch processing similar tasks",
                "⚠️ Set timers to avoid burnout from overcommitting"
            ])
        
        # Comprehensive mood-based recommendations
        mood_recommendations = {
            MoodState.OVERWHELMED: [
                "🫂 Priority: self-soothing and grounding activities",
                "📱 Reach out to someone supportive if possible",
                "🌱 Only do what feels immediately manageable",
                "📋 Consider using a brain dump to externalize thoughts"
            ],
            MoodState.ANXIOUS: [
                "🧘 Start with calming, predictable activities",
                "📝 Write down worries to clear mental space",
                "🎧 Use familiar background sounds or music",
                "✅ Focus on small, completable tasks for confidence"
            ],
            MoodState.SENSORY_OVERLOAD: [
                "🌿 Seek quiet, low-stimulation environment",
                "🎧 Use noise-canceling headphones if available",
                "💡 Dim bright lights, reduce visual chaos",
                "🧸 Engage in soothing sensory activities"
            ],
            MoodState.REJECTION_SENSITIVE: [
                "💝 Avoid tasks requiring vulnerable communication today",
                "🛡️ Focus on solo work where you feel competent",
                "🤗 Practice self-compassion and gentle self-talk",
                "📖 Engage with supportive content or communities"
            ],
            MoodState.HYPERFOCUS_CRASH: [
                "🔋 Prioritize rest and gentle recharging",
                "🍎 Focus on basic needs: food, water, movement",
                "⏰ Avoid starting new intensive projects",
                "🌸 Be patient with your recovery process"
            ],
            MoodState.EXECUTIVE_DYSFUNCTION: [
                "🧩 Use external structure: timers, lists, body doubling",
                "⚡ Start with the tiniest possible first step",
                "🔄 Use task-switching if stuck on one thing",
                "📱 Voice memos instead of writing if needed"
            ],
            MoodState.MASKING_FATIGUE: [
                "🎭 Minimize social demands and interactions",
                "🏠 Create space for authentic self-expression",
                "🌊 Allow natural stims and self-regulation",
                "🔇 Reduce performance pressure on yourself"
            ],
            MoodState.SPECIAL_INTEREST_MODE: [
                "✨ Channel this focus into relevant tasks if possible",
                "⏰ Set boundaries to prevent complete time loss",
                "🎯 Use this energy for learning or skill-building",
                "💫 Enjoy this state while being mindful of balance"
            ],
            MoodState.HYPERFOCUS_FLOW: [
                "🎯 Ride this wave but set gentle boundaries",
                "⏰ Use timers for breaks and basic needs",
                "🚀 Tackle your most important or challenging tasks",
                "💧 Remember: hydration, food, and bathroom breaks"
            ],
            MoodState.STIMMING_HAPPY: [
                "🌈 Maintain this regulation with movement breaks",
                "🎵 Incorporate rhythmic or repetitive tasks",
                "✨ Use this calm alertness for focus work",
                "🔄 Honor your body's need for movement"
            ],
            MoodState.PROCESSING: [
                "🤔 Give yourself space for slow, thoughtful work",
                "📝 Use written processing over verbal when possible",
                "⏳ Avoid rushed decisions or time pressure",
                "🧠 Trust your need for processing time"
            ],
            MoodState.CAUTIOUS: [
                "🐌 Start with familiar, low-risk tasks",
                "📋 Make detailed plans to increase confidence",
                "🛡️ Build in extra time and backup plans",
                "✅ Celebrate small wins to build momentum"
            ]
        }
        
        if self.current_mood in mood_recommendations:
            recommendations.extend(mood_recommendations[self.current_mood])
        
        return recommendations
    
    def get_energy_trend(self) -> str:
        """Get energy trend analysis"""
        if len(self.energy_history) < 3:
            return "📊 Not enough data yet - keep tracking!"
        
        recent_dates = sorted(self.energy_history.keys(), reverse=True)[:7]
        avg_energy = sum(self.energy_history[d].level_value for d in recent_dates) / len(recent_dates)
        
        if avg_energy >= 4.0:
            return "📈 Your energy has been great lately! Keep up what you're doing!"
        elif avg_energy >= 3.0:
            return "⚖️ Your energy has been steady. Good balance!"
        else:
            return "💙 Your energy has been lower lately. Be extra gentle with yourself."
    
    def display_dashboard(self):
        """Display energy and mood dashboard"""
        print("\n" + "=" * 50)
        print("🌈 ENERGY & MOOD DASHBOARD")
        print("=" * 50)
        
        print("Current Status:")
        print(f"⚡ Energy: {self.current_energy.description}")
        print(f"💭 Mood: {self.current_mood.description}")
        
        print(f"\n📊 {self.get_energy_trend()}")
        
        print("\n🎯 Today's Recommendations:")
        recommendations = self.get_task_recommendations()
        for rec in recommendations:
            print(f"   {rec}")
        
        # Show recent reflection if available
        today_reflection = self.reflection_notes.get(date.today())
        if today_reflection:
            print("\n💭 Today's Reflection:")
            print(f'   "{today_reflection}"')
        
        print("=" * 50)
    
    def should_reduce_task_load(self) -> bool:
        """Check if task load should be reduced"""
        return (self.current_energy.level_value <= 2 or 
                self.current_mood == MoodState.OVERWHELMED)
    
    def get_recommended_task_limit(self) -> int:
        """Get recommended task limit based on current energy"""
        energy_limits = {
            1: 1,  # VERY_LOW
            2: 3,  # LOW
            3: 5,  # MODERATE
            4: 7,  # GOOD
            5: 10  # HIGH
        }
        return energy_limits.get(self.current_energy.level_value, 5)
    
    # Getters
    def get_current_energy(self) -> EnergyLevel:
        return self.current_energy
    
    def get_current_mood(self) -> MoodState:
        return self.current_mood
    
    def get_energy_history(self) -> Dict[date, EnergyLevel]:
        return self.energy_history.copy()
    
    def get_mood_history(self) -> Dict[date, MoodState]:
        return self.mood_history.copy()