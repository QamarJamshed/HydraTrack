package com.example.hydratrack.domain.usecase

import com.example.hydratrack.domain.model.UserProfile
import com.example.hydratrack.domain.repository.HydraTrackRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: HydraTrackRepository
) {
    operator fun invoke(): Flow<UserProfile?> {
        return repository.getUserProfile()
    }
}
