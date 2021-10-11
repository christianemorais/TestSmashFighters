package com.chrismorais.smashultimatefighters.features.fighters.di

import com.chrismorais.smashultimatefighters.data.api.RetrofitConfig
import com.chrismorais.smashultimatefighters.features.fighters.FightersListViewModel
import com.chrismorais.smashultimatefighters.data.repository.FightersRepository
import com.chrismorais.smashultimatefighters.data.repository.UniverseRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val fightersModule = module {

    single {
        RetrofitConfig(context = androidContext()).getService()
    }

    factory { UniverseRepository(get()) }
    factory { FightersRepository(get()) }

    viewModel { FightersListViewModel(get(), get()) }
}