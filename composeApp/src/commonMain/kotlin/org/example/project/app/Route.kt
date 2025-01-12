package org.example.project.app

import kotlinx.serialization.Serializable


sealed interface Route {

    @Serializable
    data object Menu: Route

    @Serializable
    data object SetGame: Route

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