package com.example.szacunki.features.estimation.presentation.screens.estimation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.szacunki.features.estimation.domain.EstimationRepository
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeRowDisplayable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class EstimationViewModel(private val estimationRepository: EstimationRepository) : ViewModel() {

    private var _estimation = MutableStateFlow<EstimationDisplayable?>(null)
    val estimation = _estimation.asStateFlow()

    private fun getSingleEstimation(id: UUID) {
        viewModelScope.launch {
            val estimationFromLocal = estimationRepository
                .getSingleEstimationsFromLocal(id)
                .first()
                .let { EstimationDisplayable(it) }
            _estimation.update { estimationFromLocal }
        }
    }

    private fun saveToLocal(estimationDisplayable: EstimationDisplayable) {
        viewModelScope.launch { estimationRepository.saveEstimationToLocal(estimationDisplayable.toEstimationDomain()) }
    }

    private fun updateEstimationToLocal(estimationDisplayable: EstimationDisplayable) {
        viewModelScope.launch { estimationRepository.updateEstimation(estimationDisplayable.toEstimationDomain()) }
    }

    fun dropDataBase() {
        viewModelScope.launch { estimationRepository.dropDataBase() }
    }

    fun onIdPassed(id: UUID) {
        getSingleEstimation(id)
    }

    fun createNewSheet(
        sectionNumber: String?,
        treeNameList: List<String>?,
        baseDiameterList: List<String>
    ) {
        if (treeNameList != null) {
            _estimation.update {
                EstimationDisplayable(
                    sectionNumber = checkNotNull(sectionNumber),
                    trees = treeNameList.map { name ->
                        TreeDisplayable(
                            name = name,
                            treeRows = baseDiameterList.map { TreeRowDisplayable(diameter = it) })
                    }).also { saveToLocal(it) }
            }
        }
    }

    fun updateEstimationState(estimation: EstimationDisplayable) {
        _estimation.update { estimation.also { updateEstimationToLocal(it) } }
    }
}