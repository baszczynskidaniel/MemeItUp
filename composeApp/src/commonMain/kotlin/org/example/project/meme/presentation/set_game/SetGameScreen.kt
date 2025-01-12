package org.example.project.meme.presentation.set_game


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import memeitup.composeapp.generated.resources.Res
import memeitup.composeapp.generated.resources.navigate_back
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.jetbrains.compose.resources.stringResource


internal const val MIN_NUMBER_OF_PLAYERS = 3
internal const val MAX_NUMBER_OF_PLAYERS = 8


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetGameScreen(
    onBack: () -> Unit,
    state: SetGameState,
    onConfirm: (String) -> Unit,
    onAction: (SetGameAction) -> Unit,
    modifier: Modifier = Modifier,


) {
    MIUCenterSurface(
        title = {
            Text("Set game")
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onBack,
            ) {
                Icon(
                    AppIcons.BACK,
                    stringResource(Res.string.navigate_back)
                )
            }
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.players.forEachIndexed { index, value ->
            TextField(
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth(),
                value = value,
                onValueChange = {
                    onAction(SetGameAction.OnPlayerChange(index, it))
                },
                placeholder =  {
                    Text(
                        "Name"
                    )
                },
                label = {
                    Text(
                        "Player ${index + 1}"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onAction(SetGameAction.OnPlayerRemove(index))

                        }
                    ) {
                        Icon(AppIcons.CLEAR, "remove text")
                    }
                },
            )

        }

        if(MAX_NUMBER_OF_PLAYERS >= state.players.size) {
            OutlinedButton(
                modifier = Modifier
                    .widthIn(max = LocalDimensions.current.maxButtonWidth)
                    .fillMaxWidth(),
                onClick = {
                    onAction(SetGameAction.OnAddPlayer)
                }
            ) {
                Text("Add player", textAlign = TextAlign.Center)
            }
        }
        Button(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth()
                .weight(1f, false),
            enabled = state.players.size >= MIN_NUMBER_OF_PLAYERS && state.players.all { it.isNotBlank() },
            onClick = {

                onConfirm(Json.encodeToString(state.players.toList()))
            }
        ) {


            Text("Start game")

        }
    }
}


class SetGameViewModel: ViewModel() {
    private val _state = MutableStateFlow(SetGameState(mutableStateListOf(
        "daba", "maciej", "adrian"
    )))

    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _state.value
    )

    fun onAction(action: SetGameAction) {
        when(action) {
            is SetGameAction.OnAddPlayer -> {
                _state.value.players.add("")
            }

            is SetGameAction.OnPlayerChange -> {
                _state.value.players[action.index] = action.playerChange
            }
            is SetGameAction.OnPlayerRemove -> {
                _state.value.players.removeAt(action.index)
            }
        }
    }
}

data class SetGameState (
    val players: SnapshotStateList<String> = mutableStateListOf()

)

sealed interface SetGameAction {

    data class OnPlayerChange(val index: Int, val playerChange: String): SetGameAction
    data class OnPlayerRemove(val index: Int): SetGameAction
    data object OnAddPlayer: SetGameAction

}