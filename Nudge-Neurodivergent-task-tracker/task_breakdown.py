"""
Task breakdown and executive function support
"""

from datetime import datetime
from enum import Enum
from typing import List, Optional, Dict
from dataclasses import dataclass
import random
from energy_tracker import EnergyLevel
from task_manager import Task


class Difficulty(Enum):
    """Task difficulty levels"""
    EASY = ("🟢 Easy", "Low mental load")
    MODERATE = ("🟡 Moderate", "Medium effort required")
    HARD = ("🔴 Hard", "High concentration needed")
    
    def __init__(self, display: str, description: str):
        self.display = display
        self.description = description


class Priority(Enum):
    """Priority levels using Eisenhower matrix"""
    URGENT_IMPORTANT = ("🔥 Do First", "Urgent & Important")
    NOT_URGENT_IMPORTANT = ("📅 Schedule", "Important, Not Urgent")
    URGENT_NOT_IMPORTANT = ("🤝 Delegate", "Urgent, Not Important")
    NOT_URGENT_NOT_IMPORTANT = ("🗑️ Eliminate", "Neither Urgent nor Important")
    
    def __init__(self, action: str, description: str):
        self.action = action
        self.description = description


@dataclass
class Subtask:
    """A subtask within a larger task breakdown"""
    description: str
    difficulty: Difficulty
    estimated_minutes: int
    context_notes: str
    completed: bool = False
    completed_at: Optional[datetime] = None
    
    def mark_completed(self):
        """Mark subtask as completed"""
        self.completed = True
        self.completed_at = datetime.now()


@dataclass
class TaskPlan:
    """A complete task breakdown plan"""
    original_task: Task
    subtasks: List[Subtask]
    priority: Priority
    context_switching_notes: str
    created_at: datetime
    
    def display_plan(self):
        """Display the task breakdown plan"""
        print(f"\n📝 TASK BREAKDOWN: {self.original_task.name}")
        print("=" * 50)
        print(f"🎯 Priority: {self.priority.action} - {self.priority.description}")
        print(f"⏱️ Total estimated time: {sum(s.estimated_minutes for s in self.subtasks)} minutes")
        print(f"📋 Progress: {self.get_completion_percentage():.0f}% complete")
        
        if self.context_switching_notes:
            print(f"\n🧠 Context Notes: {self.context_switching_notes}")
        
        print("\n📋 SUBTASKS:")
        for i, subtask in enumerate(self.subtasks, 1):
            status = "✅" if subtask.completed else "⚪"
            print(f"  {status} {i}. {subtask.description}")
            print(f"      {subtask.difficulty.display} | {subtask.estimated_minutes}min")
            if subtask.context_notes:
                print(f"      💡 {subtask.context_notes}")
    
    def get_completion_percentage(self) -> float:
        """Get completion percentage"""
        if not self.subtasks:
            return 0
        completed = sum(1 for s in self.subtasks if s.completed)
        return (completed / len(self.subtasks)) * 100
    
    def is_fully_completed(self) -> bool:
        """Check if all subtasks are completed"""
        return all(s.completed for s in self.subtasks)
    
    def complete_subtask(self, index: int) -> bool:
        """Complete a subtask by index"""
        if 0 <= index < len(self.subtasks):
            self.subtasks[index].mark_completed()
            return True
        return False
    
    def add_subtask(self, description: str, difficulty: Difficulty, 
                   estimated_minutes: int, context_notes: str = ""):
        """Add a new subtask"""
        subtask = Subtask(description, difficulty, estimated_minutes, context_notes)
        self.subtasks.append(subtask)


