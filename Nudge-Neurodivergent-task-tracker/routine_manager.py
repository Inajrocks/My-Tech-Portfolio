"""
Routine management for morning and evening routines
"""

from datetime import datetime, time
from enum import Enum
from typing import List, Dict
from dataclasses import dataclass
from energy_tracker import EnergyLevel


class RoutineType(Enum):
    """Types of routines"""
    MORNING = "Morning"
    EVENING = "Evening"
    CUSTOM = "Custom"


@dataclass
class RoutineStep:
    """A single step in a routine"""
    name: str
    description: str
    estimated_minutes: int
    completed: bool = False


@dataclass
class Routine:
    """A routine with multiple steps"""
    name: str
    routine_type: RoutineType
    steps: List[RoutineStep]
    completion_count: int = 0
    
    def display_routine(self):
        """Display the routine with progress"""
        print(f"\n📅 {self.name.upper()}")
        print("-" * 40)
        completed_steps = sum(1 for step in self.steps if step.completed)
        total_time = sum(step.estimated_minutes for step in self.steps)
        
        print(f"Progress: {completed_steps}/{len(self.steps)} steps")
        print(f"Estimated time: {total_time} minutes")
        print("\nSteps:")
        
        for i, step in enumerate(self.steps, 1):
            status = "✅" if step.completed else "⚪"
            print(f"  {status} {i}. {step.name} ({step.estimated_minutes}min)")
            if step.description:
                print(f"      💡 {step.description}")
    
    def complete_step(self, step_index: int) -> bool:
        """Complete a step in the routine"""
        if 0 <= step_index < len(self.steps):
            self.steps[step_index].completed = True
            return True
        return False
    
    def reset_routine(self):
        """Reset all steps to incomplete"""
        for step in self.steps:
            step.completed = False
    
    def is_completed(self) -> bool:
        """Check if all steps are completed"""
        return all(step.completed for step in self.steps)


class RoutineManager:
    """Manages daily routines"""
    
    def __init__(self):
        self.routines: List[Routine] = []
        self._create_default_routines()
    
    def _create_default_routines(self):
        """Create default morning and evening routines"""
        # Morning routine
        morning_steps = [
            RoutineStep("Take medication", "If applicable", 2),
            RoutineStep("Drink water", "Hydrate after sleep", 2),
            RoutineStep("Check energy level", "Quick self-assessment", 1),
            RoutineStep("Review today's priorities", "3-5 main things", 5),
            RoutineStep("Breakfast/nutrition", "Fuel your brain", 15),
            RoutineStep("Quick self-care", "Hygiene, comfortable clothes", 15)
        ]
        
        morning_routine = Routine(
            name="Basic Morning",
            routine_type=RoutineType.MORNING,
            steps=morning_steps
        )
        
        # Evening routine
        evening_steps = [
            RoutineStep("Reflect on day", "What went well?", 5),
            RoutineStep("Tomorrow preparation", "Set out clothes, check calendar", 10),
            RoutineStep("Digital sunset", "Reduce screen time", 5),
            RoutineStep("Calming activity", "Reading, music, bath", 20),
            RoutineStep("Gratitude practice", "3 things you're grateful for", 3),
            RoutineStep("Sleep preparation", "Comfortable environment", 10)
        ]
        
        evening_routine = Routine(
            name="Wind Down",
            routine_type=RoutineType.EVENING,
            steps=evening_steps
        )
        
        self.routines = [morning_routine, evening_routine]
    
    def get_routine_reminders(self) -> List[str]:
        """Get routine reminders based on time of day"""
        current_hour = datetime.now().hour
        reminders = []
        
        if 6 <= current_hour <= 10:  # Morning
            morning_routines = [r for r in self.routines if r.routine_type == RoutineType.MORNING]
            for routine in morning_routines:
                if not routine.is_completed():
                    incomplete_steps = [s for s in routine.steps if not s.completed]
                    if incomplete_steps:
                        next_step = incomplete_steps[0]
                        reminders.append(f"🌅 Morning: {next_step.name}")
        
        elif 18 <= current_hour <= 23:  # Evening
            evening_routines = [r for r in self.routines if r.routine_type == RoutineType.EVENING]
            for routine in evening_routines:
                if not routine.is_completed():
                    incomplete_steps = [s for s in routine.steps if not s.completed]
                    if incomplete_steps:
                        next_step = incomplete_steps[0]
                        reminders.append(f"🌙 Evening: {next_step.name}")
        
        return reminders
    
    def get_routine_suggestions(self, energy_level: EnergyLevel) -> List[str]:
        """Get routine suggestions based on energy level"""
        suggestions = []
        
        if energy_level.level_value <= 2:  # Low energy
            suggestions.extend([
                "🌸 Keep morning routine minimal - just essentials",
                "☕ Focus on hydration and basic nutrition",
                "🛁 Prioritize comfort and gentle self-care",
                "📱 Consider voice memos instead of writing"
            ])
        elif energy_level.level_value >= 4:  # High energy
            suggestions.extend([
                "🚀 Great time to establish new routine habits",
                "📋 Consider adding planning/prep steps",
                "💪 Use this energy for routine optimization",
                "⏰ Maybe tackle some weekly prep tasks"
            ])
        else:  # Moderate energy
            suggestions.extend([
                "⚖️ Standard routines should feel manageable",
                "🎯 Focus on consistency over perfection",
                "📝 Good time for routine evaluation",
                "🔄 Consider small routine improvements"
            ])
        
        return suggestions
    
    def display_all_routines(self):
        """Display all available routines"""
        print("\n📋 YOUR ROUTINES")
        print("=" * 50)
        
        for routine in self.routines:
            completed_steps = sum(1 for step in routine.steps if step.completed)
            completion_percent = (completed_steps / len(routine.steps)) * 100
            
            routine_icon = "🌅" if routine.routine_type == RoutineType.MORNING else "🌙"
            print(f"{routine_icon} {routine.routine_type.value} Routine:")
            print(f"  📌 {routine.name} - {completion_percent:.0f}% complete ({routine.completion_count} times done)")
    
    def get_all_active_routines(self) -> List[Routine]:
        """Get all active routines"""
        return self.routines.copy()
    
    def get_routine_stats(self) -> str:
        """Get routine completion statistics"""
        total_routines = len(self.routines)
        total_completions = sum(r.completion_count for r in self.routines)
        
        return f"📅 Routines: {total_routines} active, {total_completions} total completions"
    
    def add_custom_routine(self, name: str, steps: List[RoutineStep]):
        """Add a custom routine"""
        custom_routine = Routine(
            name=name,
            routine_type=RoutineType.CUSTOM,
            steps=steps
        )
        self.routines.append(custom_routine)
        return custom_routine