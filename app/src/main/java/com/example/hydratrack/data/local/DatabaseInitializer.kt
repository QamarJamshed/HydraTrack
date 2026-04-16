package com.example.hydratrack.data.local

import com.example.hydratrack.data.local.dao.AchievementDao
import com.example.hydratrack.data.local.dao.DrinkTypeDao
import com.example.hydratrack.data.local.entity.AchievementEntity
import com.example.hydratrack.data.local.entity.DrinkTypeEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor(
    private val drinkTypeDao: DrinkTypeDao,
    private val achievementDao: AchievementDao
) {
    suspend fun initialize() {
        if (drinkTypeDao.getCount() == 0) {
            val defaultDrinks = listOf(
                DrinkTypeEntity(name = "Water", iconName = "water_drop", hydrationFactor = 1.0f, isDefault = true),
                DrinkTypeEntity(name = "Coffee", iconName = "coffee", hydrationFactor = 0.9f, isDefault = true),
                DrinkTypeEntity(name = "Tea", iconName = "emoji_food_beverage", hydrationFactor = 0.95f, isDefault = true),
                DrinkTypeEntity(name = "Juice", iconName = "local_drink", hydrationFactor = 0.85f, isDefault = true),
                DrinkTypeEntity(name = "Soda", iconName = "liquor", hydrationFactor = 0.6f, isDefault = true)
            )
            defaultDrinks.forEach { drinkTypeDao.insertDrinkType(it) }
        }

        val achievements = listOf(
            AchievementEntity(name = "First Sip", description = "Log your first drink", iconName = "star", isUnlocked = false),
            AchievementEntity(name = "Hydration Hero", description = "Meet your daily goal", iconName = "workspace_premium", isUnlocked = false),
            AchievementEntity(name = "Consistent", description = "3 day streak", iconName = "bolt", isUnlocked = false),
            AchievementEntity(name = "Hydration Master", description = "7 day streak", iconName = "military_tech", isUnlocked = false)
        )
        achievementDao.insertAchievements(achievements)
    }
}
