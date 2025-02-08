package org.example.project.meme.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MemeToVoteDto(
    val meme: MemeTemplateDto,
    val memeId: String,
)
