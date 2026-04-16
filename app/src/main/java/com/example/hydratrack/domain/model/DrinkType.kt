package com.example.hydratrack.domain.model

data class DrinkType(
    val id: Long,
    val name: String,
    val iconName: String,
    val hydrationFactor: Float,
    val isDefault: Boolean
)
