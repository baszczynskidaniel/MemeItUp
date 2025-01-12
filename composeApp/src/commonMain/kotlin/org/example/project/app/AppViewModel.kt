package org.example.project.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.project.core.domain.UserDataRepository
import org.example.project.settings.domain.AppTheme
import org.example.project.settings.domain.Settings

class AppViewModel(
    val userDataRepository: UserDataRepository,

): ViewModel() {
    val state = userDataRepository.userData.stateIn(
        scope = viewModelScope,
        initialValue = Settings(),
        started = SharingStarted.WhileSubscribed(5_000)
    )


}