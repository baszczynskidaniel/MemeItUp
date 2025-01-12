package org.example.project.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.example.project.settings.domain.Language
import org.example.project.settings.domain.Settings

class MIUPreferencesDataSource(private val dataStore: DataStore<Preferences>) {
    val userData = dataStore.data.map { getSettings() }
    suspend fun setAppTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.DARK_THEME] = isDarkTheme
        }

    }

    private suspend fun getSettings(): Settings {
        return dataStore.data.map { preferences ->
            Settings(
                darkTheme = preferences[PreferenceKeys.DARK_THEME] ?: true,
                dynamicColor = preferences[PreferenceKeys.DYNAMIC_COLOR] ?: true,
                language = Language.fromString((preferences[PreferenceKeys.LANGUAGE] ?: Language.ENGLISH.toString())) ?: Language.ENGLISH,
            )
        }.first()
    }

    suspend fun setDynamicColor(useDynamicColor: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.DYNAMIC_COLOR] = useDynamicColor
        }
    }

    suspend fun setLanguagePreference(language: Language) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.LANGUAGE] = language.isoFormat
        }
    }
}

