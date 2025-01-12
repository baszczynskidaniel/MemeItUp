package org.example.project.settings.presentation.settings_list

import org.example.project.settings.domain.Settings

data class SettingsState(
    val settings: Settings = Settings()
)
