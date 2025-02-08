package org.example.project.meme.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MemeInGameDto(
    val memeInGameId: String,
    val meme: MemeTemplateDto,
    val score: Int,
    val round: Int,
    val author: PlayerDto,
)
