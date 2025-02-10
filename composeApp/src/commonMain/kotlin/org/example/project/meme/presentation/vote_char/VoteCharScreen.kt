package org.example.project.meme.presentation.vote_char

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import org.example.project.core.presentation.design_system.MIUCard
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.design_system.MediumSpacer
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.meme.data.dto.GameStateEnum
import org.example.project.meme.data.dto.GameStateVotingDto
import org.example.project.meme.data.dto.PlayerStateEnum
import org.example.project.meme.data.dto.VoteCharDto
import org.example.project.meme.data.mappers.toMeme
import org.example.project.meme.data.network.KtorRemoteLobbyDataSource
import org.example.project.meme.domain.GameSession
import org.example.project.meme.domain.Meme
import org.example.project.meme.presentation.components.MemeImageWithTexts
import org.example.project.meme.presentation.vote.VoteAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteCharScreen(
    state: VoteCharState,
    onAction: (VoteCharAction) -> Unit,
    modifier: Modifier = Modifier,
    onGameStateChange: (GameStateEnum) -> Unit,
    onDisconnect: () -> Unit
) {
    LaunchedEffect(state.session?.gameState) {
        if(state.session?.gameState == GameStateEnum.ROUND_END || state.session?.gameState == GameStateEnum.ROUND_END_CHAR) {
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


        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else  if(state.session?.player?.state == PlayerStateEnum.WAITING) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),

                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Waiting for char: ", style = MaterialTheme.typography.displayMedium)
                    MediumSpacer()
                    Text(
                        state.session.players.filter { it.state == PlayerStateEnum.PLAYING }
                            .map { it.name }.joinToString(", "),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                }
            }
        } else {
            state.votingState?.memesToVote?.forEach {
                MIUCard(
                    modifier = modifier
                        .widthIn(LocalDimensions.current.maxButtonWidth)
                        .fillMaxWidth()
                        .clickable {
                            onAction(VoteCharAction.MakeVote(
                                it.memeInGameId, 1
                            ))
                        }
                ) {
                    MemeImageWithTexts(
                        modifier = Modifier
                            .heightIn(max = 400.dp),
                        meme = it.meme.toMeme()
                    )
                }
            }
        }
    }
}

data class VoteCharState(
    val isLoading: Boolean = true,
    val votingState: VoteCharDto? = null,
    val memes: List<Meme> = emptyList(),
    val session: GameSession? = null,
)

sealed class VoteCharAction {
    data class MakeVote(val memeId: String, val score: Int): VoteCharAction()
}

class VoteCharViewModel(
    private val dataSource: KtorRemoteLobbyDataSource,
): ViewModel() {

    private val _session = dataSource.getGameSession()


    private val _state = MutableStateFlow(VoteCharState())
    val state = combine(_state, _session) {state, session ->
        state.copy(
            session = session
        )
    }
        .onStart {
            viewModelScope.launch {
                val _votingState = dataSource.getVoteCharDto()
                _state.update { it.copy(
                    isLoading = false,
                    votingState = _votingState
                ) }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: VoteCharAction) {
        when(action) {
            is VoteCharAction.MakeVote ->  viewModelScope.launch {
                dataSource.vote(action.memeId, action.score)
            }
        }
    }
}