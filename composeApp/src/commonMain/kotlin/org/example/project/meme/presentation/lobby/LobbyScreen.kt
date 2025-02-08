package org.example.project.meme.presentation.lobby

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.lepicekmichal.signalrkore.HubConnectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import memeitup.composeapp.generated.resources.Res
import memeitup.composeapp.generated.resources.laptop
import memeitup.composeapp.generated.resources.smartphone
import org.example.project.core.domain.UserDataRepository
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.meme.data.dto.GameMode
import org.example.project.meme.data.dto.GameStateEnum
import org.example.project.meme.data.dto.LobbyDto
import org.example.project.meme.data.dto.PlayerDto
import org.example.project.meme.data.dto.RulesDto
import org.example.project.meme.data.mappers.toMeme
import org.example.project.meme.data.network.RemoteLobbyDataSource
import org.example.project.meme.domain.GameSession
import org.jetbrains.compose.resources.vectorResource

@Composable
fun LobbyPlayerItem(
    playerDto: PlayerDto,
    isOnThisDevice: Boolean,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            if(isOnThisDevice) {
                Text(
                    "${playerDto.name} (Me)",
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(playerDto.name)
            }
        },
        leadingContent = {
            Icon(
                imageVector = if(playerDto.isUsingMobileDevice) {
                    vectorResource(Res.drawable.smartphone)
                } else vectorResource(Res.drawable.laptop),
                contentDescription = null
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyScreen(
    state: LobbyState,
    onAction: (LobbyAction) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onGameStateChange: (GameStateEnum) -> Unit,
    onDisconnect: () -> Unit,
) {
    LaunchedEffect(state.session?.gameState) {
        if(state.session != null && state.session.gameState == GameStateEnum.PLAY) {
            onGameStateChange(state.session!!.gameState)
        }

    }

    LaunchedEffect(state.session?.connectionState) {
        if(state.session?.connectionState == HubConnectionState.DISCONNECTED) {
            onDisconnect()
        }
    }


    MIUCenterSurface(
        title = {
            Text("Lobby")
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onAction(LobbyAction.OnBack)
                    onBack()
                }
            ) {
                Icon(AppIcons.BACK, null)
            }
        }
    ) {

        state.lobby?.players?.forEach { player ->

            LobbyPlayerItem(
                player,
                isOnThisDevice = state.session?.player?.connectionId == player.connectionId,
                modifier = modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth()
            )
        }
        Button(
            modifier = modifier
                .widthIn(max =LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
            onClick = {
                onAction(LobbyAction.OnStartGame)
            }
        ) {
            Text("Start game")
        }

    }
}

data class LobbyState(
    val lobby: LobbyDto? = null,

    val session: GameSession? = null,
    val isLoading: Boolean = true,
)

sealed class LobbyAction {
    data object OnStartGame: LobbyAction()
    data object OnBack: LobbyAction()
}

class LobbyViewModel(
    val dataSource: RemoteLobbyDataSource,
): ViewModel() {

    private val _state = MutableStateFlow(LobbyState())
    private val _session = dataSource.getGameSession()
    private val _lobby = dataSource.getLobbyStream()

    val state = combine(_state, _lobby, _session) { state, lobby, session ->
        state.copy(
            lobby = lobby,
            session = session,
            isLoading = false,
        )
    }
    .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _state.value
    )

    fun onAction(action: LobbyAction) {
        when(action) {
            LobbyAction.OnBack -> {
                viewModelScope.launch {
                    dataSource.close()
                }
            }

            LobbyAction.OnStartGame -> {
                viewModelScope.launch {
                    dataSource.startGame()
                }
            }
        }
    }
}