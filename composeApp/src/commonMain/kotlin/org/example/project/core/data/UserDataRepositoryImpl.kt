package org.example.project.core.data

import kotlinx.coroutines.flow.Flow
import org.example.project.core.domain.UserDataRepository
import org.example.project.core.data.datastore.MIUPreferencesDataSource
import org.example.project.settings.domain.Language
import org.example.project.settings.domain.Settings

class UserDataRepositoryImpl(
    private val miuPreferencesDataSource: MIUPreferencesDataSource,
): UserDataRepository {

    override val userData: Flow<Settings> = miuPreferencesDataSource.userData

    override suspend fun setDynamicColor(useDynamicColor: Boolean) {
        miuPreferencesDataSource.setDynamicColor(useDynamicColor)
    }




    override suspend fun appTheme(isDarkTheme: Boolean) {
        miuPreferencesDataSource.setAppTheme(isDarkTheme)
    }

    override suspend fun changeLanguage(language: Language) {
        setAppLanguage(language)
        miuPreferencesDataSource.setLanguagePreference(language)
    }
}

expect fun setAppLanguage(language: Language)