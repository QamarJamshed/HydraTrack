package com.example.hydratrack.data.repository

import android.graphics.Bitmap
import com.example.hydratrack.domain.repository.GenerativeAiRepository
import com.example.hydratrack.domain.repository.LoggingResult
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenerativeAiRepositoryImpl @Inject constructor() : GenerativeAiRepository {

    private val apiKey = "AIzaSyDI_cYP7Swumzv28onRpTvIfEzwiT5ZNUQ"

    private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey,
            systemInstruction = content {
                text("You are HydraCoach, a helpful health assistant. Respond in the user's language (English or Urdu). " +
                     "Help with hydration, exercise, and general health tips.")
            }
        )
    }

    override suspend fun generateResponse(prompt: String, context: String?): String {
        val fullPrompt = if (context != null) "$context\n\nUser: $prompt" else prompt
        return try {
            val response = generativeModel.generateContent(fullPrompt)
            response.text ?: "I'm sorry, I couldn't understand that."
        } catch (e: Exception) {
            "AI Error: ${e.localizedMessage}"
        }
    }

    override suspend fun parseLoggingIntent(text: String): LoggingResult? {
        val prompt = "Extract water intake from: \"$text\". Return ONLY JSON: {\"amountMl\": Int, \"drinkType\": String}. Standard: Glass=250, Bottle=500. If no intake mentioned, return amountMl: 0."
        return try {
            val response = generativeModel.generateContent(prompt)
            val jsonText = response.text?.substringAfter("{")?.substringBeforeLast("}")?.let { "{$it}" }
            if (jsonText != null) {
                val ml = jsonText.substringAfter("\"amountMl\":").substringBefore(",").trim().toIntOrNull() ?: 0
                val type = jsonText.substringAfter("\"drinkType\":").substringBefore("}").trim().removeSurrounding("\"msg\":").trim().removeSurrounding("\"")
                if (ml > 0) LoggingResult(ml, type) else null
            } else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun estimateVolumeFromImage(bitmap: Bitmap): LoggingResult? {
        return null
    }
}
