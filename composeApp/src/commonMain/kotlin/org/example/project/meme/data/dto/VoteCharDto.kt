package org.example.project.meme.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class VoteCharDto(
    val memesToVote: List<MemeInGameDto>,
    val currentChar: PlayerDto,
)
