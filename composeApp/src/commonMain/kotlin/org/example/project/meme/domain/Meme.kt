package org.example.project.meme.domain

import androidx.compose.runtime.snapshots.SnapshotStateList

data class Meme(
    val id: String = "",
    val imageUrl: String = "",
    val content: SnapshotStateList<MemeTextContent> = SnapshotStateList()
)

data class MemeTextContent(
    val text: String,
    val id: String,
    val top: Float,
    val bottom: Float,
    val left: Float,
    val right: Float,
    val memeTemplateId: String,
)



