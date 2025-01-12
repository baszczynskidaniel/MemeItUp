package org.example.project.meme.domain

import androidx.compose.runtime.snapshots.SnapshotStateList

data class Meme(
    val id: String, 
    val imageUrl: String,
    val author: String,
    val content: SnapshotStateList<MemeTextContent>
)

data class MemeTextContent(
    val text: String,
    val top: Float,
    val bottom: Float,
    val left: Float,
    val right: Float,
)



