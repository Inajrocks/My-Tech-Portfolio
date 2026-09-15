# Replit Project Guide

## Overview

**Nudge** is a console-based Java application designed specifically for neurodivergent users, particularly those with ADHD, who struggle with remembering tasks and following through. The application focuses on creating a validating, encouraging, and fun user experience with motivational rewards, flexible reminder types, and gentle but effective task management.

## User Preferences

- Preferred communication style: Simple, everyday language
- Must be implemented in Python (console application)
- Focus on neurodivergent-friendly design with encouraging, validating feedback
- Emphasis on user experience over just functionality

## System Architecture

**Technology Stack:**
- Python 3.11
- Console-based application with text interface
- Object-oriented design with separate modules for different concerns

**Design Patterns:**
- Enum patterns for type safety (ReminderType, TaskCategory, MoodState, EnergyLevel)
- Manager pattern for task operations (TaskManager, RoutineManager, HabitTracker)
- System pattern for cross-cutting concerns (RewardSystem, OverwhelmManager)
- Modular architecture with separate files for each major feature

## Key Components

### Core Modules:

1. **enhanced_nudge_app.py** - Main application class and entry point
   - Handles user interaction and enhanced 20-option menu system
   - Coordinates between all enhanced modules
   - Provides comprehensive neurodivergent-friendly interface with mood tracking

2. **task_manager.py** - Core task management
   - Task class with full properties and methods
   - TaskManager for storage and operations
   - Support for all task categories and reminder types

3. **energy_tracker.py** - Energy and mood tracking system
   - 15 neurodivergent-specific mood states
   - 5-level energy tracking with spoon theory
   - Personalized recommendations based on current state

4. **reward_system.py** - Motivation and evidence-based affirmations
   - Point-based rewards with streak tracking
   - Research-backed affirmations for neurodivergent users
   - Mood-specific encouragement

5. **Enhanced Features:**
   - **pomodoro_timer.py** - Focus sessions with customizable timers
   - **routine_manager.py** - Morning/evening routines with progress tracking
   - **habit_tracker.py** - Comprehensive habit building with streak visualization
   - **task_breakdown.py** - AI-powered task breakdown with executive function support
   - **overwhelm_manager.py** - Sensory overwhelm detection and coping strategies
   - **social_support.py** - Body doubling and accountability features
   - **recurring_task.py** - Advanced recurring task scheduling

## Data Flow

1. **Task Creation Flow:**
   - User selects "Add New Task" from main menu
   - Application prompts for task details (name, deadline, category, reminder type, points)
   - Task object is created and added to TaskManager
   - Confirmation message displayed

2. **Task Completion Flow:**
   - User selects "Mark Task Complete"
   - TaskManager displays active tasks
   - User selects task by ID
   - Task marked as completed
   - RewardSystem awards points and provides encouraging feedback
   - Streak tracking updated

3. **Reminder System:**
   - Each loop iteration checks for due/overdue tasks
   - TaskManager identifies tasks needing reminders
   - Task objects generate personalized reminder messages based on type
   - Reminders displayed at start of each menu cycle

4. **Missed Task Support:**
   - User can mark tasks as missed
   - RewardSystem provides supportive check-in messages
   - Streak reset with encouraging language
   - Optional space for user reflection

## External Dependencies

- **Python Standard Library:** datetime, enum, typing, dataclasses, uuid, random
- **No external libraries** - self-contained application using only built-in Python modules

## Deployment Strategy

- **Development:** Run with `python enhanced_nudge_app.py`
- **Workflow Configuration:** Automated execution via Replit workflows
- **Environment:** Requires Python 3.11 or compatible version

## Recent Changes

**July 12, 2025 - MAJOR ENHANCEMENT:**
- ✅ Complete Enhanced Nudge application with ALL advanced features
- ✅ Original core features maintained and enhanced
- ✅ NEW MAJOR FEATURES IMPLEMENTED:

