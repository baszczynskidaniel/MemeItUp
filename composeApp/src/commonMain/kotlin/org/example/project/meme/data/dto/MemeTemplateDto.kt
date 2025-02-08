package org.example.project.meme.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemeTemplateDto(
    @SerialName("id") val id: String?,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("textPositions") val textPositions: List<TextPositionsDto>
)


@Serializable
data class TextPositionsDto(
    @SerialName("id") val id: String?,
    @SerialName("top") val top: Float,
    @SerialName("bottom") val bottom: Float,
    @SerialName("left") val left: Float,
    @SerialName("right") val right: Float,
    @SerialName("text") val text: String,
    @SerialName("memeTemplateId") val memeTemplateId: String?,
)

