package com.example.hydratrack.data.repository

import com.example.hydratrack.data.local.HydraTrackPreferences
import com.example.hydratrack.data.local.dao.*
import com.example.hydratrack.data.local.entity.*
import com.example.hydratrack.domain.model.*
import com.example.hydratrack.domain.repository.HydraTrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HydraTrackRepositoryImpl @Inject constructor(
    private val waterIntakeDao: WaterIntakeDao,
    private val userProfileDao: UserProfileDao,
    private val drinkTypeDao: DrinkTypeDao,
    private val achievementDao: AchievementDao,
    private val preferences: HydraTrackPreferences,
    private val database: com.example.hydratrack.data.local.HydraTrackDatabase
) : HydraTrackRepository {

    override fun getUserProfile(): Flow<UserProfile?> =
        userProfileDao.getUserProfile().map { it?.toDomain() }

    override suspend fun saveUserProfile(profile: UserProfile) {
        userProfileDao.insertProfile(profile.toEntity())
    }

    override fun getDailyIntake(startOfDay: Long, endOfDay: Long): Flow<List<WaterIntake>> =
        waterIntakeDao.getIntakeWithDrinkTypeForPeriod(startOfDay, endOfDay).map { list ->
            list.map { item ->
                item.intake.toDomain().copy(drinkTypeName = item.drinkType.name)
            }
        }

    override fun getTotalDailyIntake(startOfDay: Long, endOfDay: Long): Flow<Int> =
        waterIntakeDao.getTotalIntakeForDay(startOfDay, endOfDay).map { it ?: 0 }

    override suspend fun addWaterIntake(intake: WaterIntake) {
        waterIntakeDao.insertIntake(intake.toEntity())
    }

    override suspend fun deleteWaterIntake(intake: WaterIntake) {
        waterIntakeDao.deleteIntake(intake.toEntity())
    }

    override fun getAllDrinkTypes(): Flow<List<DrinkType>> =
        drinkTypeDao.getAllDrinkTypes().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun addDrinkType(drinkType: DrinkType) {
        drinkTypeDao.insertDrinkType(drinkType.toEntity())
    }

    override fun getAllAchievements(): Flow<List<Achievement>> =
        achievementDao.getAllAchievements().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun updateAchievement(achievement: Achievement) {
        achievementDao.updateAchievement(achievement.toEntity())
    }

    override fun getWeeklyIntake(startTime: Long): Flow<List<com.example.hydratrack.data.local.dao.DayTotal>> =
        waterIntakeDao.getDailyTotalsFrom(startTime)

    override fun isOnboardingCompleted(): Flow<Boolean> = preferences.isOnboardingCompleted

    override suspend fun setOnboardingCompleted(completed: Boolean) =
        preferences.setOnboardingCompleted(completed)

    override fun getThemeMode(): Flow<String> = preferences.themeMode

    override suspend fun setThemeMode(mode: String) =
        preferences.setThemeMode(mode)

    override suspend fun logout() {
        preferences.clearAll()
        database.clearAllData()
    }

    override fun isNotificationsEnabled(): Flow<Boolean> = preferences.notificationsEnabled

    override suspend fun setNotificationsEnabled(enabled: Boolean) =
        preferences.setNotificationsEnabled(enabled)

    override fun getReminderInterval(): Flow<Int> = preferences.reminderIntervalMinutes

    override suspend fun setReminderInterval(minutes: Int) =
        preferences.setReminderInterval(minutes)

    // Mappers
    private fun UserProfileEntity.toDomain() = UserProfile(
        name = name,
        weightKg = weightKg,
        gender = gender,
        dailyGoalMl = dailyGoalMl,
        wakeUpTime = wakeUpTime,
        sleepTime = sleepTime
    )

    private fun UserProfile.toEntity() = UserProfileEntity(
        name = name,
        weightKg = weightKg,
        gender = gender,
        dailyGoalMl = dailyGoalMl,
        wakeUpTime = wakeUpTime,
        sleepTime = sleepTime
    )

    private fun WaterIntakeEntity.toDomain() = WaterIntake(
        id = id,
        amountMl = amountMl,
        timestamp = timestamp,
        drinkTypeId = drinkTypeId
    )

    private fun WaterIntake.toEntity() = WaterIntakeEntity(
        id = id,
        amountMl = amountMl,
        timestamp = timestamp,
        drinkTypeId = drinkTypeId
    )

    private fun DrinkTypeEntity.toDomain() = DrinkType(
        id = id,
        name = name,
        iconName = iconName,
        hydrationFactor = hydrationFactor,
        isDefault = isDefault
    )

    private fun DrinkType.toEntity() = DrinkTypeEntity(
        id = id,
        name = name,
        iconName = iconName,
        hydrationFactor = hydrationFactor,
        isDefault = isDefault
    )

    private fun AchievementEntity.toDomain() = Achievement(
        id = id,
        name = name,
        description = description,
        iconName = iconName,
        isUnlocked = isUnlocked
    )

    private fun Achievement.toEntity() = AchievementEntity(
        id = id,
        name = name,
        description = description,
        iconName = iconName,
        isUnlocked = isUnlocked
    )
}
