package org.example.project.meme.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GameStateEnum {
    @SerialName("0")
    LOBBY,
    @SerialName("1")
    PLAY,
    @SerialName("2")
    VOTE,
    @SerialName("3")
    ROUND_END,
    @SerialName("4")
    GAME_END,
    @SerialName("5")
    UNKNOWN,
    @SerialName("6")
    VOTE_CHAR,
    @SerialName("7")
    ROUND_END_CHAR
}