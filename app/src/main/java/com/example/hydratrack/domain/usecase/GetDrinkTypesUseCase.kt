package com.example.hydratrack.domain.usecase

import com.example.hydratrack.domain.model.DrinkType
import com.example.hydratrack.domain.repository.HydraTrackRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDrinkTypesUseCase @Inject constructor(
    private val repository: HydraTrackRepository
) {
    operator fun invoke(): Flow<List<DrinkType>> {
        return repository.getAllDrinkTypes()
    }
}
