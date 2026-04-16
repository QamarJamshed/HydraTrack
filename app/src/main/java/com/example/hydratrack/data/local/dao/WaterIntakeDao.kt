package com.example.hydratrack.data.local.dao

import androidx.room.*
import com.example.hydratrack.data.local.entity.WaterIntakeEntity
import kotlinx.coroutines.flow.Flow

data class WaterIntakeWithDrinkType(
    @Embedded val intake: WaterIntakeEntity,
    @Relation(
        parentColumn = "drinkTypeId",
        entityColumn = "id"
    )
    val drinkType: com.example.hydratrack.data.local.entity.DrinkTypeEntity
)

@Dao
interface WaterIntakeDao {
    @Transaction
    @Query("SELECT * FROM water_intake WHERE timestamp >= :startOfDay AND timestamp <= :endOfDay")
    fun getIntakeWithDrinkTypeForPeriod(startOfDay: Long, endOfDay: Long): Flow<List<WaterIntakeWithDrinkType>>

    @Query("SELECT * FROM water_intake WHERE timestamp >= :startOfDay AND timestamp <= :endOfDay")
    fun getIntakeForPeriod(startOfDay: Long, endOfDay: Long): Flow<List<WaterIntakeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntake(intake: WaterIntakeEntity)

    @Delete
    suspend fun deleteIntake(intake: WaterIntakeEntity)

    @Query("SELECT SUM(amountMl) FROM water_intake WHERE timestamp >= :startOfDay AND timestamp <= :endOfDay")
    fun getTotalIntakeForDay(startOfDay: Long, endOfDay: Long): Flow<Int?>

    @Query("SELECT timestamp / 86400000 as day, SUM(amountMl) as total FROM water_intake WHERE timestamp >= :startTime GROUP BY day ORDER BY day ASC")
    fun getDailyTotalsFrom(startTime: Long): Flow<List<DayTotal>>

    @Query("DELETE FROM water_intake")
    suspend fun clearAll()
}

data class DayTotal(
    val day: Long,
    val total: Int
)
