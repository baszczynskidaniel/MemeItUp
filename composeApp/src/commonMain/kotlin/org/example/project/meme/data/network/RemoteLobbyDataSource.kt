package org.example.project.meme.data.network

import eu.lepicekmichal.signalrkore.UUID
import kotlinx.coroutines.flow.Flow
import org.example.project.core.domain.DataError
import org.example.project.core.domain.EmptyResult
import org.example.project.core.domain.Result
import org.example.project.meme.data.dto.GameStatePlayingDto
import org.example.project.meme.data.dto.GameStateVotingDto
import org.example.project.meme.data.dto.LobbyDto
import org.example.project.meme.data.dto.MemeInGameDto
import org.example.project.meme.data.dto.MemeTemplateDto
import org.example.project.meme.data.dto.PlayerDto
import org.example.project.meme.data.dto.PlayerStateEnum
import org.example.project.meme.data.dto.PlayersDto
import org.example.project.meme.data.dto.ResultDto
import org.example.project.meme.domain.GameSession

interface RemoteLobbyDataSource {
    fun getLobbyStream(): Flow<LobbyDto>
    suspend fun close()
    suspend fun joinLobby(playerDto: PlayerDto)

    suspend fun broadcastPlayers()
    suspend fun startGame()

    fun receivePlayerStateEnum(): Flow<PlayerStateEnum>
    fun receiveGameStatePlaying(): Flow<GameStatePlayingDto>
    suspend fun postMeme(meme: MemeTemplateDto)
    suspend fun getMemeTemplate(): Result<MemeTemplateDto, DataError.Remote>
    fun receiveGameStateVotingDto(): Flow<GameStateVotingDto>
    fun getMemeVotingStream(): Flow<MemeInGameDto>
    suspend fun vote(memeInGame: String, score: Int)
    fun getGameSession(): Flow<GameSession>

    fun getRoundResult(): Flow<ResultDto>
    fun getPlayers(): Flow<PlayersDto>
    fun finishReviewResult()
}