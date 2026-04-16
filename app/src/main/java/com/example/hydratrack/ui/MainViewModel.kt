package com.example.hydratrack.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydratrack.domain.model.UserProfile
import com.example.hydratrack.domain.model.WaterIntake
import com.example.hydratrack.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getDailyIntakeUseCase: GetDailyIntakeUseCase,
    private val getTotalDailyIntakeUseCase: GetTotalDailyIntakeUseCase,
    private val addWaterIntakeUseCase: AddWaterIntakeUseCase,
    private val deleteWaterIntakeUseCase: DeleteWaterIntakeUseCase,
    private val getDrinkTypesUseCase: GetDrinkTypesUseCase,
    private val isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase,
    private val getDailyProgressUseCase: GetDailyProgressUseCase,
    private val repository: com.example.hydratrack.domain.repository.HydraTrackRepository
) : ViewModel() {

    val themeMode: StateFlow<String> = repository.getThemeMode()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "System")

    val userProfile: StateFlow<UserProfile?> = getUserProfileUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val dailyIntake: StateFlow<List<WaterIntake>> = getDailyIntakeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalDailyIntake: StateFlow<Int> = getTotalDailyIntakeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val dailyProgress: StateFlow<Float> = getDailyProgressUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    private val _isHotDay = MutableStateFlow(false)
    val isHotDay = _isHotDay.asStateFlow()

    init {
        // Simulate a weather check - in a real app, this would call a Weather API
        viewModelScope.launch {
            val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            _isHotDay.value = hour in 11..16 // Hot during midday
        }
    }

    val adjustedGoal: StateFlow<Int> = combine(userProfile, isHotDay) { profile, hot ->
        val baseGoal = profile?.dailyGoalMl ?: 2000
        if (hot) baseGoal + 500 else baseGoal
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2000)

    val drinkTypes = getDrinkTypesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isOnboardingCompleted = isOnboardingCompletedUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun addWaterIntake(amountMl: Int, drinkTypeId: Long) {
        viewModelScope.launch {
            addWaterIntakeUseCase(
                WaterIntake(
                    amountMl = amountMl,
                    timestamp = System.currentTimeMillis(),
                    drinkTypeId = drinkTypeId
                )
            )
        }
    }

    fun deleteWaterIntake(intake: WaterIntake) {
        viewModelScope.launch {
            deleteWaterIntakeUseCase(intake)
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            repository.setThemeMode(mode)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
