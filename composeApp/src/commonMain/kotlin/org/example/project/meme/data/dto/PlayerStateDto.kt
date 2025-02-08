package org.example.project.meme.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PlayerStateEnum {
    @SerialName("0")
    LOBBY,
    @SerialName("1")
    PLAYING,
    @SerialName("2")
    WAITING,
    @SerialName("3")
    DISCONNECTED,
}

