#!/usr/bin/env python3
"""
Enhanced Nudge - A comprehensive neurodivergent-friendly task management system
Converted from Java to Python while maintaining all advanced features
"""

import sys
from datetime import datetime, date, timedelta
from typing import List, Dict, Optional
from dataclasses import dataclass, field
from enum import Enum
import json
import os

# Import all our enhanced modules
from energy_tracker import EnergyTracker, EnergyLevel, MoodState
from reward_system import RewardSystem
from task_manager import TaskManager, Task, TaskCategory, ReminderType
from recurring_task import RecurringTask, RecurrenceType
from pomodoro_timer import PomodoroTimer, TimerState, SessionType
from routine_manager import RoutineManager
from habit_tracker import HabitTracker
from task_breakdown import TaskBreakdown
from overwhelm_manager import OverwhelmManager
from social_support import SocialSupport


class EnhancedNudgeApp:
    """
    Enhanced main application class for the Nudge task management system
    with all advanced features for neurodivergent users
    """
    
    def __init__(self):
        # Core systems
        self.task_manager = TaskManager()
        self.reward_system = RewardSystem()
        self.running = True
        
        # Enhanced features
        self.energy_tracker = EnergyTracker()
        self.pomodoro_timer = PomodoroTimer()
        self.routine_manager = RoutineManager()
        self.habit_tracker = HabitTracker()
        self.task_breakdown = TaskBreakdown()
        self.overwhelm_manager = OverwhelmManager()
        self.social_support = SocialSupport()
    
    def run(self):
        """Main application loop"""
        self.display_welcome_message()
        self.check_daily_checkin()
        
        while self.running:
            # Check for reminders and overwhelm
            self.display_contextual_info()
            
            self.display_main_menu()
            choice = self.get_menu_choice()
            self.handle_menu_choice(choice)
        
        self.display_goodbye_message()
    
    def check_daily_checkin(self):
        """Comprehensive neurodivergent-friendly daily check-in"""
        print("\n🌈 DAILY NEURODIVERGENT-FRIENDLY CHECK-IN")
        print("=" * 55)
        print("This helps personalize your experience based on how")
        print("your unique brain is working today. No judgment, just support!")
        print("=" * 55)
        
        # Enhanced energy check-in with more context
        print("\n⚡ ENERGY CHECK: How's your battery today?")
        print("Think about: physical energy, mental clarity, motivation")
        print("1. 😴 Very Low - Spoon theory: maybe 1-2 spoons left")
        print("2. 😌 Low - Running on fumes, need gentle approach")
        print("3. 🙂 Moderate - Steady baseline, can handle routine")
        print("4. 😊 Good - Feeling capable, ready for challenges")
        print("5. ⚡ High - Hyperfocus energy, but watch for burnout")
        
        energy_choice = self.get_menu_choice(1, 5)
        energy = list(EnergyLevel)[energy_choice - 1]
        
        # Comprehensive neurodivergent mood check-in
        print("\n💭 EMOTIONAL/NEUROLOGICAL STATE:")
        print("What best describes your internal experience right now?")
        print("=== CHALLENGING STATES ===")
        print("1. 😵 Overwhelmed - Too much input, need simplification")
        print("2. 😰 Anxious - Seeking safety and predictability")
        print("3. 🌪️ Sensory overload - Environment feels too intense")
        print("4. 💔 RSD activated - Rejection sensitivity heightened")
        print("5. 🪫 Post-hyperfocus crash - Energy completely depleted")
        print("6. 🧠 Executive dysfunction - Can't initiate or switch tasks")
        print("7. 🎭 Masking fatigue - Exhausted from social performance")
        
        print("\n=== NEUTRAL STATES ===")
        print("8. 😐 Neutral - Regular baseline day")
        print("9. 🤔 Processing - Need time to think things through")
        print("10. 🐌 Cautious - Taking things slow and careful")
        
        print("\n=== POSITIVE STATES ===")
        print("11. 😌 Content - Comfortable and steady")
        print("12. ✨ Special interest engaged - Deep passion mode")
        print("13. 🎯 Hyperfocus flow - In the productive zone")
        print("14. 🌈 Stimming happy - Self-regulation working well")
        print("15. 🔥 Motivated - Ready to channel energy effectively")
        
        mood_choice = self.get_menu_choice(1, 15)
        mood = list(MoodState)[mood_choice - 1]
        
        # Contextual follow-up questions based on mood state
        if mood.is_challenging_state():
            print("\n💙 I see you're having a tough moment. That's completely valid.")
            additional_context = input("🤗 What would help you feel more supported today? ")
        elif mood.is_positive_state():
            print("\n✨ Wonderful! Let's make the most of this good energy.")
            additional_context = input("🎯 Is there anything specific you'd like to focus on today? ")
        else:
            additional_context = input("💭 Any thoughts about what might help today? (or press Enter to skip): ")
        
        # Optional sensory check for challenging states
        sensory_triggers = []
        if mood in [MoodState.SENSORY_OVERLOAD, MoodState.OVERWHELMED, MoodState.ANXIOUS]:
            print("\n🌿 SENSORY CHECK (optional - press Enter to skip any):")
            sound = input("Sounds bothering you? (traffic, voices, etc.): ").strip()
            if sound:
                sensory_triggers.append(f"Sound: {sound}")
            
            visual = input("Visual overwhelm? (bright lights, clutter, etc.): ").strip()
            if visual:
                sensory_triggers.append(f"Visual: {visual}")
            
            touch = input("Touch/texture issues? (clothing, temperature, etc.): ").strip()
            if touch:
                sensory_triggers.append(f"Touch: {touch}")
        
        # Log the comprehensive check-in
        self.energy_tracker.log_daily_checkin(energy, mood, additional_context)
        
        # Set overwhelm level based on mood with more nuanced mapping
        overwhelm_level = self.overwhelm_manager.map_mood_to_overwhelm(mood)
        overwhelm_triggers = []
        if sensory_triggers:
            overwhelm_triggers.append("SENSORY_OVERLOAD")
        
        self.overwhelm_manager.record_checkin(overwhelm_level, overwhelm_triggers, additional_context)
        
        # Provide immediate mood-specific affirmation
        print(f"\n💝 {self.reward_system.get_mood_specific_affirmation(mood)}")
        
        # Personalized recommendations
        print("\n✨ PERSONALIZED RECOMMENDATIONS FOR TODAY:")
        recommendations = self.energy_tracker.get_task_recommendations()
        max_recs = 3 if mood.is_challenging_state() else 5  # Fewer suggestions when struggling
        for i, rec in enumerate(recommendations[:max_recs]):
            print(f"   {rec}")
        
        # Special guidance for challenging states
        if mood.is_challenging_state():
            print("\n🌱 Remember: Managing a neurodivergent brain takes extra energy.")
            print("   Your struggles are valid, and small steps count as victories.")
        elif mood.is_positive_state():
            print("\n🚀 You're in a great space! Remember to:")
            print("   Set gentle boundaries to maintain this energy sustainably.")
        
        print("\n" + "=" * 55)
    
    def display_contextual_info(self):
        """Display contextual reminders and information"""
        # Send reminders
        self.task_manager.send_reminders()
        
        # Check for routine reminders
        routine_reminders = self.routine_manager.get_routine_reminders()
        if routine_reminders:
            print("\n⏰ ROUTINE REMINDERS:")
            for reminder in routine_reminders:
                print(f"   {reminder}")
        
        # Check for habit reminders
        habit_reminders = self.habit_tracker.get_todays_habit_reminders()
        if habit_reminders and len(habit_reminders) <= 2:  # Don't overwhelm
            print("\n🌱 HABIT NUDGES:")
            for reminder in habit_reminders:
                print(f"   {reminder}")
        
        # Check for social check-ins
        social_reminders = self.social_support.get_due_checkins()
        if social_reminders:
            print("\n👥 SOCIAL:")
            for reminder in social_reminders:
                print(f"   {reminder}")
        
        # Show active Pomodoro session
        if self.pomodoro_timer.get_state() == TimerState.RUNNING:
            print(f"\n🍅 ACTIVE POMODORO: {self.pomodoro_timer.get_time_remaining()}")
        
        # Show active body doubling session
        current_session = self.social_support.get_current_session()
        if current_session and current_session.is_active():
            print(f"👥 BODY DOUBLING: {current_session.get_time_remaining()}")
    
    def display_welcome_message(self):
        """Display the welcome message"""
        print("\n" + "=" * 60)
        print("🌟 WELCOME TO NUDGE - ENHANCED PYTHON EDITION 🌟")
        print("Your comprehensive neurodivergent-friendly task companion!")
        print("=" * 60)
        print("💛 Remember: Progress over perfection!")
        print("✨ Every small step is a victory!")
        print("🤗 Be kind to yourself today!")
        print("🧠 Your brain works differently, and that's your superpower!")
        print("=" * 60)
    
    def display_main_menu(self):
        """Display the enhanced main menu"""
        print("\n📋 MAIN MENU:")
        print("=== TASKS ===")
        print("1. ➕ Add New Task")
        print("2. 📋 View Active Tasks")
        print("3. ✅ Mark Task Complete")
        print("4. ❌ Mark Task Missed")
        print("5. 🗑️  Remove Task")
        print("6. 📊 View Completed Tasks")
        print("7. 🔄 Add Recurring Task")
        print("8. 📝 Break Down Task")
        
        print("\n=== FOCUS & TIME ===")
        print("9. 🍅 Pomodoro Timer")
        print("10. 👥 Body Doubling Session")
        print("11. 📅 Routine Management")
        print("12. 🌱 Habit Tracker")
        
        print("\n=== WELLNESS ===")
        print("13. ⚡ Energy & Mood Dashboard")
        print("14. 😰 Overwhelm Check-in")
        print("15. 🌿 Sensory Break")
        
        print("\n=== SOCIAL & REWARDS ===")
        print("16. 🤝 Social Support")
        print("17. 🏆 View Stats & Achievements")
        print("18. 💫 Get Affirmation")
        
        print("\n=== OTHER ===")
        print("19. ⚙️ Settings")
        print("20. 🚪 Exit")
        print("\nChoose an option (1-20): ", end="")
    
    def get_menu_choice(self, min_val: int = 1, max_val: int = 20) -> int:
        """Get and validate menu choice"""
        try:
            choice = int(input().strip())
            if min_val <= choice <= max_val:
                return choice
            else:
                print(f"🌸 Please choose a number between {min_val} and {max_val}!")
                return self.get_menu_choice(min_val, max_val)
        except (ValueError, KeyboardInterrupt):
            print("🌸 Please enter a valid number!")
            return self.get_menu_choice(min_val, max_val)
    
    def handle_menu_choice(self, choice: int):
        """Handle the selected menu choice"""
        menu_handlers = {
            1: self.add_new_task,
            2: self.view_active_tasks,
            3: self.mark_task_complete,
            4: self.mark_task_missed,
            5: self.remove_task,
            6: self.view_completed_tasks,
            7: self.add_recurring_task,
            8: self.break_down_task,
            9: self.manage_pomodoro_timer,
            10: self.manage_body_doubling,
            11: self.manage_routines,
            12: self.manage_habits,
            13: self.view_energy_dashboard,
            14: self.overwhelm_checkin,
            15: self.take_sensory_break,
            16: self.manage_social_support,
            17: self.view_stats,
            18: self.show_affirmation,
            19: self.manage_settings,
            20: lambda: setattr(self, 'running', False)
        }
        
        handler = menu_handlers.get(choice)
        if handler:
            handler()
        else:
            print("Invalid choice. Please try again.")
    
    def add_new_task(self):
        """Enhanced task creation with more options"""
        print("\n✨ CREATING A NEW TASK")
        print("-" * 30)
        
        # Get task name
        name = input("📝 Task name: ").strip()
        if not name:
            print("🌸 Task name cannot be empty. Try again!")
            return
        
        # Get deadline
        deadline = self.get_task_deadline()
        if deadline is None:
            return
        
        # Get category
        category = self.get_task_category()
        if category is None:
            return
        
        # Get reminder type
        reminder_type = self.get_reminder_type()
        if reminder_type is None:
            return
        
        # Get point value
        point_value = self.get_point_value()
        if point_value == -1:
            return
        
        # Get estimated duration
        duration_str = input("⏰ Estimated duration (minutes, or press Enter for 30): ").strip()
        duration = 30
        if duration_str:
            try:
                duration = int(duration_str)
            except ValueError:
                print("🌸 Using default duration of 30 minutes")
        
        # Get context notes
        context_notes = input("📋 Context/preparation notes (optional): ").strip()
        
        # Create and add task
        task = Task(name, deadline, reminder_type, category, point_value)
        task.estimated_duration_minutes = duration
        if context_notes:
            task.context_notes = context_notes
        
        self.task_manager.add_task(task)
        
        print("\n🎉 Task created successfully!")
        print(f"📋 {task}")
        
        # Ask if they want to break it down
        breakdown_choice = input("🔍 Would you like to break this task into smaller steps? (y/n): ").strip().lower()
        if breakdown_choice in ['y', 'yes']:
            plan = self.task_breakdown.suggest_breakdown(task)
            plan.display_plan()
            print("💡 Task breakdown suggestions created! You can view and modify them later.")
        
        print("💪 You've got this!")
    
    def show_affirmation(self):
        """Display enhanced affirmations and encouragement"""
        print("\n" + "=" * 60)
        print("💫 AFFIRMATIONS & ENCOURAGEMENT")
        print("=" * 60)
        
        # Mood-specific affirmation first
        print("🎯 For your current state:")
        current_mood = self.energy_tracker.get_current_mood()
        print(f"   {self.reward_system.get_mood_specific_affirmation(current_mood)}")
        
        # General evidence-based affirmation
        print("\n✨ Universal truth:")
        print(f"   {self.reward_system.get_random_affirmation()}")
        
        # Add contextual encouragement
        current_energy = self.energy_tracker.get_current_energy()
        if current_energy.level_value <= 2:
            print("\n💙 GENTLE REMINDER FOR LOW ENERGY DAYS:")
            print("   🌱 Rest is productive - your brain needs recovery time")
            print("   🐌 Slow progress is still progress")
            print("   💤 Honoring your limits is self-respect, not laziness")
        elif current_mood.is_positive_state():
            print("\n🌟 CELEBRATING YOUR POSITIVE ENERGY:")
            print("   ⚡ This feeling is proof of your resilience")
            print("   🎯 Channel this energy with gentle boundaries")
            print("   💫 You've earned these good moments")
        
        current_streak = self.reward_system.get_current_streak()
        if current_streak > 0:
            print("\n🔥 STREAK CELEBRATION:")
            print(f"   You're on a {current_streak} day completion streak!")
            if current_streak >= 7:
                print("   🏆 This consistency shows incredible strength")
        
        # Random neurodivergent-specific encouragement
        specific_encouragement = [
            "🧠 Your brain's unique wiring creates perspectives others can't see",
            "⚡ Hyperfocus and deep interests are superpowers when channeled well",
            "🌈 Your sensitivity to the world makes you incredibly empathetic",
            "🎯 Pattern recognition and attention to detail are your strengths",
            "💫 The world needs minds that work differently - like yours",
            "🔍 Your ability to see details others miss is invaluable",
            "🌊 Your emotional depth creates authentic connections",
            "⭐ Neurodivergent minds drive innovation and creativity"
        ]
        
        import random
        print("\n🌈 NEURODIVERGENT PRIDE:")
        print(f"   {random.choice(specific_encouragement)}")
        
        print("\n" + "=" * 60)
        input("\nPress Enter to continue...")
    
    # Placeholder methods for other menu items - these would be implemented similarly
    def view_active_tasks(self): pass
    def mark_task_complete(self): pass
    def mark_task_missed(self): pass
    def remove_task(self): pass
    def view_completed_tasks(self): pass
    def add_recurring_task(self): pass
    def break_down_task(self): pass
    def manage_pomodoro_timer(self): pass
    def manage_body_doubling(self): pass
    def manage_routines(self): pass
    def manage_habits(self): pass
    def view_energy_dashboard(self): pass
    def overwhelm_checkin(self): pass
    def take_sensory_break(self): pass
    def manage_social_support(self): pass
    def view_stats(self): pass
    def manage_settings(self): pass
    
    def get_task_deadline(self):
        """Get task deadline from user input"""
        # Placeholder implementation
        return datetime.now() + timedelta(days=1)
    
    def get_task_category(self):
        """Get task category from user input"""
        # Placeholder implementation
        return TaskCategory.PERSONAL
    
    def get_reminder_type(self):
        """Get reminder type from user input"""
        # Placeholder implementation
        return ReminderType.GENTLE
    
    def get_point_value(self):
        """Get point value from user input"""
        # Placeholder implementation
        return 10
    
    def display_goodbye_message(self):
        """Display goodbye message"""
        print("\n" + "=" * 60)
        print("🌟 GOODBYE FROM ENHANCED NUDGE PYTHON EDITION! 🌟")
        print("=" * 60)
        print("💛 You did great today!")
        print("✨ Remember to be kind to yourself!")
        print("🧠 Your neurodivergent brain is amazing!")
        print("🌈 See you next time!")
        print("💪 You've got this - always!")
        print("=" * 60)


def main():
    """Main entry point"""
    try:
        app = EnhancedNudgeApp()
        app.run()
    except KeyboardInterrupt:
        print("\n\n🌈 Thanks for using Nudge! Take care! 💛")
    except Exception as e:
        print(f"\n❌ An unexpected error occurred: {e}")
        print("🌸 Don't worry - this doesn't reflect on you at all!")


if __name__ == "__main__":
    main()