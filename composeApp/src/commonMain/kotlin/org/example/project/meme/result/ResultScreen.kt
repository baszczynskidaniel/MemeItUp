package org.example.project.meme.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEachIndexed
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
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.meme.data.dto.GameStateEnum
import org.example.project.meme.data.dto.PlayerDto
import org.example.project.meme.data.dto.ResultDto
import org.example.project.meme.data.mappers.toMeme
import org.example.project.meme.data.network.KtorRemoteLobbyDataSource
import org.example.project.meme.domain.GameSession
import org.example.project.meme.domain.Meme
import org.example.project.meme.presentation.components.MemeImageWithTexts


@Composable
fun MemeWithScore(
    modifier: Modifier = Modifier,
    meme: Meme,
    score: Int,
    author: String,
    isOnThisDevice: Boolean
) {
    val contentColor = if(isOnThisDevice) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    ElevatedCard(
        modifier = modifier,

    ) {
        Column(
            modifier = Modifier.padding(LocalDimensions.current.mediumPadding),
            verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.mediumPadding),

        ) {
            MemeImageWithTexts(
                meme = meme,
                modifier.fillMaxWidth()
            )
            Text(
                "Author: $author",
                color = contentColor
            )
            Text(
                "Score: $score",
                color = contentColor
            )
        }
    }
}

@Composable
fun PlayerResultItem(
    modifier: Modifier = Modifier,
    player: PlayerDto,
    isOnThisDevice: Boolean,
    place: Int,

) {
    val contentColor = if(isOnThisDevice) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(
                text = player.name,
                color = contentColor
            )
        },
        leadingContent = {
            Text(
                text = "$place. ",
                color = contentColor,
            )
        },
        trailingContent = {
            Text(
                text = player.score.toString(),
                color = contentColor,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    state: ResultState,
    onAction: (ResultAction) -> Unit,
    modifier: Modifier = Modifier,
    onGameStateChange: (GameStateEnum) -> Unit,
    onDisconnect: () -> Unit
) {
    LaunchedEffect(state.session?.gameState) {
        if(state.session?.gameState == GameStateEnum.LOBBY || state.session?.gameState == GameStateEnum.PLAY) {
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
            Text("Round ${state.roundResult?.round} / ${state.roundResult?.numberOfRounds}")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, false),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.mediumPadding)
        ) {
            Text(
                "Leaderboard",
                style = MaterialTheme.typography.titleLarge
            )
            state.players.fastForEachIndexed { index, player ->
                PlayerResultItem(
                    modifier = Modifier
                        .widthIn(max = LocalDimensions.current.maxButtonWidth)
                        .fillMaxWidth(),
                    player = player,
                    place = index + 1,
                    isOnThisDevice = player.connectionId == state.session?.player?.connectionId
                )
                if (index != state.players.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier
                            .widthIn(max = LocalDimensions.current.maxButtonWidth)
                            .fillMaxWidth(),
                    )
                }
            }
            Text(
                "Memes",
                style = MaterialTheme.typography.titleLarge
            )

            state.roundResult?.memes?.sortedByDescending {
                it.score
            }?.forEachIndexed { index, memeInGame ->
                MemeWithScore(
                    modifier = Modifier
                        .widthIn(max = LocalDimensions.current.maxButtonWidth)
                        .fillMaxWidth(),
                    meme = memeInGame.meme.toMeme(),
                    author = memeInGame.author.name,
                    score = memeInGame.score,
                    isOnThisDevice = memeInGame.author.connectionId == state.session?.player?.connectionId
                )
            }
        }
        Button(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),

            onClick = {
                onAction(ResultAction.OnNextRound)
            }
        ) {
            Text("Continue")
        }


    }
}

data class ResultState(
    val isLoading: Boolean = true,
    val roundResult: ResultDto? = null,
    val session: GameSession? = null,
    val players: List<PlayerDto> = emptyList()

)

sealed interface ResultAction {
    data object OnNextRound: ResultAction
}

class ResultViewModel(
    private val dataSource: KtorRemoteLobbyDataSource
): ViewModel() {

    private val _session = dataSource.getGameSession()
    private val _roundResult = dataSource.getRoundResult()
    private val _state = MutableStateFlow(ResultState())
    private val _players = dataSource.getPlayers()

    val state = combine(_state, _roundResult, _session, _players) { state, roundResult, session, players ->


        state.copy(
            roundResult = roundResult,
            session = session,
            players = players.players.sortedByDescending { it.score }
        )
    }.onStart {
        viewModelScope.launch {
            dataSource.getMemeTemplate()
                .onSuccess { memeTemplate ->
                    _state.update { it.copy(

                        isLoading = false,
                    ) }
                }
                .onError {
                    _state.update { it.copy(
                        isLoading = false,
                    ) }
                }
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: ResultAction) {
        when(action) {
            ResultAction.OnNextRound -> {
                viewModelScope.launch {
                    dataSource.finishReviewResult()
                }
            }
        }
    }
}