"""
Core task management system
"""

from datetime import datetime, timedelta
from enum import Enum
from typing import List, Optional
from dataclasses import dataclass, field
import uuid


class TaskCategory(Enum):
    """Task categories for organization"""
    MEDICATION = "💊 Medication"
    SELF_CARE = "🛁 Self-care"
    CHORES = "🧹 Chores"
    WORK = "💼 Work"
    PERSONAL = "🌱 Personal"
    SOCIAL = "👥 Social"
    HEALTH = "🏥 Health"
    CREATIVE = "🎨 Creative"
    LEARNING = "📚 Learning"
    
    def __init__(self, display_name: str):
        self.display_name = display_name


class ReminderType(Enum):
    """Different reminder styles"""
    TEXT = ("📝 Text", "Remember to")
    EMOJI = ("🎉 Emoji", "✨ Time for")
    VIBEY = ("🌟 Vibey", "Yo! Don't forget about")
    GENTLE = ("💝 Gentle", "Gentle reminder about")
    
    def __init__(self, display_name: str, prefix: str):
        self.display_name = display_name
        self.prefix = prefix


@dataclass
class Task:
    """Represents an individual task"""
    name: str
    deadline: datetime
    reminder_type: ReminderType
    category: TaskCategory
    point_value: int
    task_id: str = field(default_factory=lambda: str(uuid.uuid4())[:8])
    completed: bool = False
    completed_at: Optional[datetime] = None
    estimated_duration_minutes: int = 30
    context_notes: str = ""
    
    def mark_completed(self):
        """Mark the task as completed"""
        self.completed = True
        self.completed_at = datetime.now()
    
    def is_overdue(self) -> bool:
        """Check if task is overdue"""
        return not self.completed and datetime.now() > self.deadline
    
    def is_due_soon(self, hours: int = 24) -> bool:
        """Check if task is due within specified hours"""
        return not self.completed and datetime.now() + timedelta(hours=hours) >= self.deadline
    
    def get_reminder_message(self) -> str:
        """Generate reminder message based on reminder type"""
        return f"{self.reminder_type.prefix} {self.name}!"
    
    def get_status_icon(self) -> str:
        """Get status icon for display"""
        if self.completed:
            return "✅"
        elif self.is_overdue():
            return "🔴"
        elif self.is_due_soon():
            return "🟡"
        else:
            return "⚪"
    
    def __str__(self) -> str:
        """String representation of the task"""
        status = self.get_status_icon()
        deadline_str = self.deadline.strftime("%Y-%m-%d %H:%M")
        return f"{status} [{self.task_id}] {self.name} | {self.category.display_name} | Due: {deadline_str} | {self.point_value}pts"


class TaskManager:
    """Manages task storage and operations"""
    
    def __init__(self):
        self.tasks: List[Task] = []
    
    def add_task(self, task: Task):
        """Add a new task"""
        self.tasks.append(task)
    
    def remove_task(self, task_id: str) -> bool:
        """Remove a task by ID"""
        for i, task in enumerate(self.tasks):
            if task.task_id == task_id:
                self.tasks.pop(i)
                return True
        return False
    
    def find_task_by_id(self, task_id: str) -> Optional[Task]:
        """Find a task by its ID"""
        for task in self.tasks:
            if task.task_id == task_id:
                return task
        return None
    
    def get_active_tasks(self) -> List[Task]:
        """Get all active (incomplete) tasks"""
        return [task for task in self.tasks if not task.completed]
    
    def get_completed_tasks(self) -> List[Task]:
        """Get all completed tasks"""
        return [task for task in self.tasks if task.completed]
    
    def get_overdue_tasks(self) -> List[Task]:
        """Get all overdue tasks"""
        return [task for task in self.tasks if task.is_overdue()]
    
    def get_due_soon_tasks(self, hours: int = 24) -> List[Task]:
        """Get tasks due soon"""
        return [task for task in self.tasks if task.is_due_soon(hours)]
    
    def send_reminders(self):
        """Send reminders for due tasks"""
        due_soon = self.get_due_soon_tasks()
        overdue = self.get_overdue_tasks()
        
        if overdue:
            print("\n🔴 OVERDUE TASKS:")
            for task in overdue:
                print(f"   {task.get_reminder_message()}")
        
        if due_soon:
            print("\n🟡 DUE SOON:")
            for task in due_soon:
                if task not in overdue:  # Don't double-show overdue tasks
                    print(f"   {task.get_reminder_message()}")
    
    def display_active_tasks(self):
        """Display all active tasks"""
        active_tasks = self.get_active_tasks()
        
        if not active_tasks:
            print("🎉 No active tasks! You're all caught up!")
            return
        
        print("\n📋 ACTIVE TASKS:")
        print("-" * 50)
        for task in sorted(active_tasks, key=lambda t: t.deadline):
            print(f"   {task}")
    
    def display_completed_tasks(self):
        """Display completed tasks"""
        completed_tasks = self.get_completed_tasks()
        
        if not completed_tasks:
            print("📋 No completed tasks yet. You've got this!")
            return
        
        print("\n✅ COMPLETED TASKS:")
        print("-" * 50)
        for task in sorted(completed_tasks, key=lambda t: t.completed_at or datetime.min, reverse=True):
            completed_time = task.completed_at.strftime("%Y-%m-%d %H:%M") if task.completed_at else "Unknown"
            print(f"   ✅ {task.name} | Completed: {completed_time} | {task.point_value}pts")
    
    def get_all_tasks(self) -> List[Task]:
        """Get all tasks"""
        return self.tasks.copy()
    
    def get_tasks_by_category(self, category: TaskCategory) -> List[Task]:
        """Get tasks by category"""
        return [task for task in self.tasks if task.category == category]