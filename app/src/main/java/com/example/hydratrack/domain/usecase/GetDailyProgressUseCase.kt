package com.example.hydratrack.domain.usecase

import com.example.hydratrack.domain.repository.HydraTrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class GetDailyProgressUseCase @Inject constructor(
    private val repository: HydraTrackRepository
) {
    operator fun invoke(): Flow<Float> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfDay = calendar.timeInMillis

        return combine(
            repository.getTotalDailyIntake(startOfDay, endOfDay),
            repository.getUserProfile().map { it?.dailyGoalMl ?: 2000 }
        ) { totalIntake, goal ->
            if (goal > 0) totalIntake.toFloat() / goal else 0f
        }
    }
}
