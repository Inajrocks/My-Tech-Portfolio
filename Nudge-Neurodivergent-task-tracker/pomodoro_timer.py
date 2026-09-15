"""
Pomodoro timer for focus sessions
"""

from datetime import datetime, timedelta
from enum import Enum
from typing import Optional
from dataclasses import dataclass


class TimerState(Enum):
    """Timer states"""
    IDLE = "idle"
    RUNNING = "running"
    PAUSED = "paused"
    BREAK = "break"


class SessionType(Enum):
    """Types of Pomodoro sessions"""
    WORK = "Work"
    SHORT_BREAK = "Short Break"
    LONG_BREAK = "Long Break"


@dataclass
class PomodoroSession:
    """A completed Pomodoro session"""
    start_time: datetime
    end_time: datetime
    session_type: SessionType
    task_name: Optional[str] = None
    completed: bool = True


class PomodoroTimer:
    """Pomodoro timer with customizable intervals"""
    
    def __init__(self):
        self.work_duration = 25  # minutes
        self.short_break_duration = 5  # minutes
        self.long_break_duration = 15  # minutes
        self.sessions_until_long_break = 4
        
        self.current_session_start: Optional[datetime] = None
        self.current_session_type = SessionType.WORK
        self.current_task_name: Optional[str] = None
        self.state = TimerState.IDLE
        self.paused_time: Optional[datetime] = None
        self.elapsed_when_paused = timedelta()
        
        self.completed_sessions: list[PomodoroSession] = []
        self.work_sessions_today = 0
    
    def start_session(self, task_name: Optional[str] = None):
        """Start a new Pomodoro session"""
        if self.state != TimerState.IDLE:
            print("🍅 Session already in progress!")
            return
        
        self.current_session_start = datetime.now()
        self.current_task_name = task_name
        self.state = TimerState.RUNNING
        self.elapsed_when_paused = timedelta()
        
        duration = self._get_current_duration()
        session_name = self.current_session_type.value
        
        if task_name:
            print(f"🍅 Started {session_name} session for: {task_name}")
        else:
            print(f"🍅 Started {session_name} session")
        
        print(f"⏰ Duration: {duration} minutes")
        print("💡 Use menu options to pause/complete/skip when ready")
    
    def pause_session(self):
        """Pause the current session"""
        if self.state != TimerState.RUNNING:
            print("🌸 No active session to pause")
            return
        
        self.paused_time = datetime.now()
        elapsed = self.paused_time - self.current_session_start
        self.elapsed_when_paused = elapsed
        self.state = TimerState.PAUSED
        print("⏸️ Session paused")
    
    def resume_session(self):
        """Resume a paused session"""
        if self.state != TimerState.PAUSED:
            print("🌸 No paused session to resume")
            return
        
        # Adjust start time to account for pause duration
        pause_duration = datetime.now() - self.paused_time
        self.current_session_start += pause_duration
        self.state = TimerState.RUNNING
        print("▶️ Session resumed")
    
    def complete_session(self):
        """Complete the current session"""
        if self.state not in [TimerState.RUNNING, TimerState.PAUSED]:
            print("🌸 No active session to complete")
            return
        
        end_time = datetime.now()
        session = PomodoroSession(
            start_time=self.current_session_start,
            end_time=end_time,
            session_type=self.current_session_type,
            task_name=self.current_task_name,
            completed=True
        )
        
        self.completed_sessions.append(session)
        
        if self.current_session_type == SessionType.WORK:
            self.work_sessions_today += 1
            print(f"✅ Work session completed! That's {self.work_sessions_today} today!")
            
            # Suggest next session type
            if self.work_sessions_today % self.sessions_until_long_break == 0:
                self.current_session_type = SessionType.LONG_BREAK
                print(f"🎉 Time for a {self.long_break_duration}-minute long break!")
            else:
                self.current_session_type = SessionType.SHORT_BREAK
                print(f"☕ Time for a {self.short_break_duration}-minute short break!")
        else:
            print("✅ Break completed! Ready for the next work session?")
            self.current_session_type = SessionType.WORK
        
        self._reset_session()
    
    def skip_session(self):
        """Skip the current session"""
        if self.state not in [TimerState.RUNNING, TimerState.PAUSED]:
            print("🌸 No active session to skip")
            return
        
        print("⏭️ Session skipped - that's totally okay!")
        self._reset_session()
    
    def _reset_session(self):
        """Reset session state"""
        self.current_session_start = None
        self.current_task_name = None
        self.state = TimerState.IDLE
        self.paused_time = None
        self.elapsed_when_paused = timedelta()
    
    def _get_current_duration(self) -> int:
        """Get duration for current session type"""
        if self.current_session_type == SessionType.WORK:
            return self.work_duration
        elif self.current_session_type == SessionType.SHORT_BREAK:
            return self.short_break_duration
        else:
            return self.long_break_duration
    
    def get_time_remaining(self) -> str:
        """Get remaining time in current session"""
        if self.state == TimerState.IDLE:
            return "No active session"
        
        if self.state == TimerState.PAUSED:
            elapsed = self.elapsed_when_paused
        else:
            elapsed = datetime.now() - self.current_session_start
        
        total_duration = timedelta(minutes=self._get_current_duration())
        remaining = total_duration - elapsed
        
        if remaining.total_seconds() <= 0:
            return "Time's up! ⏰"
        
        minutes = int(remaining.total_seconds() // 60)
        seconds = int(remaining.total_seconds() % 60)
        return f"{minutes:02d}:{seconds:02d}"
    
    def customize_settings(self, work_minutes: int, short_break_minutes: int, 
                          long_break_minutes: int, sessions_for_long: int):
        """Customize timer settings"""
        self.work_duration = work_minutes
        self.short_break_duration = short_break_minutes
        self.long_break_duration = long_break_minutes
        self.sessions_until_long_break = sessions_for_long
        
        print("⚙️ Pomodoro settings updated!")
        print(f"   Work: {work_minutes}min")
        print(f"   Short break: {short_break_minutes}min")
        print(f"   Long break: {long_break_minutes}min")
        print(f"   Sessions until long break: {sessions_for_long}")
    
    def display_stats(self):
        """Display Pomodoro statistics"""
        print("\n🍅 POMODORO STATS")
        print("-" * 30)
        print(f"📊 Total sessions: {len(self.completed_sessions)}")
        print(f"💼 Work sessions today: {self.work_sessions_today}")
        
        total_work_time = sum(
            (session.end_time - session.start_time).total_seconds() / 60
            for session in self.completed_sessions
            if session.session_type == SessionType.WORK
        )
        print(f"⏱️ Total focus time: {total_work_time:.0f} minutes ({total_work_time/60:.1f} hours)")
        
        if self.state != TimerState.IDLE:
            print(f"🔄 Current: {self.current_session_type.value} - {self.get_time_remaining()}")
    
    def get_state(self) -> TimerState:
        return self.state
    
    def get_completed_sessions(self) -> int:
        return len([s for s in self.completed_sessions if s.session_type == SessionType.WORK])
    
    def get_total_focus_time(self) -> float:
        """Get total focus time in minutes"""
        return sum(
            (session.end_time - session.start_time).total_seconds() / 60
            for session in self.completed_sessions
            if session.session_type == SessionType.WORK
        )