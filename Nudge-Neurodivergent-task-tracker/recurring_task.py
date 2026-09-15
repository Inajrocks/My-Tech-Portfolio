"""
Recurring task functionality
"""

from datetime import datetime, timedelta, date
from enum import Enum
from typing import List, Set
from task_manager import Task, TaskCategory, ReminderType


class RecurrenceType(Enum):
    """Types of task recurrence"""
    DAILY = "Daily"
    WEEKLY = "Weekly" 
    WEEKDAYS_ONLY = "Weekdays Only"
    CUSTOM = "Custom Days"
    
    def __init__(self, display_name: str):
        self.display_name = display_name


class RecurringTask(Task):
    """Task that repeats on a schedule"""
    
    def __init__(self, name: str, first_due: datetime, reminder_type: ReminderType, 
                 category: TaskCategory, point_value: int, recurrence_type: RecurrenceType):
        super().__init__(name, first_due, reminder_type, category, point_value)
        self.recurrence_type = recurrence_type
        self.custom_days: Set[int] = set()  # Days of week (0=Monday, 6=Sunday)
        self.completion_count = 0
        
    def add_custom_day(self, day: int):
        """Add a custom day (0=Monday, 6=Sunday)"""
        if 0 <= day <= 6:
            self.custom_days.add(day)
    
    def schedule_next(self):
        """Schedule the next occurrence of this task"""
        if not self.completed:
            return
            
        self.completion_count += 1
        
        # Calculate next due date
        current_due = self.deadline
        next_due = None
        
        if self.recurrence_type == RecurrenceType.DAILY:
            next_due = current_due + timedelta(days=1)
        elif self.recurrence_type == RecurrenceType.WEEKLY:
            next_due = current_due + timedelta(weeks=1)
        elif self.recurrence_type == RecurrenceType.WEEKDAYS_ONLY:
            next_due = self._find_next_weekday(current_due)
        elif self.recurrence_type == RecurrenceType.CUSTOM:
            next_due = self._find_next_custom_day(current_due)
        
        if next_due:
            # Reset completion status for next occurrence
            self.completed = False
            self.completed_at = None
            self.deadline = next_due
    
    def _find_next_weekday(self, current: datetime) -> datetime:
        """Find next weekday (Monday-Friday)"""
        next_day = current + timedelta(days=1)
        while next_day.weekday() > 4:  # Skip weekends
            next_day += timedelta(days=1)
        return next_day
    
    def _find_next_custom_day(self, current: datetime) -> datetime:
        """Find next custom day"""
        if not self.custom_days:
            return current + timedelta(days=1)
        
        next_day = current + timedelta(days=1)
        for _ in range(7):  # Check up to a week ahead
            if next_day.weekday() in self.custom_days:
                return next_day
            next_day += timedelta(days=1)
        
        # If no custom day found in next week, return next occurrence of first custom day
        first_custom_day = min(self.custom_days)
        days_until = (first_custom_day - next_day.weekday()) % 7
        if days_until == 0:
            days_until = 7
        return next_day + timedelta(days=days_until)
    
    def get_next_due(self) -> datetime:
        """Get the next due date without scheduling"""
        if self.recurrence_type == RecurrenceType.DAILY:
            return self.deadline + timedelta(days=1)
        elif self.recurrence_type == RecurrenceType.WEEKLY:
            return self.deadline + timedelta(weeks=1)
        elif self.recurrence_type == RecurrenceType.WEEKDAYS_ONLY:
            return self._find_next_weekday(self.deadline)
        elif self.recurrence_type == RecurrenceType.CUSTOM:
            return self._find_next_custom_day(self.deadline)
        else:
            return self.deadline + timedelta(days=1)
    
    def __str__(self) -> str:
        """String representation including recurrence info"""
        base_str = super().__str__()
        return f"{base_str} | 🔄 {self.recurrence_type.display_name} (×{self.completion_count})"