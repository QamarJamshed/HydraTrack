package com.example.hydratrack.domain.repository

import android.graphics.Bitmap

interface GenerativeAiRepository {
    suspend fun generateResponse(prompt: String, context: String? = null): String
    suspend fun parseLoggingIntent(text: String): LoggingResult?
    suspend fun estimateVolumeFromImage(bitmap: Bitmap): LoggingResult?
}

data class LoggingResult(
    val amountMl: Int,
    val drinkType: String
)
