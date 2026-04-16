package com.example.hydratrack.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydratrack.domain.model.UserProfile
import com.example.hydratrack.domain.usecase.CalculateWaterGoalUseCase
import com.example.hydratrack.domain.usecase.SaveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class OnboardingUiState(
    val name: String = "",
    val weight: String = "",
    val gender: String = "Male",
    val wakeUpTime: String = "07:00",
    val sleepTime: String = "23:00",
    val calculatedGoal: Int = 0
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val calculateWaterGoalUseCase: CalculateWaterGoalUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onWeightChange(weight: String) {
        _uiState.update { 
            val goal = if (weight.isNotEmpty()) {
                calculateWaterGoalUseCase(weight.toFloatOrNull() ?: 0f, it.gender)
            } else 0
            it.copy(weight = weight, calculatedGoal = goal) 
        }
    }

    fun onGenderChange(gender: String) {
        _uiState.update { 
            val goal = if (it.weight.isNotEmpty()) {
                calculateWaterGoalUseCase(it.weight.toFloatOrNull() ?: 0f, gender)
            } else 0
            it.copy(gender = gender, calculatedGoal = goal) 
        }
    }

    fun onWakeUpTimeChange(time: String) {
        _uiState.update { it.copy(wakeUpTime = time) }
    }

    fun onSleepTimeChange(time: String) {
        _uiState.update { it.copy(sleepTime = time) }
    }

    fun completeOnboarding() {
        val currentState = _uiState.value
        val profile = UserProfile(
            name = currentState.name,
            weightKg = currentState.weight.toFloatOrNull() ?: 70f,
            gender = currentState.gender,
            dailyGoalMl = currentState.calculatedGoal,
            wakeUpTime = parseTimeToMillis(currentState.wakeUpTime),
            sleepTime = parseTimeToMillis(currentState.sleepTime)
        )
        viewModelScope.launch {
            saveUserProfileUseCase(profile)
        }
    }

    private fun parseTimeToMillis(time: String): Long {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = format.parse(time) ?: Date()
        val calendar = Calendar.getInstance()
        val timeCalendar = Calendar.getInstance().apply { this.time = date }
        
        calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        
        return calendar.timeInMillis
    }
}
