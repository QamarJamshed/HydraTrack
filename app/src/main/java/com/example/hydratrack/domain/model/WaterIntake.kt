package com.example.hydratrack.domain.model

data class WaterIntake(
    val id: Long = 0,
    val amountMl: Int,
    val timestamp: Long,
    val drinkTypeId: Long,
    val drinkTypeName: String = "Water"
)
