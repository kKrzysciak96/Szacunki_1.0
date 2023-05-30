package com.example.szacunki.features.estimation.data.local.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EstimationDao {

    @Insert
    suspend fun saveEstimationToLocal(estimation: EstimationCached)

    @Query("SELECT * FROM EstimationCached")
    fun getAllEstimationsFromLocal(): Flow<List<EstimationCached>>

    @Query("SELECT * FROM EstimationCached WHERE id = :id")
    fun getSingleEstimationsFromLocal(id: Int): Flow<EstimationCached>

    @Update
    suspend fun updateEstimation(estimation: EstimationCached)

    @Query("DELETE FROM EstimationCached")
    suspend fun dropDataBase()
}