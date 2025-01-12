package org.example.project.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

actual class DataStoreFactory(
    private val producePath: () -> String
) {
    actual fun create(): DataStore<Preferences> {
        
        return PreferenceDataStoreFactory.createWithPath(

            produceFile = { producePath().toPath() }
        )
    }
}

