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
import org.example.project.meme.presentation.local_game.LocalGameAction
import org.example.project.meme.presentation.local_game.LocalGameScreen
import org.example.project.meme.presentation.local_game.LocalGameViewModel
import org.example.project.meme.presentation.local_game.Player
import org.example.project.meme.presentation.menu.MenuScreen
import org.example.project.meme.presentation.navigation_view_models.PlayersNavigationViewModel
import org.example.project.meme.presentation.set_game.SetGameScreen
import org.example.project.meme.presentation.set_game.SetGameState
import org.example.project.meme.presentation.set_game.SetGameViewModel
import org.example.project.settings.presentation.language.LanguageScreen
import org.example.project.settings.presentation.language.LanguageState
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
                            onMultiplayer = {},
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