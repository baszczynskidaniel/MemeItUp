package org.example.project.app

import kotlinx.serialization.Serializable


sealed interface Route {

    @Serializable
    data object Menu: Route

    @Serializable
    data object Lobby: Route

    @Serializable
    data object Vote: Route

    @Serializable
    data object RoundEnd: Route

    @Serializable
    data object GameEnd: Route

    @Serializable
    data object SetGame: Route

    @Serializable
    data object CreateMeme: Route

    @Serializable
    data object LobbyName: Route

    @Serializable
    data class LocalGame(val players: String): Route

    @Serializable
    data object Language: Route

    @Serializable
    data object AppGraph: Route

    @Serializable
    data object Settings: Route

    @Serializable
    data object MakeMeme: Route

    @Serializable
    data object SelectMemePicture: Route

    @Serializable
    data object SelectMeme: Route
}