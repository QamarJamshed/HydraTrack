package com.example.hydratrack.di

import com.example.hydratrack.data.repository.HydraTrackRepositoryImpl
import com.example.hydratrack.domain.repository.HydraTrackRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHydraTrackRepository(
        hydraTrackRepositoryImpl: HydraTrackRepositoryImpl
    ): HydraTrackRepository
}
