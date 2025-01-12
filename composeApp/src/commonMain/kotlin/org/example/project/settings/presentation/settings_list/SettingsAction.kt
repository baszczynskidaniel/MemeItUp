package org.example.project.settings.presentation.settings_list

sealed interface SettingsAction {
    data class OnDarkThemeChange(val darkThemeChange: Boolean): SettingsAction
    data class OnDynamicColorChange(val dynamicColorChange: Boolean): SettingsAction
}