package org.example.project.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import org.example.project.core.presentation.ui.AppTheme
import org.example.project.meme.data.dto.GameStateEnum
import org.example.project.meme.presentation.create_meme.CreateMemeScreen
import org.example.project.meme.presentation.create_meme.CreateMemeViewModel
import org.example.project.meme.presentation.lobby.LobbyScreen
import org.example.project.meme.presentation.lobby.LobbyViewModel
import org.example.project.meme.presentation.lobby_name.LobbyNameScreen
import org.example.project.meme.presentation.lobby_name.LobbyNameViewModel
import org.example.project.meme.presentation.local_game.LocalGameAction
import org.example.project.meme.presentation.local_game.LocalGameScreen
import org.example.project.meme.presentation.local_game.LocalGameViewModel
import org.example.project.meme.presentation.menu.MenuScreen
import org.example.project.meme.presentation.navigation_view_models.PlayersNavigationViewModel
import org.example.project.meme.presentation.result_char.ResultCharScreen
import org.example.project.meme.presentation.result_char.ResultCharState
import org.example.project.meme.presentation.result_char.ResultCharViewModel
import org.example.project.meme.presentation.round_end.RoundEndScreen
import org.example.project.meme.presentation.round_end.RoundEndViewModel
import org.example.project.meme.presentation.set_game.SetGameScreen
import org.example.project.meme.presentation.set_game.SetGameViewModel
import org.example.project.meme.presentation.vote.VoteScreen
import org.example.project.meme.presentation.vote.VoteViewModel
import org.example.project.meme.presentation.vote_char.VoteCharScreen
import org.example.project.meme.presentation.vote_char.VoteCharState
import org.example.project.meme.presentation.vote_char.VoteCharViewModel
import org.example.project.meme.result.ResultScreen
import org.example.project.meme.result.ResultState
import org.example.project.meme.result.ResultViewModel
import org.example.project.settings.presentation.language.LanguageScreen
import org.example.project.settings.presentation.language.LanguageViewModel
import org.example.project.settings.presentation.settings_list.SettingsScreen
import org.example.project.settings.presentation.settings_list.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


