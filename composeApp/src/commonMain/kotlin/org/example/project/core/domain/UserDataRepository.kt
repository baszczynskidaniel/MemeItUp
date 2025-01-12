package org.example.project.core.domain

import kotlinx.coroutines.flow.Flow
import org.example.project.settings.domain.AppTheme
import org.example.project.settings.domain.Language
import org.example.project.settings.domain.Settings

interface UserDataRepository {

    val userData: Flow<Settings>

    suspend fun appTheme(isDarkTheme: Boolean)
    suspend fun setDynamicColor(useDynamicColor: Boolean)
    suspend fun changeLanguage(language: Language)
}