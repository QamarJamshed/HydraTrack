package com.example.hydratrack.domain.usecase

import com.example.hydratrack.domain.model.Achievement
import com.example.hydratrack.domain.repository.HydraTrackRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAchievementsUseCase @Inject constructor(
    private val repository: HydraTrackRepository
) {
    operator fun invoke(): Flow<List<Achievement>> {
        return repository.getAllAchievements()
    }
}
