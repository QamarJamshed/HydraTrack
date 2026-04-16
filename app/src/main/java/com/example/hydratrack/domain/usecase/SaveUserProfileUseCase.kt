package com.example.hydratrack.domain.usecase

import com.example.hydratrack.domain.model.UserProfile
import com.example.hydratrack.domain.repository.HydraTrackRepository
import javax.inject.Inject

class SaveUserProfileUseCase @Inject constructor(
    private val repository: HydraTrackRepository
) {
    suspend operator fun invoke(profile: UserProfile) {
        repository.saveUserProfile(profile)
        repository.setOnboardingCompleted(true)
    }
}
