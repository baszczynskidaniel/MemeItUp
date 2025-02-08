package org.example.project.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

actual class DataStoreFactory(
    private val context: Context
) {
    actual fun create(): DataStore<Preferences> {

        return PreferenceDataStoreFactory.createWithPath(
            produceFile = { context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath.toPath() }
        )

    }
}
