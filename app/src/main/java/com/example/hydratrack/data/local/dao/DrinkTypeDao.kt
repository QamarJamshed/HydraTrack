package com.example.hydratrack.data.local.dao

import androidx.room.*
import com.example.hydratrack.data.local.entity.DrinkTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkTypeDao {
    @Query("SELECT * FROM drink_types")
    fun getAllDrinkTypes(): Flow<List<DrinkTypeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrinkType(drinkType: DrinkTypeEntity)

    @Query("SELECT COUNT(*) FROM drink_types")
    suspend fun getCount(): Int
}
