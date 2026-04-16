package com.example.hydratrack.domain.usecase

import com.example.hydratrack.domain.repository.HydraTrackRepository
import javax.inject.Inject

class ToggleNotificationsUseCase @Inject constructor(
    private val repository: HydraTrackRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.setNotificationsEnabled(enabled)
    }
}
