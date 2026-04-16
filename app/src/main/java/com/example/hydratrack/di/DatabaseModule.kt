package com.example.hydratrack.di

import android.content.Context
import androidx.room.Room
import com.example.hydratrack.data.local.HydraTrackDatabase
import com.example.hydratrack.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HydraTrackDatabase {
        return Room.databaseBuilder(
            context,
            HydraTrackDatabase::class.java,
            HydraTrackDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideWaterIntakeDao(database: HydraTrackDatabase): WaterIntakeDao = database.waterIntakeDao()

    @Provides
    fun provideUserProfileDao(database: HydraTrackDatabase): UserProfileDao = database.userProfileDao()

    @Provides
    fun provideDrinkTypeDao(database: HydraTrackDatabase): DrinkTypeDao = database.drinkTypeDao()

    @Provides
    fun provideAchievementDao(database: HydraTrackDatabase): AchievementDao = database.achievementDao()
}
