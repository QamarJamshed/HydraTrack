package com.example.hydratrack.domain.model

data class Achievement(
    val id: Long,
    val name: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean
)
