package com.example.hydratrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drink_types")
data class DrinkTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconName: String,
    val hydrationFactor: Float = 1.0f,
    val isDefault: Boolean = false
)
