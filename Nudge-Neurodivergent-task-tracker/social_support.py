"""
Social support features for accountability and body doubling
"""

from datetime import datetime, timedelta
from enum import Enum
from typing import List, Optional, Dict
from dataclasses import dataclass


class SessionType(Enum):
    """Types of social support sessions"""
    BODY_DOUBLING = "Body Doubling"
    ACCOUNTABILITY_CHECKIN = "Accountability Check-in"
    CELEBRATION = "Achievement Celebration"
    SUPPORT_CALL = "Support Call"


@dataclass
class BodyDoublingSession:
    """A body doubling session for parallel work"""
    name: str
    start_time: datetime
    duration_minutes: int
    task_focus: Optional[str] = None
    session_id: str = None
    ended: bool = False
    
    def __post_init__(self):
        if self.session_id is None:
            self.session_id = f"bd_{datetime.now().strftime('%Y%m%d_%H%M%S')}"
    
    def get_end_time(self) -> datetime:
        """Get session end time"""
        return self.start_time + timedelta(minutes=self.duration_minutes)
    
    def is_active(self) -> bool:
        """Check if session is currently active"""
        return not self.ended and datetime.now() < self.get_end_time()
    
    def get_time_remaining(self) -> str:
        """Get remaining time in session"""
        if self.ended:
            return "Session ended"
        
        remaining = self.get_end_time() - datetime.now()
        if remaining.total_seconds() <= 0:
            return "Time's up!"
        
        minutes = int(remaining.total_seconds() // 60)
        seconds = int(remaining.total_seconds() % 60)
        return f"{minutes:02d}:{seconds:02d}"
    
    def end_session(self):
        """End the session early"""
        self.ended = True


@dataclass
class AccountabilityPartner:
    """An accountability partner or support person"""
    name: str
    contact_method: str  # "text", "call", "email", etc.
    preferred_checkin_frequency: str  # "daily", "weekly", etc.
    last_checkin: Optional[datetime] = None
    shared_goals: List[str] = None
    
    def __post_init__(self):
        if self.shared_goals is None:
            self.shared_goals = []
    
    def is_checkin_due(self) -> bool:
        """Check if a check-in is due"""
        if self.last_checkin is None:
            return True
        
        days_since = (datetime.now() - self.last_checkin).days
        
        if self.preferred_checkin_frequency == "daily":
            return days_since >= 1
        elif self.preferred_checkin_frequency == "weekly":
            return days_since >= 7
        elif self.preferred_checkin_frequency == "biweekly":
            return days_since >= 14
        
        return False


@dataclass
class Achievement:
    """An achievement to share or celebrate"""
    title: str
    description: str
    date_achieved: datetime
    category: str  # "task", "habit", "streak", "milestone"
    shared_with: List[str] = None
    
    def __post_init__(self):
        if self.shared_with is None:
            self.shared_with = []


class SocialSupport:
    """Manages social support features"""
    
    def __init__(self):
        self.accountability_partners: List[AccountabilityPartner] = []
        self.active_session: Optional[BodyDoublingSession] = None
        self.session_history: List[BodyDoublingSession] = []
        self.achievements: List[Achievement] = []
        self._create_sample_data()
    
    def _create_sample_data(self):
        """Create sample accountability partners"""
        sample_partner = AccountabilityPartner(
            name="Study Buddy",
            contact_method="text",
            preferred_checkin_frequency="weekly",
            shared_goals=["Task completion", "Daily habits"]
        )
        self.accountability_partners.append(sample_partner)
    
    def start_body_doubling_session(self, name: str, duration_minutes: int, 
                                  task_focus: Optional[str] = None) -> BodyDoublingSession:
        """Start a new body doubling session"""
        if self.active_session and self.active_session.is_active():
            print("🚨 Another session is already active!")
            return self.active_session
        
        session = BodyDoublingSession(
            name=name,
            start_time=datetime.now(),
            duration_minutes=duration_minutes,
            task_focus=task_focus
        )
        
        self.active_session = session
        print(f"👥 Body doubling session started: {name}")
        print(f"⏰ Duration: {duration_minutes} minutes")
        if task_focus:
            print(f"🎯 Focus: {task_focus}")
        print("💡 Work alongside others virtually - you're not alone!")
        
        return session
    
    def end_current_session(self):
        """End the current body doubling session"""
        if not self.active_session or not self.active_session.is_active():
            print("🌸 No active session to end")
            return
        
        self.active_session.end_session()
        self.session_history.append(self.active_session)
        
        duration = (datetime.now() - self.active_session.start_time).total_seconds() / 60
        print(f"✅ Session completed! You focused for {duration:.0f} minutes")
        print("🎉 Great job working together!")
        
        self.active_session = None
    
    def get_current_session(self) -> Optional[BodyDoublingSession]:
        """Get current active session"""
        if self.active_session and self.active_session.is_active():
            return self.active_session
        return None
    
    def add_accountability_partner(self, name: str, contact_method: str, 
                                 frequency: str, shared_goals: List[str] = None):
        """Add a new accountability partner"""
        partner = AccountabilityPartner(
            name=name,
            contact_method=contact_method,
            preferred_checkin_frequency=frequency,
            shared_goals=shared_goals or []
        )
        self.accountability_partners.append(partner)
        print(f"🤝 Added accountability partner: {name}")
    
    def record_checkin(self, partner_name: str):
        """Record a check-in with an accountability partner"""
        for partner in self.accountability_partners:
            if partner.name.lower() == partner_name.lower():
                partner.last_checkin = datetime.now()
                print(f"📞 Check-in recorded with {partner.name}!")
                return True
        return False
    
    def get_due_checkins(self) -> List[str]:
        """Get list of overdue check-ins"""
        due_checkins = []
        for partner in self.accountability_partners:
            if partner.is_checkin_due():
                due_checkins.append(f"Check in with {partner.name} ({partner.contact_method})")
        return due_checkins
    
    def add_achievement(self, title: str, description: str, category: str):
        """Add an achievement to celebrate"""
        achievement = Achievement(
            title=title,
            description=description,
            date_achieved=datetime.now(),
            category=category
        )
        self.achievements.append(achievement)
        print(f"🏆 Achievement unlocked: {title}!")
        print(f"   {description}")
    
    def get_celebration_reminders(self) -> List[str]:
        """Get reminders to celebrate recent achievements"""
        recent_achievements = [
            a for a in self.achievements
            if (datetime.now() - a.date_achieved).days <= 7
        ]
        
        if recent_achievements:
            return [f"🎉 Celebrate: {a.title}" for a in recent_achievements[-2:]]
        return []
    
    def display_social_dashboard(self):
        """Display social support dashboard"""
        print("\n👥 SOCIAL SUPPORT DASHBOARD")
        print("=" * 50)
        
        # Active session
        if self.active_session and self.active_session.is_active():
            print(f"🔴 ACTIVE SESSION: {self.active_session.name}")
            print(f"   Time remaining: {self.active_session.get_time_remaining()}")
            if self.active_session.task_focus:
                print(f"   Focus: {self.active_session.task_focus}")
        else:
            print("⚪ No active body doubling session")
        
        # Accountability partners
        print(f"\n🤝 ACCOUNTABILITY PARTNERS ({len(self.accountability_partners)}):")
        for partner in self.accountability_partners:
            status = "✅" if not partner.is_checkin_due() else "🟡"
            last_checkin = partner.last_checkin.strftime("%Y-%m-%d") if partner.last_checkin else "Never"
            print(f"   {status} {partner.name} | Last: {last_checkin} | {partner.contact_method}")
        
        # Recent achievements
        recent_achievements = [
            a for a in self.achievements
            if (datetime.now() - a.date_achieved).days <= 30
        ]
        
        if recent_achievements:
            print(f"\n🏆 RECENT ACHIEVEMENTS ({len(recent_achievements)}):")
            for achievement in recent_achievements[-3:]:
                days_ago = (datetime.now() - achievement.date_achieved).days
                print(f"   🌟 {achievement.title} ({days_ago} days ago)")
        
        # Session stats
        total_sessions = len(self.session_history)
        total_time = sum((s.get_end_time() - s.start_time).total_seconds() / 60 
                        for s in self.session_history if s.ended)
        
        print(f"\n📊 STATS:")
        print(f"   Total sessions: {total_sessions}")
        print(f"   Total focus time: {total_time:.0f} minutes ({total_time/60:.1f} hours)")
        
        print("=" * 50)
    
    def get_body_doubling_tips(self) -> List[str]:
        """Get tips for effective body doubling"""
        return [
            "🎯 Set a specific task or goal for the session",
            "📱 Agree on minimal talking to maintain focus",
            "⏰ Use shared timers for breaks and check-ins",
            "🌸 Be supportive - presence is more important than productivity",
            "💻 Consider virtual background if camera makes you uncomfortable",
            "☕ Schedule short breaks together",
            "🎵 Agree on background music/sounds preferences",
            "📝 Share quick wins and struggles during breaks",
            "🚀 Celebrate completed sessions together",
            "💛 Remember: parallel presence reduces isolation"
        ]