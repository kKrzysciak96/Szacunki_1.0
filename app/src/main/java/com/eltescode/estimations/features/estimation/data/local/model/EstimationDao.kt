package com.eltescode.estimations.features.estimation.data.local.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface EstimationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEstimationToLocal(estimation: EstimationCached)

    @Delete
    suspend fun deleteEstimation(estimationCached: EstimationCached)

    @Query("SELECT * FROM EstimationCached")
    fun getAllEstimationsFromLocal(): Flow<List<EstimationCached>>

    @Query("SELECT * FROM EstimationCached WHERE id = :id")
    fun getSingleEstimationsFromLocal(id: UUID): Flow<EstimationCached>

    @Update
    suspend fun updateEstimation(estimation: EstimationCached)

    @Query("DELETE FROM EstimationCached")
    suspend fun dropDataBase()
}