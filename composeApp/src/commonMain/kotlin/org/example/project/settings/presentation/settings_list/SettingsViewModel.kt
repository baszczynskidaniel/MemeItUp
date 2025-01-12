package org.example.project.settings.presentation.settings_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.project.core.domain.UserDataRepository

class SettingsViewModel(
    private val userDataRepository: UserDataRepository
): ViewModel() {

    private val _preferences = userDataRepository.userData
    private val _state = MutableStateFlow<SettingsState>(SettingsState())
    val state = combine(_state, _preferences) {
        state, preferences ->
            SettingsState(
                settings = preferences
            )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _state.value
    )





    fun onAction(action: SettingsAction) {
        viewModelScope.launch {
            when (action) {
                is SettingsAction.OnDarkThemeChange -> {
                    userDataRepository.appTheme(action.darkThemeChange)
                }

                is SettingsAction.OnDynamicColorChange -> {
                    userDataRepository.setDynamicColor(action.dynamicColorChange)
                }

            }

        }
    }
}