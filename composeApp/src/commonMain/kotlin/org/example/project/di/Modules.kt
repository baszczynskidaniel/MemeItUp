package org.example.project.di

import org.example.project.app.AppViewModel
import org.example.project.core.data.UserDataRepositoryImpl
import org.example.project.core.domain.UserDataRepository
import org.example.project.core.data.datastore.MIUPreferencesDataSource
import org.example.project.core.data.network.HttpClientFactory
import org.example.project.meme.data.network.KtorRemoteCreateMemeDataSource
import org.example.project.meme.data.network.KtorRemoteLobbyDataSource
import org.example.project.meme.data.network.KtorRemoteMemeDataSource
import org.example.project.meme.data.network.RemoteCreateMemeDataSource
import org.example.project.meme.data.network.RemoteLobbyDataSource
import org.example.project.meme.presentation.create_meme.CreateMemeViewModel
import org.example.project.meme.presentation.lobby.LobbyViewModel
import org.example.project.meme.presentation.lobby_name.LobbyNameViewModel
import org.example.project.meme.presentation.local_game.LocalGameViewModel
import org.example.project.meme.presentation.navigation_view_models.PlayersNavigationViewModel
import org.example.project.meme.presentation.result_char.ResultCharViewModel
import org.example.project.meme.presentation.round_end.RoundEndViewModel
import org.example.project.meme.presentation.set_game.SetGameViewModel
import org.example.project.meme.presentation.vote.VoteState
import org.example.project.meme.presentation.vote.VoteViewModel
import org.example.project.meme.presentation.vote_char.VoteCharViewModel
import org.example.project.meme.result.ResultViewModel
import org.example.project.settings.data.DataStoreFactory
import org.example.project.settings.presentation.language.LanguageViewModel
import org.example.project.settings.presentation.settings_list.SettingsViewModel

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::MIUPreferencesDataSource)
    single {
        get<DataStoreFactory>().create()
    }
    singleOf(::UserDataRepositoryImpl).bind<UserDataRepository>()
    singleOf(::KtorRemoteLobbyDataSource).bind<RemoteLobbyDataSource>()
    singleOf(::KtorRemoteCreateMemeDataSource).bind<RemoteCreateMemeDataSource>()


    viewModelOf(::LobbyViewModel)

    viewModelOf(::LobbyNameViewModel)
    viewModelOf(::AppViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::LanguageViewModel)
    viewModelOf(::SetGameViewModel)
    viewModelOf(::LocalGameViewModel)
    viewModelOf(::ResultViewModel)
    viewModelOf(::PlayersNavigationViewModel)
    viewModelOf(::CreateMemeViewModel)
    viewModelOf(::VoteViewModel)
    viewModelOf(::RoundEndViewModel)

    viewModelOf(::VoteCharViewModel)
    viewModelOf(::ResultCharViewModel)

}