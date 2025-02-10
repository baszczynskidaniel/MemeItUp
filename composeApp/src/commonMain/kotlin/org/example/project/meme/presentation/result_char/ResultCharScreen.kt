package org.example.project.meme.presentation.result_char

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.design_system.MIUTopAppBar
import org.example.project.core.presentation.design_system.MediumSpacer
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.meme.data.dto.GameStateEnum
import org.example.project.meme.data.dto.PlayerStateEnum
import org.example.project.meme.data.dto.ResultDto
import org.example.project.meme.data.mappers.toMeme
import org.example.project.meme.data.network.KtorRemoteLobbyDataSource
import org.example.project.meme.domain.GameSession
import org.example.project.meme.result.MemeWithScore
import org.example.project.meme.result.PlayersResultList
import org.example.project.meme.result.ResultAction

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ResultCharScreen(
    state: ResultCharState,
    onAction: (ResultCharAction) -> Unit,
    modifier: Modifier = Modifier,
    onGameStateChange: (GameStateEnum) -> Unit,
    onDisconnect: () -> Unit
) {
    LaunchedEffect(state.session?.gameState) {
        if (state.session?.gameState == GameStateEnum.LOBBY || state.session?.gameState == GameStateEnum.PLAY) {
            onGameStateChange(state.session.gameState)
        }
    }

    LaunchedEffect(state.session?.connectionState) {
        if (state.session?.connectionState == HubConnectionState.DISCONNECTED) {
            onDisconnect()
        }
    }
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.session?.player?.state == PlayerStateEnum.WAITING) {
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
                Text("Waiting for: ", style = MaterialTheme.typography.displayMedium)
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
        Column(
            modifier = Modifier

                .fillMaxSize()
        ) {

            MIUTopAppBar(
                title = {
                    Text("Round ${state.roundResult?.round} / ${state.roundResult?.numberOfRounds}")
                }
            )
            Column(
                modifier = Modifier.padding(LocalDimensions.current.mediumPadding).fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.mediumPadding)
            ) {
                PlayersResultList(
                    modifier = Modifier
                        .widthIn(max = LocalDimensions.current.maxButtonWidth)
                        .fillMaxWidth(),
                    players = state!!.session!!.players.sortedByDescending { it.score },
                    deviceConnectionId = state.session!!.player.connectionId
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.mediumPadding),
                    horizontalArrangement = Arrangement.Center,

                    ) {


                    state.roundResult?.memes?.sortedByDescending {
                        it.score
                    }?.forEachIndexed { index, memeInGame ->
                        Box() {
                            MemeWithScore(
                                modifier = Modifier
                                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                                    .fillMaxWidth()
                                    .border(width = if(memeInGame.score > 0) 0.dp else 2.dp, shape = CardDefaults.shape, color = MaterialTheme.colorScheme.primary)

                                ,

                                meme = memeInGame.meme.toMeme(),
                                author = memeInGame.author.name,
                                score = memeInGame.score,
                                isOnThisDevice = memeInGame.author.connectionId == state.session?.player?.connectionId
                            )
                            if (index < state.roundResult!!.memes!!.size - 1) {
                                Box(modifier.width(LocalDimensions.current.mediumPadding))
                            }

                        }
                    }
                }
                Button(
                    modifier = Modifier
                        .widthIn(max = LocalDimensions.current.maxButtonWidth)
                        .fillMaxWidth(),

                    onClick = {
                        onAction(ResultCharAction.OnNextRound)
                    }
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

data class ResultCharState(
    val isLoading: Boolean = true,
    val roundResult: ResultDto? = null,
    val session: GameSession? = null,
)

sealed class ResultCharAction {
    data object OnNextRound: ResultCharAction()
}

class ResultCharViewModel(
    private val dataSource: KtorRemoteLobbyDataSource
): ViewModel() {

    private val _session = dataSource.getGameSession()
    private val _roundResult = dataSource.getRoundResult()
    private val _state = MutableStateFlow(ResultCharState())

    val state = combine(_state, _roundResult, _session) { state, roundResult, session ->


        state.copy(
            roundResult = roundResult,
            session = session,

        )
    }.onStart {
        viewModelScope.launch {
            _state.update { it.copy(

                isLoading = false,
            ) }

        }
    } .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _state.value
    )

    fun onAction(action: ResultCharAction) {
        when(action) {
            ResultCharAction.OnNextRound -> {
                viewModelScope.launch {
                    dataSource.finishReviewResult()
                }
            }
        }
    }
}