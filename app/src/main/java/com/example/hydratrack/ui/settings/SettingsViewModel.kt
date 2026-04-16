package com.example.hydratrack.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydratrack.domain.repository.HydraTrackRepository
import com.example.hydratrack.worker.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: HydraTrackRepository,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    val notificationsEnabled: StateFlow<Boolean> = repository.isNotificationsEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val reminderInterval: StateFlow<Int> = repository.getReminderInterval()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 60)

    val themeMode: StateFlow<String> = repository.getThemeMode()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "System")

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNotificationsEnabled(enabled)
            if (enabled) {
                reminderScheduler.scheduleReminder(reminderInterval.value)
            } else {
                reminderScheduler.cancelReminders()
            }
        }
    }

    fun updateReminderInterval(minutes: Int) {
        viewModelScope.launch {
            repository.setReminderInterval(minutes)
            if (notificationsEnabled.value) {
                reminderScheduler.scheduleReminder(minutes)
            }
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
