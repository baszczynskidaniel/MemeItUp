package org.example.project.meme.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LobbyDto(
    val players: List<PlayerDto> = emptyList(),
    val rules: RulesDto,
    val host: PlayerDto,
)
