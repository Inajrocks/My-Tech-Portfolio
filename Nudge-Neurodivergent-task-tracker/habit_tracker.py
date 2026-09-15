"""
Habit tracking system
"""

from datetime import date, timedelta
from typing import Dict, List, Set
from dataclasses import dataclass
import random
from task_manager import TaskCategory
from energy_tracker import EnergyLevel


@dataclass
class Habit:
    """Represents a habit to track"""
    name: str
    description: str
    category: TaskCategory
    target_frequency: int  # times per week
    completion_dates: Set[date]
    created_date: date
    
    def __post_init__(self):
        if not hasattr(self, 'completion_dates') or self.completion_dates is None:
            self.completion_dates = set()
    
    def mark_completed(self, completion_date: date = None):
        """Mark habit as completed for a specific date"""
        if completion_date is None:
            completion_date = date.today()
        self.completion_dates.add(completion_date)
    
    def is_completed_today(self) -> bool:
        """Check if habit was completed today"""
        return date.today() in self.completion_dates
    
    def get_weekly_progress(self) -> tuple[int, int]:
        """Get this week's progress (completed, target)"""
        today = date.today()
        week_start = today - timedelta(days=today.weekday())
        
        this_week_completions = sum(
            1 for d in self.completion_dates
            if week_start <= d <= today
        )
        
        return this_week_completions, self.target_frequency
    
    def get_current_streak(self) -> int:
        """Get current daily streak"""
        if not self.completion_dates:
            return 0
        
        streak = 0
        current_date = date.today()
        
        while current_date in self.completion_dates:
            streak += 1
            current_date -= timedelta(days=1)
        
        return streak
    
    def get_encouragement_message(self) -> str:
        """Get an encouraging message based on progress"""
        completed, target = self.get_weekly_progress()
        streak = self.get_current_streak()
        
        if completed >= target:
            messages = [
                "🎉 Weekly goal smashed! You're on fire!",
                "👑 Habit royalty! Goal achieved!",
                "⚡ Consistency champion! Target reached!"
            ]
        elif streak >= 3:
            messages = [
                f"🔥 {streak} day streak! Momentum building!",
                f"💪 {streak} days strong! Keep it rolling!",
                f"⭐ {streak} day consistency! You're amazing!"
            ]
        elif completed > 0:
            messages = [
                "🌱 Progress is progress! Every step counts!",
                "✨ Building that habit muscle! Great work!",
                "🎯 You're on the path! Keep going!"
            ]
        else:
            messages = [
                "🌟 Fresh start! Today's a perfect day to begin!",
                "💛 No pressure! Small steps lead to big changes!",
                "🌱 Every expert was once a beginner!"
            ]
        
        return random.choice(messages)


class HabitTracker:
    """Tracks habits and provides motivation"""
    
    def __init__(self):
        self.habits: List[Habit] = []
        self._create_starter_habits()
    
    def _create_starter_habits(self):
        """Create some starter habit suggestions"""
        # These are just examples - users can modify or remove them
        starter_habits = [
            Habit(
                name="Morning hydration",
                description="Drink a glass of water when you wake up",
                category=TaskCategory.SELF_CARE,
                target_frequency=7,
                completion_dates=set(),
                created_date=date.today()
            ),
            Habit(
                name="Daily medication",
                description="Take prescribed medication if applicable",
                category=TaskCategory.MEDICATION,
                target_frequency=7,
                completion_dates=set(),
                created_date=date.today()
            ),
            Habit(
                name="Gratitude moment",
                description="Notice one thing you're grateful for",
                category=TaskCategory.PERSONAL,
                target_frequency=5,
                completion_dates=set(),
                created_date=date.today()
            )
        ]
        self.habits = starter_habits
    
    def add_habit(self, name: str, description: str, category: TaskCategory, target_frequency: int):
        """Add a new habit"""
        habit = Habit(
            name=name,
            description=description,
            category=category,
            target_frequency=target_frequency,
            completion_dates=set(),
            created_date=date.today()
        )
        self.habits.append(habit)
        return habit
    
    def mark_habit_completed(self, habit_name: str, completion_date: date = None):
        """Mark a habit as completed"""
        for habit in self.habits:
            if habit.name.lower() == habit_name.lower():
                habit.mark_completed(completion_date)
                return True
        return False
    
    def get_todays_habit_reminders(self) -> List[str]:
        """Get gentle reminders for today's habits"""
        reminders = []
        incomplete_habits = [h for h in self.habits if not h.is_completed_today()]
        
        # Don't overwhelm - max 3 reminders
        for habit in incomplete_habits[:3]:
            completed, target = habit.get_weekly_progress()
            if completed < target:
                reminders.append(f"🌱 {habit.name} - {habit.description}")
        
        return reminders
    
    def display_habit_dashboard(self):
        """Display habit tracking dashboard"""
        print("\n🌱 HABIT TRACKER DASHBOARD")
        print("=" * 50)
        
        if not self.habits:
            print("No habits tracked yet. Consider adding some!")
            return
        
        for habit in self.habits:
            completed, target = habit.get_weekly_progress()
            streak = habit.get_current_streak()
            today_status = "✅" if habit.is_completed_today() else "⚪"
            
            print(f"\n{today_status} {habit.name}")
            print(f"   📊 This week: {completed}/{target}")
            print(f"   🔥 Streak: {streak} days")
            print(f"   📝 {habit.description}")
            
            # Progress bar
            progress = min(completed / target, 1.0) if target > 0 else 0
            bar_length = 20
            filled = int(progress * bar_length)
            bar = "█" * filled + "░" * (bar_length - filled)
            print(f"   [{bar}] {progress*100:.0f}%")
    
    def get_habit_suggestions(self, energy_level: EnergyLevel) -> List[str]:
        """Get habit suggestions based on energy level"""
        if energy_level.level_value <= 2:  # Low energy
            suggestions = [
                "🌸 Focus on micro-habits today (1-2 minutes)",
                "💧 Hydration habits are perfect for low energy",
                "🛁 Gentle self-care habits feel manageable",
                "📱 Use habit stacking with existing routines"
            ]
        elif energy_level.level_value >= 4:  # High energy
            suggestions = [
                "🚀 Great time to establish new habits",
                "💪 Consider increasing habit frequency goals",
                "🎯 Use this energy for habit planning",
                "⚡ Perfect for tackling avoided habits"
            ]
        else:  # Moderate energy
            suggestions = [
                "⚖️ Maintain your current habit rhythm",
                "🔄 Good time for habit consistency",
                "📋 Consider habit tracking review",
                "🌱 Steady progress is perfect progress"
            ]
        
        return suggestions
    
    def get_motivational_quote(self) -> str:
        """Get a motivational quote about habits"""
        quotes = [
            "We are what we repeatedly do. Excellence, then, is not an act, but a habit. - Aristotle",
            "Small habits, big changes. Your future self will thank you.",
            "Progress, not perfection. Every attempt builds the neural pathway.",
            "Habits are the compound interest of self-improvement.",
            "You don't have to be perfect, just consistent.",
            "The best time to plant a tree was 20 years ago. The second best time is now.",
            "Motivation gets you started. Habit keeps you going.",
            "Success is the sum of small efforts repeated day in and day out."
        ]
        return random.choice(quotes)
    
    def get_all_habits(self) -> List[Habit]:
        """Get all habits"""
        return self.habits.copy()
    
    def remove_habit(self, habit_name: str) -> bool:
        """Remove a habit"""
        for i, habit in enumerate(self.habits):
            if habit.name.lower() == habit_name.lower():
                self.habits.pop(i)
                return True
        return False