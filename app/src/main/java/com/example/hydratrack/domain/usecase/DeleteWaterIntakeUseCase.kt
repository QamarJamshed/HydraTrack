package com.example.hydratrack.domain.usecase

import com.example.hydratrack.domain.model.WaterIntake
import com.example.hydratrack.domain.repository.HydraTrackRepository
import javax.inject.Inject

class DeleteWaterIntakeUseCase @Inject constructor(
    private val repository: HydraTrackRepository
) {
    suspend operator fun invoke(intake: WaterIntake) {
        repository.deleteWaterIntake(intake)
    }
}
