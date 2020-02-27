package com.message.toschat.di

import com.message.toschat.network.remote.FireBaseService
import com.message.toschat.ui.collection.CollectionViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


var appModules = module {

    factory {
        FireBaseService()
    }

    viewModel {
        CollectionViewModel()
    }
}