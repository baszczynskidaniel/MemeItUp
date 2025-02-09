package org.example.project.meme.domain

import eu.lepicekmichal.signalrkore.HubConnection
import eu.lepicekmichal.signalrkore.HubConnectionState
import kotlinx.coroutines.flow.StateFlow
import org.example.project.meme.data.dto.GameStateEnum
import org.example.project.meme.data.dto.PlayerDto

data class GameSession(
    val player: PlayerDto,
    val gameState: GameStateEnum,
    val players: List<PlayerDto>,
    val round: Int,
    val numberOfRounds: Int,
    val connectionState: HubConnectionState
)
