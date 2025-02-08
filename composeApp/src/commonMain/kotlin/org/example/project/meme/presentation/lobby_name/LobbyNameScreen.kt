package org.example.project.meme.presentation.lobby_name

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import memeitup.composeapp.generated.resources.Res
import memeitup.composeapp.generated.resources.navigate_back
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.design_system.MediumSpacer
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.isDesktop
import org.example.project.meme.data.dto.PlayerDto
import org.example.project.meme.data.dto.PlayerStateEnum
import org.example.project.meme.data.network.KtorRemoteLobbyDataSource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyNameScreen(
    state: LobbyNameState,
    onAction: (LobbyNameAction) -> Unit,
    onBack: () -> Unit,
    onConfirmSuccessful: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(state.confirmSuccessful) {
        if(state.confirmSuccessful) {
            onConfirmSuccessful()
        }
    }

    MIUCenterSurface(
        modifier = modifier,
        title = {
            Text("Join lobby")
        },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
               Icon(AppIcons.BACK, stringResource(Res.string.navigate_back))
            }
        },
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = state.name,
            placeholder = {
                Text("Name")
            },
            modifier = Modifier.widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
            onValueChange = {
                onAction(LobbyNameAction.OnNameChange(it))
            },
            label = {
                Text("Name")
            },
            maxLines = 1,
        )
        MediumSpacer()
        Button(
            modifier = Modifier.widthIn(max = LocalDimensions.current.maxButtonWidth)
            .fillMaxWidth(),
            onClick = {onAction(LobbyNameAction.OnConfirm)}
        ) {
            Text("Join lobby")
        }
    }
}

data class LobbyNameState(
    val confirmSuccessful: Boolean = false,
    val name: String = "",
    val nameError: String? = null,
)

sealed interface LobbyNameAction {
    data class OnNameChange(val nameChange: String): LobbyNameAction
    data object OnConfirm: LobbyNameAction
}

class LobbyNameViewModel(
    private val dataSource: KtorRemoteLobbyDataSource
): ViewModel() {

    private val _state = MutableStateFlow(LobbyNameState())
    val state = _state
        .onStart {  }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: LobbyNameAction) {
        when(action) {
            LobbyNameAction.OnConfirm -> {
                if(_state.value.name.isNotBlank()) {
                    viewModelScope.launch {
                        runBlocking {

                            dataSource.joinLobby(
                                PlayerDto(
                                    connectionId = "",
                                    name = _state.value.name,
                                    isUsingMobileDevice = !isDesktop(),

                                    id = "",
                                    score = 0,
                                    state = PlayerStateEnum.LOBBY,
                                )
                            )
                        }
                        _state.update { it.copy(
                            confirmSuccessful = true,
                        ) }
                    }


                }
            }


            is LobbyNameAction.OnNameChange -> _state.update { it.copy(
                name = action.nameChange
            ) }
        }
    }
}