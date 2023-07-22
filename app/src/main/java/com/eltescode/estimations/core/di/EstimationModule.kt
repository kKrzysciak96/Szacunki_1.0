package com.eltescode.estimations.core.di


import com.eltescode.estimations.features.estimation.data.repository.EstimationRepositoryImpl
import com.eltescode.estimations.features.estimation.domain.EstimationRepository
import com.eltescode.estimations.features.estimation.presentation.screens.estimation.EstimationViewModel
import com.eltescode.estimations.features.estimation.presentation.screens.saved.SavedEstimationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val estimationModule = module {
    factory<EstimationRepository> { EstimationRepositoryImpl(get()) }
    viewModel { EstimationViewModel(get()) }
    viewModel { SavedEstimationsViewModel(get()) }
}