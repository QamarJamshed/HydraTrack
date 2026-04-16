package com.example.hydratrack.ui.assistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydratrack.domain.model.WaterIntake
import com.example.hydratrack.domain.repository.GenerativeAiRepository
import com.example.hydratrack.domain.repository.HydraTrackRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Message(val text: String, val isUser: Boolean)

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val repository: HydraTrackRepository,
    private val aiRepository: GenerativeAiRepository
) : ViewModel() {

    private val _messages = MutableStateFlow(listOf(Message("Hi! I'm your HydraCoach. How can I help you today?\nالسلام علیکم! میں آپ کا ہائیڈرا کوچ ہوں۔ میں آج آپ کی کیا مدد کر سکتا ہوں؟", false)))
    val messages = _messages.asStateFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping = _isTyping.asStateFlow()

    fun sendMessage(text: String) {
        viewModelScope.launch {
            _messages.update { it + Message(text, true) }
            _isTyping.value = true
            
            try {
                // 1. Try to parse as a logging intent (e.g., "I drank water")
                val loggingResult = aiRepository.parseLoggingIntent(text)
                
                val response = if (loggingResult != null && loggingResult.amountMl > 0) {
                    repository.addWaterIntake(WaterIntake(
                        amountMl = loggingResult.amountMl,
                        timestamp = System.currentTimeMillis(),
                        drinkTypeId = 1L
                    ))
                    if (isUrdu(text)) "ٹھیک ہے، میں نے ${loggingResult.amountMl} ملی لیٹر پانی ریکارڈ کر لیا ہے۔ ✅"
                    else "Logged ${loggingResult.amountMl}ml of water for you! ✅"
                } else {
                    // 2. Normal Chat (Hydration tips, health, etc.)
                    val profile = repository.getUserProfile().first()
                    val context = "User Profile: ${profile?.name}, Weight: ${profile?.weightKg}kg, Goal: ${profile?.dailyGoalMl}ml"
                    aiRepository.generateResponse(text, context)
                }
                _messages.update { it + Message(response, false) }
            } catch (e: Exception) {
                _messages.update { it + Message("Sorry, I encountered an error. Please check your API key and connection.", false) }
            } finally {
                _isTyping.value = false
            }
        }
    }

    private fun isUrdu(text: String): Boolean {
        // Simple check for Urdu script
        val urduCharacters = Regex("[\\u0600-\\u06FF]")
        return urduCharacters.containsMatchIn(text)
    }
}
