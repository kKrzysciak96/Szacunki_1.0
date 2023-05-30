package com.example.szacunki.features.estimation.presentation.screens.estimation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szacunki.features.estimation.domain.EstimationRepository
import com.example.szacunki.features.estimation.domain.model.EstimationDomain
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EstimationViewModel(private val estimationRepository: EstimationRepository) : ViewModel() {

    private var _estimationFlow = MutableStateFlow<EstimationDisplayable?>(null)
    val estimationFlow = _estimationFlow.asStateFlow()


    fun getSingleEstimation(id: Int) {
        viewModelScope.launch {
            val local: EstimationDisplayable =
                estimationRepository.getSingleEstimationsFromLocal(id).first()
                    .let { EstimationDisplayable(it) }
            _estimationFlow.update { local }
        }
    }

    private fun saveToLocal(estimationDisplayable: EstimationDisplayable) {
        viewModelScope.launch { estimationRepository.saveEstimationToLocal(estimationDisplayable.toEstimationDomain()) }
        Log.d("SAVE", "SAVED")
    }

    fun updateEstimation(estimationDisplayable: EstimationDisplayable) {
        viewModelScope.launch { estimationRepository.updateEstimation(estimationDisplayable.toEstimationDomain()) }
    }

    fun dropDataBase() {
        viewModelScope.launch { estimationRepository.dropDataBase() }
    }

    fun onIdPassed(id: Int) {
        getSingleEstimation(id)
    }

    fun createNewSheet() {
        _estimationFlow.update { EstimationDisplayable(EstimationDomain()).also { saveToLocal(it) } }
    }


}