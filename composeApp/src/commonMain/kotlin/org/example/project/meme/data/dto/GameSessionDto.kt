package org.example.project.meme.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameSessionDto(
    val player: PlayerDto,
    val gameState: GameStateEnum,
)
