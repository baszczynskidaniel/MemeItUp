package org.example.project.meme.presentation.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.core.presentation.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import memeitup.composeapp.generated.resources.Res
import memeitup.composeapp.generated.resources.language
import memeitup.composeapp.generated.resources.local
import memeitup.composeapp.generated.resources.multiplayer
import memeitup.composeapp.generated.resources.navigate_back
import memeitup.composeapp.generated.resources.settings
import org.example.project.core.domain.DataError
import org.example.project.core.domain.onError
import org.example.project.core.domain.onSuccess
import org.example.project.core.presentation.design_system.BigSpacer
import org.example.project.core.presentation.design_system.MIUCard
import org.example.project.core.presentation.design_system.MIUCenterSurface
import org.example.project.core.presentation.ui.AppIcons
import org.example.project.core.presentation.ui.LocalDimensions
import org.example.project.meme.data.network.KtorJokeDataSource
import org.example.project.meme.domain.Meme
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    state: MenuState,
    onAction: (MenuAction) -> Unit,
    onSettings: () -> Unit,
    onLocal: () -> Unit,
    onMultiplayer: () -> Unit,
    modifier: Modifier = Modifier,
    ) {

    MIUCenterSurface(
        modifier = modifier,
        actions = {
            IconButton(
                onClick = onSettings,
            ) {
                Icon(
                    AppIcons.SETTINGS,
                    stringResource(Res.string.settings)
                )
            }
        },
        title = {},
        verticalArrangement = Arrangement.Center
    ) {
        MIUCard(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Joke",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                FilledTonalIconButton(
                    onClick = {
                        onAction(MenuAction.OnJokeChange)
                    }
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        null,

                        )
                }
            }


                if (state.joke != null) {
                    Text(
                        state.joke,
                        style = MaterialTheme.typography.titleMedium,

                        )


                }


        }
        BigSpacer()
        BigSpacer()
        BigSpacer()

        Button(
            modifier = Modifier
                .widthIn(max = LocalDimensions.current.maxButtonWidth)
                .fillMaxWidth(),
            onClick = onMultiplayer
        ) {
            Text(
                stringResource(Res.string.multiplayer)
            )
        }
//        BigSpacer()
//        OutlinedButton(
//            modifier = Modifier
//                .widthIn(max = LocalDimensions.current.maxButtonWidth)
//                .fillMaxWidth(),
//            onClick = onLocal
//        ) {
//            Text(
//                stringResource(Res.string.local)
//            )
//        }
    }
}

data class MenuState(
    val joke: String? = null,
    val errorMessage: String? = null,
)

class MenuViewModel(
    private val jokeDataSource: KtorJokeDataSource
): ViewModel() {
    private val _state = MutableStateFlow(MenuState())
    val state = _state
        .onStart {
            viewModelScope.launch {
                jokeDataSource.getJoke()
                    .onSuccess { joke ->
                        _state.update { it.copy(
                            joke = joke.joke,
                            errorMessage = null,
                        ) }
                    }
                    .onError { error ->
                        _state.update { it.copy(
                            joke = null,
                            errorMessage = error.name,
                        ) }
                    }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)
    fun onAction(action: MenuAction) {
        when(action) {
            MenuAction.OnJokeChange -> viewModelScope.launch {
                jokeDataSource.getJoke()
                    .onSuccess { joke ->
                        _state.update { it.copy(
                            joke = joke.joke,
                            errorMessage = null,
                        ) }
                    }
                    .onError { error ->
                        _state.update { it.copy(
                            joke = null,
                            errorMessage = error.name,
                        ) }
                    }
            }
        }
    }

}

