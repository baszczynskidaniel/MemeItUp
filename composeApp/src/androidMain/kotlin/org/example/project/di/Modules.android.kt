package org.example.project.di

import org.example.project.settings.data.DataStoreFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DataStoreFactory(androidApplication()) }
    }