class TaskBreakdown:
    """Provides AI-powered task breakdown and executive function support"""
    
    def __init__(self):
        self.task_plans: Dict[str, TaskPlan] = {}  # task_id -> TaskPlan
    
    def suggest_breakdown(self, task: Task) -> TaskPlan:
        """Create an AI-powered task breakdown"""
        # Generate breakdown based on task type and complexity
        subtasks = self._generate_subtasks(task)
        priority = self._determine_priority(task)
        context_notes = self._generate_context_notes(task)
        
        plan = TaskPlan(
            original_task=task,
            subtasks=subtasks,
            priority=priority,
            context_switching_notes=context_notes,
            created_at=datetime.now()
        )
        
        self.task_plans[task.task_id] = plan
        return plan
    
    def _generate_subtasks(self, task: Task) -> List[Subtask]:
        """Generate subtasks based on task characteristics"""
        task_name = task.name.lower()
        subtasks = []
        
        # General task breakdown patterns
        if any(word in task_name for word in ['email', 'message', 'contact', 'call']):
            subtasks = [
                Subtask("Gather contact information", Difficulty.EASY, 3, "Check phone/email"),
                Subtask("Draft message outline", Difficulty.MODERATE, 10, "Key points to cover"),
                Subtask("Write/make contact", Difficulty.MODERATE, 15, "Be clear and direct"),
                Subtask("Follow up if needed", Difficulty.EASY, 5, "Set reminder for response")
            ]
        elif any(word in task_name for word in ['clean', 'organize', 'tidy']):
            subtasks = [
                Subtask("Gather supplies", Difficulty.EASY, 5, "Cleaning materials, storage"),
                Subtask("Set up music/podcast", Difficulty.EASY, 2, "Make it enjoyable"),
                Subtask("Start with smallest area", Difficulty.MODERATE, 15, "Build momentum"),
                Subtask("Sort and decide", Difficulty.HARD, 20, "Keep/donate/trash decisions"),
                Subtask("Put everything away", Difficulty.MODERATE, 10, "Everything has a place")
            ]
        elif any(word in task_name for word in ['research', 'study', 'learn']):
            subtasks = [
                Subtask("Define specific questions", Difficulty.MODERATE, 10, "What exactly do you need?"),
                Subtask("Find reliable sources", Difficulty.MODERATE, 15, "Quality over quantity"),
                Subtask("Take notes actively", Difficulty.HARD, 30, "Summarize key points"),
                Subtask("Organize findings", Difficulty.MODERATE, 10, "Make it usable later")
            ]
        elif any(word in task_name for word in ['write', 'document', 'report']):
            subtasks = [
                Subtask("Brain dump ideas", Difficulty.EASY, 10, "Don't edit, just write"),
                Subtask("Create outline", Difficulty.MODERATE, 15, "Structure your thoughts"),
                Subtask("Write first draft", Difficulty.HARD, 45, "Progress over perfection"),
                Subtask("Review and edit", Difficulty.MODERATE, 20, "Read aloud for flow"),
                Subtask("Final proofread", Difficulty.EASY, 10, "Fresh eyes if possible")
            ]
        else:
            # Generic breakdown
            estimated_total = task.estimated_duration_minutes
            subtasks = [
                Subtask("Prepare and gather materials", Difficulty.EASY, 
                       max(5, estimated_total // 6), "Set up workspace"),
                Subtask("Start the main work", Difficulty.MODERATE,
                       max(15, estimated_total // 2), "Focus on core task"),
                Subtask("Complete and wrap up", Difficulty.MODERATE,
                       max(10, estimated_total // 3), "Finish and clean up")
            ]
        
        return subtasks
    
    def _determine_priority(self, task: Task) -> Priority:
        """Determine task priority using Eisenhower matrix"""
        # Simple heuristic based on deadline and category
        is_urgent = task.is_due_soon(hours=48)  # Due within 2 days
        
        # Important categories
        important_categories = [
            'MEDICATION', 'HEALTH', 'WORK'
        ]
        is_important = task.category.name in important_categories
        
        if is_urgent and is_important:
            return Priority.URGENT_IMPORTANT
        elif not is_urgent and is_important:
            return Priority.NOT_URGENT_IMPORTANT
        elif is_urgent and not is_important:
            return Priority.URGENT_NOT_IMPORTANT
        else:
            return Priority.NOT_URGENT_NOT_IMPORTANT
    
    def _generate_context_notes(self, task: Task) -> str:
        """Generate context switching and preparation notes"""
        notes = []
        
        # Time-based notes
        if task.estimated_duration_minutes > 60:
            notes.append("🍅 Consider using Pomodoro technique for focus")
        
        # Category-specific notes
        category_notes = {
            'WORK': "📧 Close personal tabs, use work environment",
            'CREATIVE': "🎨 Gather inspiration, prepare creative space",
            'SOCIAL': "👥 Check your social energy levels first",
            'CHORES': "🎵 Put on energizing music or podcast",
            'HEALTH': "🏥 Gather any needed documents or information"
        }
        
        if task.category.name in category_notes:
            notes.append(category_notes[task.category.name])
        
        # General preparation
        notes.extend([
            "📱 Put phone on focus mode",
            "💧 Have water nearby",
            "⏰ Set realistic time expectations"
        ])
        
        return " | ".join(notes)
    
    def get_executive_function_tips(self, energy_level: EnergyLevel) -> List[str]:
        """Get executive function tips based on current energy"""
        base_tips = [
            "🧠 Break tasks into 2-minute micro-steps",
            "⏰ Use external timers for time awareness",
            "📝 Write down every step (external memory)",
            "🔄 Change environment when stuck",
            "🎯 Do the smallest possible next action"
        ]
        
        if energy_level.level_value <= 2:  # Low energy
            base_tips.extend([
                "🌸 Pick the tiniest possible next step",
                "📱 Use voice memos instead of writing",
                "🎵 Try body doubling or background sounds",
                "🧠 Remember: your brain works differently, and that's okay",
                "💪 Progress over perfection, always"
            ])
        elif energy_level.level_value >= 4:  # High energy
            base_tips.extend([
                "⚡ Batch similar tasks together",
                "🚀 Tackle avoided or complex tasks",
                "📋 Use this energy for planning sessions",
                "⚠️ Set boundaries to prevent burnout"
            ])
        
        return base_tips
    
    def find_task_plan(self, task: Task) -> Optional[TaskPlan]:
        """Find existing task plan"""
        return self.task_plans.get(task.task_id)
    
    def remove_task_plan(self, task: Task):
        """Remove task plan when task is deleted"""
        if task.task_id in self.task_plans:
            del self.task_plans[task.task_id]
    
    def get_all_active_plans(self) -> List[TaskPlan]:
        """Get all active task plans"""
        return list(self.task_plans.values())