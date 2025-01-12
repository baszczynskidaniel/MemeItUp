package org.example.project.settings.presentation.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.project.core.domain.UserDataRepository
import org.example.project.settings.domain.Language
import org.example.project.settings.presentation.settings_list.SettingsState

class LanguageViewModel(
    private val userDataRepository: UserDataRepository
): ViewModel() {
    private val _preferences = userDataRepository.userData

    private val _state = MutableStateFlow<LanguageState>(LanguageState(selectedLanguage = Language.ENGLISH))
    val state = combine(_state, _preferences) {
            state, preferences ->
        LanguageState(
             selectedLanguage = preferences.language
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _state.value
    )

    fun onAction(action: LanguageAction) {
        viewModelScope.launch {
            when (action) {
                is LanguageAction.OnLanguageChange -> userDataRepository.changeLanguage(action.languageChange)
            }
        }
    }
}