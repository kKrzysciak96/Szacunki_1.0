package com.example.szacunki.features.estimation.domain

import com.example.szacunki.features.estimation.domain.model.EstimationDomain
import kotlinx.coroutines.flow.Flow

interface EstimationRepository {

    suspend fun saveEstimationToLocal(estimationDomain: EstimationDomain)
    fun getAllEstimationsFromLocal(): Flow<List<EstimationDomain>>
    fun getSingleEstimationsFromLocal(id: Int): Flow<EstimationDomain>
    suspend fun dropDataBase()
    suspend fun updateEstimation(estimationDomain: EstimationDomain)

}