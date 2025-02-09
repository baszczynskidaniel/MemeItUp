package org.example.project.meme.presentation.create_meme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import org.example.project.core.presentation.design_system.MIUCard
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.design_system.MediumSpacer
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.meme.data.dto.GameStateEnum
import org.example.project.meme.data.dto.GameStatePlayingDto
import org.example.project.meme.data.dto.PlayerDto
import org.example.project.meme.data.dto.PlayerStateEnum
import org.example.project.meme.data.mappers.toMeme
import org.example.project.meme.data.mappers.toMemeTemplateDto
import org.example.project.meme.data.network.KtorRemoteLobbyDataSource
import org.example.project.meme.domain.GameSession
import org.example.project.meme.domain.Meme
import org.example.project.meme.presentation.components.MemeImageWithTexts

@Composable
fun PlayerItem(
    player: PlayerDto,
    isOnThisDevice: Boolean,
    modifier: Modifier = Modifier
) {
    val containerColor = if(isOnThisDevice)
        MaterialTheme.colorScheme.tertiaryContainer
    else
        MaterialTheme.colorScheme.surfaceContainer

    val contentColor = if(isOnThisDevice)
        MaterialTheme.colorScheme.onTertiaryContainer
    else
        MaterialTheme.colorScheme.onSurface

        Box(
            modifier = modifier
                .padding(LocalDimensions.current.smallPadding)
                .width(
                    width = LocalDimensions.current.smallItemWidth,
                )
                .clip(RoundedCornerShape(LocalDimensions.current.mediumClip))
                .background(containerColor)
                .padding(LocalDimensions.current.mediumPadding),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.smallPadding)
            ) {
                Text(
                    text = player.name,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,

                    )
                Text(
                    text = player.score.toString(),
                    color = contentColor,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,

                    )
            }
        }

        Box(
            modifier = Modifier
                .offset(
                    -LocalDimensions.current.smallPadding - LocalDimensions.current.smallIcon / 2,
                    //LocalDimensions.current.smallIcon / 2
                ),
        ) {
            if (player.state == PlayerStateEnum.WAITING) {

                Icon(
                    imageVector = AppIcons.DONE,
                    modifier = Modifier
                         .size(LocalDimensions.current.smallIcon)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),


                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )


            }
        }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMemeScreen(
    state: CreateMemeState,
    onAction: (CreateMemeAction) -> Unit,
    modifier: Modifier = Modifier,
    onGameStateChange: (GameStateEnum) -> Unit,
    onDisconnect: () -> Unit
) {
    LaunchedEffect(state.session?.gameState) {
        if(state.session?.gameState == GameStateEnum.VOTE) {
            onGameStateChange(state.session.gameState)
        }
    }

    LaunchedEffect(state.session?.connectionState) {
        if(state.session?.connectionState == HubConnectionState.DISCONNECTED) {
            onDisconnect()
        }
    }

    MIUCenterSurface(
        //modifier = modifier.blur(if(state.playerStateEnum != PlayerStateEnum.PLAYING) 16.dp else 0.dp),
        title = {
            Text("Round ${state.session?.round}/${state.session?.numberOfRounds}")
        }
    ) {

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        else  if(state.session?.player?.state == PlayerStateEnum.WAITING) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),

                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalArrangement = Arrangement.Center,
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
        }
        else {
            MIUCard(
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,

                    ) {
                    state.session?.players?.forEach { player ->
                        PlayerItem(
                            player = player,
                            isOnThisDevice = player.connectionId == state.session!!.player.connectionId,
                            // isOnThisDevice = player.connectionId == state.gameState.callerConnectionId
                        )
                    }
                }
            }
            MIUCard(
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth(),
            ) {
                MemeImageWithTexts(
                    modifier = Modifier
                        .heightIn(max = 400.dp),
                    meme = state.meme
                )
            }
            MIUCard(
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth(),
            ) {
                state.meme.content.forEachIndexed { index, memeTextContent ->
                    TextField(
                        modifier = Modifier
                            .widthIn(max = LocalDimensions.current.maxButtonWidth)
                            .fillMaxWidth(),
                        value = memeTextContent.text,
                        onValueChange = {
                            onAction(CreateMemeAction.OnMemeContentChange(index, it))
                        },
                        placeholder = {
                            Text(
                                "Text ${index + 1}"
                            )
                        },
                        label = {
                            Text(
                                "Text ${index + 1}"
                            )
                        },
                        trailingIcon = {
                            if (memeTextContent.text.isNotBlank()) {
                                IconButton(
                                    onClick = {
                                        onAction(CreateMemeAction.OnMemeClear(index))

                                    }
                                ) {
                                    Icon(AppIcons.CLEAR, "remove text")
                                }
                            }
                        },
                    )
                }
            }
            Button(
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth(),
                onClick = {
                    onAction(CreateMemeAction.OnConfirmMeme)
                }
            ) {
                Text("Confirm")
            }
        }
    }




}

data class CreateMemeState(
    val isLoading: Boolean = true,
    val meme: Meme = Meme(),
    val session: GameSession? = null,
)

sealed interface CreateMemeAction {
    data object OnConfirmMeme: CreateMemeAction
    data class OnMemeContentChange(val index: Int, val contentChange: String): CreateMemeAction
    data class OnMemeClear(val index: Int): CreateMemeAction
}

class CreateMemeViewModel(
    private val dataSource: KtorRemoteLobbyDataSource
): ViewModel() {
    private val _gameState = dataSource.receiveGameStatePlaying()

    private val _state = MutableStateFlow(CreateMemeState())
    private val _session = dataSource.getGameSession()


    val state = combine(_state, _session,) { state, session ->

        state.copy(


            session = session,

        )
    }.onStart {
        viewModelScope.launch {
            var meme = dataSource.getMeme()
            _state.update { it.copy(
                meme = meme.toMeme(),
                isLoading = false,
            ) }
        }




    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: CreateMemeAction) {
        when(action) {
            CreateMemeAction.OnConfirmMeme -> {
                viewModelScope.launch {
                    dataSource.postMeme(state.value.meme.toMemeTemplateDto())
                }
            }
            is CreateMemeAction.OnMemeClear -> {
                _state.value.meme.content[action.index] =  _state.value.meme.content[action.index].copy(text = "")
            }
            is CreateMemeAction.OnMemeContentChange -> {

                _state.value.meme.content[action.index] =  _state.value.meme.content[action.index].copy(text = action.contentChange)

            }
        }
    }
}