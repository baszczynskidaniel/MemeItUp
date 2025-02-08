package org.example.project.meme.presentation.vote

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.lepicekmichal.signalrkore.HubConnectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.meme.data.dto.GameStateEnum
import org.example.project.meme.data.dto.GameStateVotingDto
import org.example.project.meme.data.dto.MemeInGameDto
import org.example.project.meme.data.dto.PlayerDto
import org.example.project.meme.data.mappers.toMeme
import org.example.project.meme.data.network.KtorRemoteLobbyDataSource
import org.example.project.meme.domain.GameSession
import org.example.project.meme.domain.Meme
import org.example.project.meme.presentation.components.MemeImageWithTexts
import org.example.project.meme.presentation.create_meme.CreateMemeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteScreen(
    state: VoteState,
    onAction: (VoteAction) -> Unit,
    modifier: Modifier = Modifier,
    onGameStateChange: (GameStateEnum) -> Unit,
    onDisconnect: () -> Unit
) {
    LaunchedEffect(state.session?.gameState) {
        if(state.session?.gameState == GameStateEnum.ROUND_END || state.session?.gameState == GameStateEnum.GAME_END) {
            onGameStateChange(state.session.gameState)
        }
    }

    LaunchedEffect(state.session?.connectionState) {
        if(state.session?.connectionState == HubConnectionState.DISCONNECTED) {
            onDisconnect()
        }
    }
    MIUCenterSurface(
        title = {
            Text("Vote on meme")
        }
    ) {



        if(state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            MemeImageWithTexts(
                modifier = Modifier
                    .heightIn(max = 400.dp),
                meme = state.currentMeme
            )
            Row(
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxSurfaceWidth)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val emoji = mapOf<String, Int> (
                    "\uD83E\uDD71" to 1,
                    "\uD83D\uDE03" to 2,
                    "\uD83E\uDEE5" to 3,
                    "\uD83D\uDE42" to 4,
                    "\uD83E\uDD23" to 5,
                )

                emoji.forEach { entry ->
                    Box(
                        modifier = Modifier.clickable {
                            if(state.enableVoting) {
                                onAction(VoteAction.MakeVote(entry.value))
                            }
                        }
                    ) {
                        Text(entry.key, fontSize = 48.sp)
                    }
                }
            }
        }
    }
}

data class VoteState(
    val isLoading: Boolean = true,
    val votingState: GameStateVotingDto = GameStateVotingDto.default(),
    val memeId: String = "",
    val currentMeme: Meme = Meme(),
    val enableVoting: Boolean = true,
    val session: GameSession? = null,
)

sealed interface VoteAction {
    data class MakeVote(val score: Int): VoteAction
}

class VoteViewModel(
    private val dataSource: KtorRemoteLobbyDataSource,

): ViewModel() {

    private val _votingState = dataSource.receiveGameStateVotingDto()
    private val _currentMeme = dataSource.getMemeVotingStream()
    private val _session = dataSource.getGameSession()


    private val _state = MutableStateFlow(VoteState())
    val state = combine(_state, _votingState, _currentMeme, _session) { state, votingState, currentMeme, session ->
        state.copy(
            currentMeme = currentMeme.meme.toMeme(),
            votingState = votingState,
            memeId = currentMeme.memeInGameId,
            session = session,
            isLoading = false
        )

    }
    .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _state.value
    )

    fun onAction(action: VoteAction) {
        when(action) {
            is VoteAction.MakeVote -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        enableVoting = false
                        )
                    }
                    dataSource.vote(state.value.memeId, action.score)
                    _state.update { it.copy(
                        enableVoting = true
                        )
                    }
                }
            }
        }
    }
}