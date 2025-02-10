package org.example.project.meme.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
import memeitup.composeapp.generated.resources.Res
import memeitup.composeapp.generated.resources.laptop
import memeitup.composeapp.generated.resources.smartphone
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.design_system.MIUCard
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.design_system.MIULabel
import org.example.project.core.presentation.design_system.MIUTopAppBar
import org.example.project.core.presentation.design_system.MediumSpacer
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.meme.data.dto.GameStateEnum
import org.example.project.meme.data.dto.PlayerDto
import org.example.project.meme.data.dto.PlayerStateEnum
import org.example.project.meme.data.dto.ResultDto
import org.example.project.meme.data.mappers.toMeme
import org.example.project.meme.data.network.KtorRemoteLobbyDataSource
import org.example.project.meme.domain.GameSession
import org.example.project.meme.domain.Meme
import org.example.project.meme.presentation.components.MemeImageWithTexts
import org.jetbrains.compose.resources.vectorResource


@Composable
fun MemeWithScore(
    modifier: Modifier = Modifier,
    meme: Meme,
    score: Int,
    author: String,
    isOnThisDevice: Boolean,

) {
    val contentColor = if(isOnThisDevice) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

    MIUCard(
        modifier = modifier,

    ) {
        MemeImageWithTexts(
            meme = meme,
            modifier = modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Author: $author",
                color = contentColor,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.End,
            )
            Text(
                "Score: $score",
                color = contentColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
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

@Composable
fun PlayersResultList(
    modifier: Modifier = Modifier,
    players: List<PlayerDto>,
    deviceConnectionId: String,

) {

    if(players.isNotEmpty()) {
        val maxPointsLength = players.maxBy { it.score }.toString().length
        val numberOfPlayers = players.size.toString().length
        MIUCard(
            modifier = modifier
        ) {
            MIULabel("Leaderboard")
            players.forEachIndexed { index, player ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.mediumPadding)
                ) {
                    if(deviceConnectionId == player.connectionId) {
                        Text(
                            text = "Place: ${index + 1}".padEnd(numberOfPlayers + 7, ' '),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        Text(
                            text = "Place: ${index + 1}".padEnd(numberOfPlayers + 7, ' '),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    if(deviceConnectionId == player.connectionId) {
                        Text(
                            modifier = Modifier.fillMaxWidth().weight(1f, false),
                            text = "${player.name} (Me)",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        Text(
                            player.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth().weight(1f, false),
                        )
                    }

                    if(deviceConnectionId == player.connectionId) {
                        Text(
                            text = "Points: ${player.score}".padEnd(maxPointsLength + 8, ' '),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        Text(
                            text = "Points: ${player.score}".padEnd(maxPointsLength + 8, ' '),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }


                }
                if(index < players.size - 1) {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
                Text("Waiting for: ", style = MaterialTheme.typography.displayMedium)
                MediumSpacer()
                Text(
                    state.session.players.filter { it.state == PlayerStateEnum.PLAYING }.map { it.name }.joinToString(", "),
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
                                .fillMaxWidth(),
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
                    onAction(ResultAction.OnNextRound)
                }
            ) {
                Text("Continue")
            }
        }

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