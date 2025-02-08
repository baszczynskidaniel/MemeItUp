package org.example.project.meme.presentation.round_end

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import org.example.project.core.presentation.design_system.MIUCenterSurface

@Composable
fun RoundEndScreen(
    state: RoundEndState,
    onAction: (RoundEndAction) -> Unit,
    modifier: Modifier = Modifier
) {
    MIUCenterSurface {
        Text("Round end")
    }
}

data class RoundEndState(
    val lol: Int = 3,
)

sealed class RoundEndAction {

}

class RoundEndViewModel(

): ViewModel() {

    private val _state = MutableStateFlow(RoundEndState())
    val state = _state
        .onStart {  }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: RoundEndAction) {

    }
}