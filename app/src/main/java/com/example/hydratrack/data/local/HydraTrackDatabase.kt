package com.example.hydratrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hydratrack.data.local.dao.*
import com.example.hydratrack.data.local.entity.*

@Database(
    entities = [
        WaterIntakeEntity::class,
        UserProfileEntity::class,
        DrinkTypeEntity::class,
        AchievementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HydraTrackDatabase : RoomDatabase() {
    abstract fun waterIntakeDao(): WaterIntakeDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun drinkTypeDao(): DrinkTypeDao
    abstract fun achievementDao(): AchievementDao

    suspend fun clearAllData() {
        waterIntakeDao().clearAll()
        userProfileDao().clearAll()
        achievementDao().resetAchievements()
    }

    companion object {
        const val DATABASE_NAME = "hydratrack_db"
    }
}
