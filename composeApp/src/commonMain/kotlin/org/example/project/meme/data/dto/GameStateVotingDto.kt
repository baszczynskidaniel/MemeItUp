package org.example.project.meme.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameStateVotingDto(
    val numberOfRounds: Int,
    val round: Int,
    val players: List<PlayerDto>,
    val gameStateEnum: GameStateEnum,
) {
    companion object {
        fun default() = GameStateVotingDto(
            0,
            0,
            emptyList(),
            gameStateEnum = GameStateEnum.LOBBY
        )
    }
}