**🔄 Task Scheduling & Recurring Tasks:**
- Recurring tasks with daily/weekly/custom patterns
- Smart rescheduling when tasks are missed
- Flexible scheduling options

**⏰ Time Management Support:**
- Pomodoro timer with customizable sessions
- Task duration estimates and time blocking
- Multiple reminder times per task

**⚡ Energy & Mood Tracking:**
- Daily energy level check-ins (1-5 scale)
- Mood tracking with task correlation
- Personalized task recommendations based on energy

**🔔 Enhanced Reminder System:**
- Multiple reminder times per task
- Gentle escalation reminders
- Context-based reminder suggestions

**🧠 Executive Function Support:**
- AI-powered task breakdown into subtasks
- Priority matrix (Eisenhower method)
- Context switching helpers and preparation notes

**🌱 Habit Building & Routine Support:**
- Comprehensive habit tracker with streak visualization
- Morning/evening routine templates
- Flexible habit scheduling allowing for difficult days

**😰 Sensory & Overwhelm Management:**
- Overwhelm check-ins with coping strategies
- Sensory break reminders and suggestions
- Task load adjustment based on capacity

**👥 Social & Accountability Features:**
- Body doubling session timers
- Accountability partner system
- Achievement sharing and celebration

- ✅ Enhanced main application (EnhancedReminderApp) with 20+ menu options
- ✅ Comprehensive integration between all systems
- ✅ Personalized daily check-ins with energy/mood tracking
- ✅ Contextual suggestions based on user state
- ✅ All features designed with neurodivergent users in mind

**July 13, 2025 - ENHANCED MOOD & AFFIRMATION SYSTEM:**
- ✅ **15 neurodivergent-specific mood states** including:
  - Challenging: overwhelmed, sensory overload, RSD, hyperfocus crash, executive dysfunction, masking fatigue
  - Neutral: processing, cautious, baseline neutral
  - Positive: special interest mode, hyperfocus flow, stimming happy, content, motivated
- ✅ **Evidence-based affirmations** rooted in psychological research:
  - Self-compassion theory (Kristin Neff)
  - Growth mindset research (Carol Dweck)
  - Neurodivergent strengths validation
  - Executive function accommodation normalization
- ✅ **Comprehensive mood-specific recommendations** for each state
- ✅ **Enhanced check-in system** with:
  - Contextual follow-up questions based on mood state
  - Optional sensory trigger identification
  - Immediate mood-specific affirmations
  - Tailored suggestions based on current capacity
- ✅ **Integrated overwhelm management** with mood state correlation

**July 17, 2025 - COMPLETE CONVERSION TO PYTHON:**
- ✅ **Successfully converted entire application from Java to Python**
- ✅ **Maintained all enhanced features** including 15 mood states and evidence-based affirmations
- ✅ **Modular Python architecture** with separate files for each major feature
- ✅ **Enhanced console application** with comprehensive neurodivergent support
- ✅ **All 20+ menu options** working in Python environment
- ✅ **Preserved user experience** while improving code maintainability
- ✅ **Python 3.11 compatibility** using only standard library modules

## Key Features

### For Neurodivergent Users:
- **Encouraging Language:** All interactions use supportive, validating language
- **Flexible Reminders:** Choose from text, emoji, vibey, or gentle reminder styles
- **Self-Compassion:** Missed task check-ins focus on support rather than judgment
- **Visual Indicators:** Emojis and clear status symbols for easy scanning
- **Achievement System:** Positive reinforcement through points, streaks, and achievements
- **No Pressure Design:** Optional features and gentle prompting throughout

### Core Functionality:
- Create tasks with customizable properties
- Multiple task categories (medication, self-care, work, etc.)
- Deadline tracking with overdue detection
- Point system with customizable values (1-100 points)
- Streak tracking for motivation
- Achievement unlocking system
- Random affirmations on demand
- Comprehensive stats display