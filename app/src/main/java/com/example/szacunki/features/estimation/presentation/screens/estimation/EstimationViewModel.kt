package com.example.szacunki.features.estimation.presentation.screens.estimation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szacunki.features.estimation.domain.EstimationRepository
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import com.example.szacunki.features.estimation.presentation.model.TreeDisplayable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class EstimationViewModel(private val estimationRepository: EstimationRepository) : ViewModel() {

    private var _estimationFlow = MutableStateFlow<EstimationDisplayable?>(null)
    val estimationFlow = _estimationFlow.asStateFlow()


    private fun getSingleEstimation(id: UUID) {
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

    private fun updateEstimationToLocal(estimationDisplayable: EstimationDisplayable) {
        viewModelScope.launch { estimationRepository.updateEstimation(estimationDisplayable.toEstimationDomain()) }
    }

    fun dropDataBase() {
        viewModelScope.launch { estimationRepository.dropDataBase() }
    }

    fun onIdPassed(id: UUID) {
        getSingleEstimation(id)
    }

    fun createNewSheet(sectionNumber: String?, treeNameList: List<String>?) {
        if (treeNameList != null) {
            _estimationFlow.update {
                EstimationDisplayable(
                    sectionNumber = checkNotNull(sectionNumber),
                    trees = treeNameList.map { TreeDisplayable(name = it) }).also {
                    saveToLocal(it)
                }
            }
        }
    }

    fun updateEstimationFlow(estimation: EstimationDisplayable) {
        _estimationFlow.update { estimation.also { updateEstimationToLocal(it) } }
    }


}