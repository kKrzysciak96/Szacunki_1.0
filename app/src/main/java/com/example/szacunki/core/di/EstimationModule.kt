package com.example.szacunki.core.di

import com.example.szacunki.features.estimation.data.repository.EstimationRepositoryImpl
import com.example.szacunki.features.estimation.domain.EstimationRepository
import com.example.szacunki.features.estimation.presentation.screens.estimation.EstimationViewModel
import com.example.szacunki.features.estimation.presentation.screens.saved.SavedEstimationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val estimationModule = module {
    factory<EstimationRepository> { EstimationRepositoryImpl(get()) }
    viewModel { EstimationViewModel(get()) }
    viewModel { SavedEstimationsViewModel(get()) }
}