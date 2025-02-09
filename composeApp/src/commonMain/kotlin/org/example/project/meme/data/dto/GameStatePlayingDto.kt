package org.example.project.meme.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameStatePlayingDto(

    val numberOfRounds: Int,
    val round: Int,
    val players: List<PlayerDto>,

)
