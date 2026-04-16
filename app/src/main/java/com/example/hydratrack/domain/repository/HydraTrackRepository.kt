package com.example.hydratrack.domain.repository

import com.example.hydratrack.domain.model.UserProfile
import com.example.hydratrack.domain.model.WaterIntake
import com.example.hydratrack.domain.model.DrinkType
import com.example.hydratrack.domain.model.Achievement
import kotlinx.coroutines.flow.Flow

interface HydraTrackRepository {
    // User Profile
    fun getUserProfile(): Flow<UserProfile?>
    suspend fun saveUserProfile(profile: UserProfile)

    // Water Intake
    fun getDailyIntake(startOfDay: Long, endOfDay: Long): Flow<List<WaterIntake>>
    fun getTotalDailyIntake(startOfDay: Long, endOfDay: Long): Flow<Int>
    suspend fun addWaterIntake(intake: WaterIntake)
    suspend fun deleteWaterIntake(intake: WaterIntake)

    // Drink Types
    fun getAllDrinkTypes(): Flow<List<DrinkType>>
    suspend fun addDrinkType(drinkType: DrinkType)

    // Achievements
    fun getAllAchievements(): Flow<List<Achievement>>
    suspend fun updateAchievement(achievement: Achievement)

    // Trends
    fun getWeeklyIntake(startTime: Long): Flow<List<com.example.hydratrack.data.local.dao.DayTotal>>

    // Preferences
    fun isOnboardingCompleted(): Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean)
    fun getThemeMode(): Flow<String>
    suspend fun setThemeMode(mode: String)
    suspend fun logout()
    fun isNotificationsEnabled(): Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
    fun getReminderInterval(): Flow<Int>
    suspend fun setReminderInterval(minutes: Int)
}
