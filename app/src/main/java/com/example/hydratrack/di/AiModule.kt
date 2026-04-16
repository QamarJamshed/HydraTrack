package com.example.hydratrack.di

import com.example.hydratrack.data.repository.GenerativeAiRepositoryImpl
import com.example.hydratrack.domain.repository.GenerativeAiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiModule {

    @Binds
    @Singleton
    abstract fun bindGenerativeAiRepository(
        impl: GenerativeAiRepositoryImpl
    ): GenerativeAiRepository
}
