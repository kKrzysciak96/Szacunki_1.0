package com.example.szacunki.features.estimation.presentation.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szacunki.features.estimation.domain.EstimationRepository
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class SavedEstimationsViewModel(private val estimationRepository: EstimationRepository) :
    ViewModel() {

    val estimations = getAllEstimations()

    private fun getAllEstimations() = estimationRepository.getAllEstimationsFromLocal()
        .map { estimationDomain -> estimationDomain.map { EstimationDisplayable(it) } }


    private var _estimationFlow = MutableStateFlow<EstimationDisplayable?>(null)
    val estimationFlow = _estimationFlow.asStateFlow()


    fun getSingleEstimation(id: UUID) {
        viewModelScope.launch {
            val local: EstimationDisplayable =
                estimationRepository.getSingleEstimationsFromLocal(id).first()
                    .let { EstimationDisplayable(it) }
            _estimationFlow.update { local }
        }
    }

    fun updateEstimation(estimationDisplayable: EstimationDisplayable) {
        viewModelScope.launch { estimationRepository.updateEstimation(estimationDisplayable.toEstimationDomain()) }
    }

    fun dropDataBase() {
        viewModelScope.launch { estimationRepository.dropDataBase() }
    }


}