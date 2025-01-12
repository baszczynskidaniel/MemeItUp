package org.example.project.meme.presentation.navigation_view_models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayersNavigationViewModel: ViewModel() {
    private val _players = MutableStateFlow<String?>(null)
    val players = _players.asStateFlow()

    fun onConfirm(players: String?) {
        _players.value = players
    }
}