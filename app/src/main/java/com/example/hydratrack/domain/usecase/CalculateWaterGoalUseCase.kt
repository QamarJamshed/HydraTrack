package com.example.hydratrack.domain.usecase

import javax.inject.Inject

class CalculateWaterGoalUseCase @Inject constructor() {
    operator fun invoke(weightKg: Float, gender: String): Int {
        val multiplier = if (gender.lowercase() == "male") 35 else 31
        return (weightKg * multiplier).toInt()
    }
}
