package com.eltescode.estimations.features.estimation.data.repository

import com.eltescode.estimations.features.estimation.data.local.model.EstimationCached
import com.eltescode.estimations.features.estimation.data.local.model.EstimationDao
import com.eltescode.estimations.features.estimation.domain.EstimationRepository
import com.eltescode.estimations.features.estimation.domain.model.EstimationDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*

class EstimationRepositoryImpl(private val estimationDao: EstimationDao) : EstimationRepository {

    override suspend fun saveEstimationToLocal(estimationDomain: EstimationDomain) {
        withContext(Dispatchers.IO) {
            estimationDao.saveEstimationToLocal(
                EstimationCached(
                    estimationDomain
                )
            )
        }
    }

    override fun getAllEstimationsFromLocal(): Flow<List<EstimationDomain>> {
        return estimationDao.getAllEstimationsFromLocal()
            .map { estimationCached -> estimationCached.map { it.toEstimationDomain() } }

    }

    override fun getSingleEstimationsFromLocal(id: UUID): Flow<EstimationDomain> {
        return estimationDao.getSingleEstimationsFromLocal(id).map { it.toEstimationDomain() }

    }

    override suspend fun dropDataBase() {
        withContext(Dispatchers.IO) {
            estimationDao.dropDataBase()
        }
    }

    override suspend fun updateEstimation(estimationDomain: EstimationDomain) {
        withContext(Dispatchers.IO) {
            estimationDao.updateEstimation(
                EstimationCached(
                    estimationDomain
                )
            )
        }
    }

    override suspend fun deleteEstimation(estimationDomain: EstimationDomain) {
        estimationDao.deleteEstimation(EstimationCached(estimationDomain))
    }

}