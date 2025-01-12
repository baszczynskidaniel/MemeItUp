package org.example.project.di

import org.example.project.settings.data.DATA_STORE_FILE_NAME
import org.example.project.settings.data.DataStoreFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DataStoreFactory { DATA_STORE_FILE_NAME }
        }
    }

