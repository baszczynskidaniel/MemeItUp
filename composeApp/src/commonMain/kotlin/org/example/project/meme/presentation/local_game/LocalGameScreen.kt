package org.example.project.meme.presentation.local_game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.app.Route
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.design_system.MediumSpacer
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.meme.domain.Meme
import org.example.project.meme.domain.MemeTextContent
import org.example.project.meme.presentation.components.MemeImageWithTexts
import org.example.project.meme.presentation.set_game.SetGameAction
import kotlin.math.round

@Composable
fun LocalGameScreen(
    state: LocalGameState,
    onAction: (LocalGameAction) -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(state.allPlayersFinished) {
        onFinish()
    }
    MIUCenterSurface(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(state.waitingForPlayer) {
            Column(
                modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Turn of ${state.currentPlayer.name}",
                    style = MaterialTheme.typography.displayLarge
                )
                MediumSpacer()
                FilledTonalIconButton(
                    onClick = {
                        onAction(LocalGameAction.ConfirmPlayer)
                    },
                    modifier = Modifier.size(LocalDimensions.current.bigIconButton)
                ) {
                    Icon(Icons.Default.ArrowForward, null)
                }

            }
        } else {

            Text(
                "Round 1 / ${state.round}",
                style = MaterialTheme.typography.titleLarge
            )

            MemeImageWithTexts(
                modifier = Modifier
                    .heightIn(max = 400.dp)
                    //  .align(Alignment.CenterHorizontally)
                    //.fillMaxWidth(),
                        ,
                meme = state.currentPlayer.meme!!
            )

            state.currentPlayer.meme.content.forEachIndexed { index, value ->
                TextField(
                    modifier = Modifier
                        .widthIn(max = LocalDimensions.current.maxButtonWidth)
                        .fillMaxWidth(),
                    value = value.text,
                    onValueChange = {
                        onAction(LocalGameAction.OnMemeContentChange(index, it))
                    },
                    placeholder =  {
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
                        if(value.text.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    onAction(LocalGameAction.OnMemeClear(index))

                                }
                            ) {
                                Icon(AppIcons.CLEAR, "remove text")
                            }
                        }
                    },
                )

            }
            Button(
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth()
                    .weight(1f, false),
                onClick = {
                    onAction(LocalGameAction.OnConfirmMeme)
                }
            ) {
                Text("Confirm")
            }

        }
    }
}


class LocalGameViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private var jsonPlayers: List<String> = Json.decodeFromString(savedStateHandle.toRoute<Route.LocalGame>().players)
    private var players = jsonPlayers.mapIndexed { index, it ->
        Player(
            index = index,
            name = ""
        )
    }
    private val _state = MutableStateFlow<LocalGameState>(LocalGameState(
        currentPlayer = players.first()
    ))
    val state = _state
        .onStart {

        }
        .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _state.value
    )

    fun onAction(action: LocalGameAction) {

    }
}

data class LocalGameState(

    val round: Int = 1,
    val currentPlayer: Player,
    val waitingForPlayer: Boolean = true,
    val allPlayersFinished: Boolean = false,

)

data class Player(
    val index: Int,
    val name: String,
    val points: Int = 0,
    val meme: Meme? = null

)

sealed interface LocalGameAction {
    data object ConfirmPlayer: LocalGameAction
    data class OnMemeContentChange(val index: Int, val contentChange: String): LocalGameAction
    data class OnMemeClear(val index: Int): LocalGameAction
    data object OnConfirmMeme: LocalGameAction
    data class OnPlayersChange(val players: String): LocalGameAction
}