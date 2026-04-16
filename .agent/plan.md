# Project Plan

App Name: HydraTrack. Smart water reminder and hydration tracking application. 
- Clean Architecture (Presentation, Domain, Data).
- MVVM pattern.
- Room Database for logs, profile, drink types, achievements.
- DataStore for preferences.
- WorkManager for smart reminders (wake/sleep time aware).
- Material 3 with dark/light mode and vibrant colors.
- Full Edge-to-Edge display.
- Adaptive app icon.
- Multi-language: English, Arabic, Hebrew, Russian.
- Screens: Onboarding, Home (with animated water UI), History (Charts), Drink Customization, Achievements, Settings.
- Libraries: Hilt, Room, DataStore, WorkManager, Coil, Compose.

## Project Brief

# HydraTrack - Project Brief

HydraTrack is a modern, smart hydration tracking application designed to help users maintain optimal health through personalized water intake goals and intelligent reminders. The app features a vibrant, energetic Material 3 design with fluid animations and full edge-to-edge support.

## Features
*   **Personalized Goal Calculation**: Automatically determines daily water intake targets based on user profile data, including weight and gender.
*   **Interactive Intake Tracking**: A seamless logging experience with quick-add presets (250ml, 500ml) and an animated "water level" UI that visualizes progress in real-time.
*   **Smart Hydration Reminders**: Context-aware notifications scheduled via WorkManager that intelligently alert users between their specific wake-up and sleep times.
*   **Progress History & Achievements**: A comprehensive data-driven dashboard that tracks hydration trends over time and rewards consistency with achievement badges.

## High-Level Technical Stack
*   **Kotlin**: The primary language for modern, concise, and safe Android development.
*   **Jetpack Compose (Material 3)**: A declarative UI toolkit used to create a vibrant, adaptive, and edge-to-edge user experience.
*   **Coroutines & Flow**: For efficient asynchronous programming and reactive data streams across the app.
*   **Room (via KSP)**: A robust local database solution used to persist water intake logs, user profiles, and achievements.
*   **Hilt**: Dependency injection framework to support a scalable Clean Architecture (MVVM) implementation.
*   **WorkManager**: For reliable, battery-efficient background scheduling of hydration reminders.
*   **DataStore**: For storing lightweight user preferences and application settings.

## Implementation Steps

### Task_1_Setup_Architecture_and_Data: Configure Hilt DI, Room Database for logs and profile, DataStore for preferences, and WorkManager for smart hydration reminders. Implement the Repository pattern to coordinate data flow.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Hilt is integrated and providing dependencies
  - Room database is functional with entities for logs and profile
  - DataStore correctly persists user preferences
  - WorkManager is configured for background hydration alerts
- **StartTime:** 2026-04-08 10:55:06 PKT

### Task_2_UI_Foundation_and_Onboarding: Implement a vibrant Material 3 theme with full Edge-to-Edge support and dark/light mode. Create the Onboarding flow to collect user data (weight, gender, wake/sleep times), calculate the daily water goal, and setup app navigation.
- **Status:** PENDING
- **Acceptance Criteria:**
  - App uses Material 3 with vibrant colors and supports dark mode
  - Full Edge-to-Edge display is implemented
  - Onboarding flow successfully calculates and saves user goal
  - Navigation between main screens is functional

### Task_3_Core_Features_and_Localization: Develop the Home screen with an animated water visualization, quick-add presets, and the History screen with intake charts. Implement Drink Customization, Achievements, and Multi-language support (English, Arabic, Hebrew, Russian).
- **Status:** PENDING
- **Acceptance Criteria:**
  - Home screen features an animated water UI and functional logging
  - History screen displays hydration trends via charts
  - Achievements and Drink Customization are fully implemented
  - App supports English, Arabic, Hebrew, and Russian

### Task_4_Final_Run_and_Verify: Generate a functional adaptive app icon. Perform a comprehensive Run and Verify of the entire application to ensure stability, requirement alignment, and correct behavior of smart reminders.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Adaptive app icon is present and matches the theme
  - App builds and runs successfully without crashes
  - Smart reminders trigger correctly based on user wake/sleep times
  - Critic agent verifies alignment with all user requirements

