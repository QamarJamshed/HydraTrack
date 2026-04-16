package com.example.hydratrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 0, // Single user profile
    val name: String,
    val weightKg: Float,
    val gender: String, // "Male", "Female", "Other"
    val dailyGoalMl: Int,
    val wakeUpTime: Long, // Milliseconds from start of day
    val sleepTime: Long // Milliseconds from start of day
)