@Composable
@Preview
fun App(

) {

    val appViewModel = koinViewModel<AppViewModel>()
    val appState = appViewModel.state.collectAsState()

    AppTheme(
        language = appState.value.language,
        darkTheme = appState.value.darkTheme,
        dynamicColor = appState.value.dynamicColor

    ) {
        Surface(
            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Route.AppGraph
            ) {
                navigation<Route.AppGraph>(
                    startDestination = Route.Menu
                ) {
                    composable<Route.Menu> {
                        MenuScreen(
                            onSettings = {
                                navController.navigate(Route.Settings)
                            },
                            onLocal = {
                                navController.navigate(Route.SetGame)
                            },
                            onMultiplayer = {
                                navController.navigate(Route.LobbyName)
                            },
                        )
                    }
                    composable<Route.LobbyName> {
                        val viewModel = koinViewModel<LobbyNameViewModel>()
                        val state = viewModel.state.collectAsState()

                        LobbyNameScreen(
                            state = state.value,
                            onAction = viewModel::onAction,
                            onBack = {
                                navController.navigate(Route.Menu) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true}
                                }
                            },
                            onConfirmSuccessful = {
                                navController.navigate(Route.Lobby)
                            }
                        )
                    }


                    composable<Route.Lobby> {
                        val viewModel = koinViewModel<LobbyViewModel>()
                        val state = viewModel.state.collectAsState()
                        LobbyScreen(
                            state = state.value,
                            onAction = viewModel::onAction,
                            onBack = {
                                navController.navigate(Route.LobbyName) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true}
                                }
                            },
                            onGameStateChange = {
                                onGameStateChange(it, navController)
                            },
                            onDisconnect = {
                                onDisconnect(navController)
                            }

                        )
                    }

                    composable<Route.CreateMeme> {
                        val viewModel = koinViewModel<CreateMemeViewModel>()
                        val state = viewModel.state.collectAsStateWithLifecycle()
                        CreateMemeScreen(
                            state = state.value,
                            onAction = viewModel::onAction,
                            onGameStateChange = {
                                onGameStateChange(it, navController)
                            },
                            onDisconnect = {
                                onDisconnect(navController)
                            }
                        )
                    }

                    composable<Route.Vote> {
                        val viewModel = koinViewModel<VoteViewModel>()
                        val state = viewModel.state.collectAsState()
                        VoteScreen(
                            state = state.value,
                            onAction = viewModel::onAction,
                            onGameStateChange = {
                                onGameStateChange(it, navController)
                            },
                            onDisconnect = {
                                onDisconnect(navController)
                            }
                        )
                    }
                    composable<Route.RoundEnd> {
                        val viewModel = koinViewModel<ResultViewModel>()
                        val state = viewModel.state.collectAsState()
                        ResultScreen(
                            state = state.value,
                            onAction = viewModel::onAction,
                            onGameStateChange = {
                                onGameStateChange(it, navController)
                            },
                            onDisconnect = {
                                onDisconnect(navController)
                            }
                        )
                    }





                    composable<Route.Settings> {
                        val viewModel = koinViewModel<SettingsViewModel>()
                        val state = viewModel.state.collectAsState()
                        SettingsScreen(
                            onBack = {
                                navController.navigateUp()
                            },
                            state = state.value,
                            onAction = viewModel::onAction,
                            onLanguage = {
                                navController.navigate(Route.Language)
                            }
                        )

                    }

                    composable<Route.Language> {
                        val viewModel = koinViewModel<LanguageViewModel>()
                        val state = viewModel.state.collectAsState()
                        LanguageScreen(
                            onBack = {
                                navController.navigateUp()
                            },
                            state = state.value,
                            onAction = viewModel::onAction,

                        )
                    }

                    composable<Route.SetGame> {
                        val viewModel = koinViewModel<SetGameViewModel>()
                        val playersViewModel = it.sharedKoinViewModel<PlayersNavigationViewModel>(navController)
                        val state = viewModel.state.collectAsState()

                        LaunchedEffect(true) {
                            playersViewModel.onConfirm(null)
                        }

                        SetGameScreen(
                            onBack = {
                                navController.navigateUp()
                            },
                            state = state.value,
                            onAction = viewModel::onAction,
                            onConfirm = { players ->
                                playersViewModel.onConfirm(players)
                                navController.navigate(Route.LocalGame(players))
                            }
                        )
                    }

                    composable<Route.ResultChar> {
                        val viewModel = koinViewModel<ResultCharViewModel>()
                        val state = viewModel.state.collectAsState()
                        ResultCharScreen(
                            state = state.value,
                            onAction = viewModel::onAction,
                            onGameStateChange = {
                                onGameStateChange(it, navController)
                            },
                            onDisconnect = {
                                onDisconnect(navController)
                            }
                        )
                    }

                    composable<Route.VoteChar> {
                        val viewModel = koinViewModel<VoteCharViewModel>()
                        val state = viewModel.state.collectAsState()
                        VoteCharScreen(
                            state = state.value,
                            onAction = viewModel::onAction,
                            onGameStateChange = {
                                onGameStateChange(it, navController)
                            },
                            onDisconnect = {
                                onDisconnect(navController)
                            }
                        )
                    }

                    composable<Route.LocalGame> {

                        val playersViewModel = it.sharedKoinViewModel<PlayersNavigationViewModel>(navController)
                        val players by playersViewModel.players.collectAsStateWithLifecycle()
                        val viewModel = koinViewModel<LocalGameViewModel>()

                        val state = viewModel.state.collectAsState()



                        LaunchedEffect(players) {
                            players?.let {
                                viewModel.onAction(LocalGameAction.OnPlayersChange(it))
                            }
                        }


                        LocalGameScreen(
                            state = state.value,
                            onAction = viewModel::onAction,
                            onFinish = {}

                        )
                    }
                }
            }
        }
    }
}

private fun onDisconnect(navController: NavController)
{
    navController.navigate(Route.Menu) {
        popUpTo(navController.graph.startDestinationId) { inclusive = true}
    }
}

private fun onGameStateChange(gameState: GameStateEnum, navController: NavController)
{
    when(gameState)
    {



        GameStateEnum.LOBBY -> navController.navigate(Route.Lobby) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true}
        }
        GameStateEnum.PLAY -> navController.navigate(Route.CreateMeme) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true}
        }
        GameStateEnum.VOTE -> navController.navigate(Route.Vote) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true}
        }
        GameStateEnum.ROUND_END -> navController.navigate(Route.RoundEnd) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true}
        }
        GameStateEnum.GAME_END -> {

        }

        GameStateEnum.UNKNOWN -> {

        }
        GameStateEnum.VOTE_CHAR ->  navController.navigate(Route.VoteChar) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true}
        }

        GameStateEnum.ROUND_END_CHAR ->  navController.navigate(Route.ResultChar) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true}
        }
    }
}

@Composable
private inline fun <reified T: ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}