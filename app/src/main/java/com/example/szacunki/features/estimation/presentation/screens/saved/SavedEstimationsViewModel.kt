package com.example.szacunki.features.estimation.presentation.screens.saved

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szacunki.core.extensions.calculateFolderSize
import com.example.szacunki.core.extensions.formatSize
import com.example.szacunki.features.estimation.domain.EstimationRepository
import com.example.szacunki.features.estimation.presentation.model.EstimationDisplayable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SavedEstimationsViewModel(
    private val estimationRepository: EstimationRepository
) :
    ViewModel() {
    private val _folderSize = MutableStateFlow("")
    val folderSize = _folderSize.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

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

    fun emitMessage(message: String) {
        viewModelScope.launch {
            _message.emit(message)
        }
    }

    fun calculateFolderSize(context: Context) {
        viewModelScope.launch {
            val folderPath = context.filesDir.absolutePath + File.separator + "estimationPdf"
            withContext(Dispatchers.IO) {
                _folderSize.value = File(folderPath).calculateFolderSize().formatSize()
            }
        }
    }
}