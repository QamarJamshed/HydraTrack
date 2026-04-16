package com.example.hydratrack.domain.usecase

import com.example.hydratrack.domain.model.WaterIntake
import com.example.hydratrack.domain.repository.HydraTrackRepository
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

class AddWaterIntakeUseCase @Inject constructor(
    private val repository: HydraTrackRepository
) {
    suspend operator fun invoke(intake: WaterIntake) {
        repository.addWaterIntake(intake)
        checkAchievements()
    }

    private suspend fun checkAchievements() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        val endOfDay = calendar.timeInMillis + 86400000

        val intakes = repository.getDailyIntake(startOfDay, endOfDay).first()
        val totalIntake = intakes.sumOf { it.amountMl }
        val profile = repository.getUserProfile().first()
        val achievements = repository.getAllAchievements().first()

        // First Sip
        achievements.find { it.name == "First Sip" && !it.isUnlocked }?.let {
            repository.updateAchievement(it.copy(isUnlocked = true))
        }

        // Hydration Hero
        profile?.let { user ->
            if (totalIntake >= user.dailyGoalMl) {
                achievements.find { it.name == "Hydration Hero" && !it.isUnlocked }?.let {
                    repository.updateAchievement(it.copy(isUnlocked = true))
                }
            }
        }
    }
}
