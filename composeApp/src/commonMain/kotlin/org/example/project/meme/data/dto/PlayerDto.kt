package org.example.project.meme.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDto(
    val id: String,
    val connectionId: String,
    val name: String,
    val isUsingMobileDevice: Boolean,
    val score: Int = 0,
    val state: PlayerStateEnum = PlayerStateEnum.LOBBY
)
