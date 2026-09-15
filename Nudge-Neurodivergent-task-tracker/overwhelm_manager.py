"""
Overwhelm management and sensory support
"""

from datetime import datetime
from enum import Enum
from typing import List, Optional
from dataclasses import dataclass
from energy_tracker import MoodState


class OverwhelmLevel(Enum):
    """Levels of overwhelm"""
    CALM = "😌 Calm - Feeling balanced"
    SLIGHT = "😐 Slight - Minor stress, manageable"
    MODERATE = "😰 Moderate - Feeling scattered"
    HIGH = "😵 High - Very overwhelmed"
    CRISIS = "🆘 Crisis - Need immediate support"
    
    def __init__(self, description: str):
        self.description = description


@dataclass
class OverwhelmCheckin:
    """Record of an overwhelm check-in"""
    timestamp: datetime
    level: OverwhelmLevel
    triggers: List[str]
    notes: Optional[str] = None


class OverwhelmManager:
    """Manages sensory overwhelm and provides coping strategies"""
    
    def __init__(self):
        self.current_level = OverwhelmLevel.CALM
        self.checkin_history: List[OverwhelmCheckin] = []
        self.last_sensory_break = datetime.now()
    
    def record_checkin(self, level: OverwhelmLevel, triggers: List[str], notes: Optional[str] = None):
        """Record an overwhelm check-in"""
        self.current_level = level
        checkin = OverwhelmCheckin(
            timestamp=datetime.now(),
            level=level,
            triggers=triggers.copy(),
            notes=notes
        )
        self.checkin_history.append(checkin)
        
        if level.value >= 2:  # MODERATE or higher
            print("\n💙 Thank you for checking in. Let's work through this together.")
    
    def map_mood_to_overwhelm(self, mood: MoodState) -> OverwhelmLevel:
        """Map mood state to overwhelm level"""
        mood_mapping = {
            MoodState.OVERWHELMED: OverwhelmLevel.HIGH,
            MoodState.SENSORY_OVERLOAD: OverwhelmLevel.HIGH,
            MoodState.ANXIOUS: OverwhelmLevel.MODERATE,
            MoodState.REJECTION_SENSITIVE: OverwhelmLevel.MODERATE,
            MoodState.MASKING_FATIGUE: OverwhelmLevel.MODERATE,
            MoodState.HYPERFOCUS_CRASH: OverwhelmLevel.SLIGHT,
            MoodState.EXECUTIVE_DYSFUNCTION: OverwhelmLevel.SLIGHT,
        }
        return mood_mapping.get(mood, OverwhelmLevel.CALM)
    
    def get_coping_strategies(self) -> List[str]:
        """Get coping strategies based on current overwhelm level"""
        strategies = {
            OverwhelmLevel.CALM: [
                "🌱 Great time to prepare for potential overwhelm",
                "📋 Consider doing some planning or prep work",
                "🧘 Maybe practice some mindfulness techniques"
            ],
            OverwhelmLevel.SLIGHT: [
                "🌸 Take 3 deep breaths",
                "💧 Drink some water",
                "📝 Write down what's on your mind",
                "⏰ Set a gentle timer for current task"
            ],
            OverwhelmLevel.MODERATE: [
                "🛑 STOP - pause whatever you're doing",
                "🧘 5-minute breathing exercise or meditation",
                "📱 Put devices on silent/do not disturb",
                "🗂️ Brain dump everything onto paper",
                "🎯 Pick ONE thing to focus on",
                "🌿 Consider a sensory break"
            ],
            OverwhelmLevel.HIGH: [
                "🆘 IMMEDIATE BREAK NEEDED",
                "🛁 Find a quiet, safe space",
                "🎧 Use noise-canceling headphones or earplugs",
                "🌙 Dim the lights if possible",
                "🧸 Use comfort items (weighted blanket, fidget toy)",
                "📞 Consider reaching out to someone supportive",
                "⏳ This feeling will pass - you're safe"
            ],
            OverwhelmLevel.CRISIS: [
                "🆘 CRISIS MODE - Immediate support needed",
                "📞 Reach out to support person/helpline NOW",
                "🛡️ Focus only on basic needs (safety, breathing, water)",
                "⏸️ All tasks can wait - self-preservation first"
            ]
        }
        return strategies.get(self.current_level, [])
    
    def get_sensory_break_suggestions(self) -> List[str]:
        """Get sensory break suggestions"""
        return [
            "🎧 Listen to calming music or nature sounds",
            "🌿 Step outside for fresh air",
            "🧸 Use a fidget toy or stress ball",
            "🌊 Take slow, deep breaths (4-7-8 pattern)",
            "☕ Make a warm drink mindfully",
            "🌙 Dim the lights or close your eyes",
            "🛁 Wash your hands with cool water",
            "🌸 Look at something beautiful or calming",
            "🤗 Self-hug or pressure stimming",
            "📖 Read something comforting",
            "🎨 Doodle or color",
            "🐾 Pet an animal if available",
            "🧘 Do gentle stretches",
            "🍃 Practice progressive muscle relaxation",
            "🔇 Find complete silence for a few minutes"
        ]
    
    def get_task_adjustment_suggestions(self) -> List[str]:
        """Get suggestions for adjusting tasks based on overwhelm"""
        if self.current_level == OverwhelmLevel.CALM:
            return ["✨ You're in a good space - consider tackling challenging tasks"]
        elif self.current_level == OverwhelmLevel.SLIGHT:
            return [
                "📝 Break larger tasks into smaller chunks",
                "⏰ Add extra time buffers",
                "🎯 Focus on one task at a time"
            ]
        elif self.current_level == OverwhelmLevel.MODERATE:
            return [
                "⬇️ Reduce task load by 50%",
                "🌸 Postpone non-urgent items",
                "✅ Focus only on essential/easy tasks",
                "🔄 Consider asking for help or extensions"
            ]
        else:  # HIGH or CRISIS
            return [
                "🛑 All non-essential tasks should wait",
                "🆘 Focus only on immediate needs",
                "📞 Consider reaching out for support",
                "🌱 Self-care is the only priority right now"
            ]
    
    def get_environment_suggestions(self) -> List[str]:
        """Get environment adjustment suggestions"""
        return [
            "💡 Adjust lighting (dimmer or brighter as needed)",
            "🔇 Reduce noise (earplugs, noise-canceling, quieter space)",
            "🧹 Clear visual clutter from immediate view",
            "🌡️ Adjust temperature (fan, blanket, layers)",
            "🪑 Change seating/position for comfort",
            "🌿 Add calming elements (plants, soft textures)",
            "📱 Put away distracting devices",
            "🎨 Use calming colors in your view",
            "🌊 Add white noise or nature sounds",
            "🧸 Keep comfort items within reach",
            "🚪 Ensure you have privacy/escape routes",
            "🕯️ Consider aromatherapy if helpful",
            "💨 Improve air circulation",
            "📺 Reduce screen time/blue light",
            "🛁 Create a cozy, safe feeling space"
        ]
    
    def record_sensory_break(self):
        """Record that a sensory break was taken"""
        self.last_sensory_break = datetime.now()
        print("🌿 Sensory break recorded. Taking care of yourself is important!")
    
    def get_compassionate_message(self) -> str:
        """Get a compassionate message for self-care"""
        messages = [
            "Your nervous system is asking for care - that's wisdom, not weakness",
            "Overwhelm is information - your brain is telling you what it needs",
            "Taking breaks isn't giving up - it's sustainable self-management",
            "You're not broken for needing different accommodations",
            "Sensory needs are real needs - honor them with kindness",
            "Your limits exist to protect you - respecting them is self-love",
            "Managing a neurodivergent nervous system takes extra energy",
            "Rest is not earned - it's a basic need for all humans"
        ]
        import random
        return random.choice(messages)
    
    def display_overwhelm_dashboard(self):
        """Display current overwhelm status and recent history"""
        print("\n" + "=" * 50)
        print("😰 OVERWHELM & SENSORY DASHBOARD")
        print("=" * 50)
        
        print(f"Current Level: {self.current_level.description}")
        
        if self.checkin_history:
            recent = self.checkin_history[-1]
            time_str = recent.timestamp.strftime("%Y-%m-%d %H:%M")
            print(f"Last Check-in: {time_str}")
            if recent.notes:
                print(f"Notes: \"{recent.notes}\"")
        
        time_since_break = datetime.now() - self.last_sensory_break
        hours_since = int(time_since_break.total_seconds() / 3600)
        print(f"🌿 Last sensory break: {hours_since} hours ago")
        
        if hours_since > 4:
            print("💡 Consider taking a sensory break soon!")
        
        print("=" * 50)