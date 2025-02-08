package org.example.project.meme.data.mappers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.example.project.meme.data.dto.MemeTemplateDto
import org.example.project.meme.data.dto.TextPositionsDto
import org.example.project.meme.domain.Meme
import org.example.project.meme.domain.MemeTextContent

fun MemeTemplateDto.toMeme(): Meme {
    return Meme(
        id = id!!,
        imageUrl = imageUrl,
        content = textPositions.map { it.toMemeTextContent() }.sortedWith(
            compareBy<MemeTextContent> { it.left }.thenBy { it.top }
        ).toSnapshotStateList()
    )


}

fun TextPositionsDto.toMemeTextContent(): MemeTextContent {
    return MemeTextContent(
        text = text,
        top = top,
        left = left,
        right = right,
        bottom = bottom,
        id = id ?: "",
        memeTemplateId = memeTemplateId ?: ""
    )
}

fun MemeTextContent.toTextPositionsDto(): TextPositionsDto {
    return TextPositionsDto(
        memeTemplateId = memeTemplateId,
        text = text,
        top = top,
        left = left,
        right = right,
        bottom = bottom,
        id = id
    )
}

inline fun <reified T> List<T>.toSnapshotStateList(): SnapshotStateList<T> {
    return mutableStateListOf(*this.toTypedArray())
}

fun Meme.toMemeTemplateDto(): MemeTemplateDto {
    return MemeTemplateDto(
        id = id,
        imageUrl = imageUrl,
        textPositions = content.toList().map { it.toTextPositionsDto() }
    )
}