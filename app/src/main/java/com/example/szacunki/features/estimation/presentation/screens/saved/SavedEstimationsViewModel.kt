package com.example.szacunki.features.estimation.presentation.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szacunki.features.estimation.domain.EstimationRepository
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SavedEstimationsViewModel(private val estimationRepository: EstimationRepository) :
    ViewModel() {

    var estimations = getAllEstimations()

    private var _estimationToDelete = MutableStateFlow<EstimationDisplayable?>(null)
    val estimationToDelete = _estimationToDelete.asStateFlow()

    private fun getAllEstimations() = estimationRepository.getAllEstimationsFromLocal()
        .map { estimationDomain -> estimationDomain.map { EstimationDisplayable(it) } }

    fun deleteEstimation(estimationDisplayable: EstimationDisplayable) {
        viewModelScope.launch { estimationRepository.deleteEstimation(estimationDisplayable.toEstimationDomain()) }
    }

    fun dropDataBase() {
        viewModelScope.launch { estimationRepository.dropDataBase() }
    }

    fun updateEstimation(estimation: EstimationDisplayable) {
        _estimationToDelete.update { estimation }
    }
}