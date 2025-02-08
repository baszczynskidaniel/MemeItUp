package org.example.project.meme.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlayersDto(
    val players: List<PlayerDto>
)
