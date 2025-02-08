package org.example.project.meme.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RulesDto(
    val numberOfRounds: Int,
    val sameMemeForEveryone: Boolean,
    val everyoneIsTheJudge: Boolean,
    val gameMode: GameMode
)

@Serializable
enum class GameMode
{
    @SerialName("0")
    CARDS,
    @SerialName("1")
    FILL_IN_BLANK
}
