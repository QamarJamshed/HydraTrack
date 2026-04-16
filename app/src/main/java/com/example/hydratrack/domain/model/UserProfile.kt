package com.example.hydratrack.domain.model

data class UserProfile(
    val name: String,
    val weightKg: Float,
    val gender: String,
    val dailyGoalMl: Int,
    val wakeUpTime: Long,
    val sleepTime: Long
)
