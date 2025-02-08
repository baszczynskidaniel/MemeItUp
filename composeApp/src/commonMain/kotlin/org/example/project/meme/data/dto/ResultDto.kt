package org.example.project.meme.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResultDto(
    val memes: List<MemeInGameDto>,
    val round: Int,
    val numberOfRounds: Int,

)
