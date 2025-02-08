package org.example.project.meme.data.network

import eu.lepicekmichal.signalrkore.HubConnectionBuilder
import eu.lepicekmichal.signalrkore.HubConnectionState
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.example.project.core.data.network.safeCall
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result
import org.example.project.core.domain.UserDataRepository
import org.example.project.core.domain.onSuccess
import org.example.project.meme.data.dto.GameSessionDto
import org.example.project.meme.data.dto.GameStateEnum
import org.example.project.meme.data.dto.GameStatePlayingDto
import org.example.project.meme.data.dto.LobbyDto
import org.example.project.meme.data.dto.MemeTemplateDto
import org.example.project.meme.data.dto.PlayerDto
import org.example.project.meme.data.dto.PlayerStateEnum
import org.example.project.meme.data.dto.GameStateVotingDto
import org.example.project.meme.data.dto.MemeInGameDto
import org.example.project.meme.data.dto.PlayersDto
import org.example.project.meme.data.dto.ResultDto
import org.example.project.meme.domain.GameSession

private const val BASE_URL: String = "https://localhost:7206"

object HubMessages {
    const val RECEIVE_GAME_SESSION: String = "ReceiveGameSession"

}

object HubFunctions {
    const val SEND_GAME_SESSION_TO_CALLER: String = "SendGameSessionToCaller"
}

class KtorRemoteLobbyDataSource(
    private val client: HttpClient,
    private val userDataRepository: UserDataRepository
): RemoteLobbyDataSource {

    private var connection = HubConnectionBuilder
        .create("$BASE_URL/lobbyHub") {
            httpClient = client
            json = Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            }
        }



    override fun getGameSession(): Flow<GameSession>
    {
        val gameSessionFlow = flow {
            connection.on(HubMessages.RECEIVE_GAME_SESSION, paramType1 = GameSessionDto::class)
                .onStart {connection.send(HubFunctions.SEND_GAME_SESSION_TO_CALLER)}
                .collect { state ->
                    emit(state.arg1)
                }
        }
        val connectionStateFlow = connection.connectionState
        return combine(gameSessionFlow, connectionStateFlow) { gameSession, connectionState ->
            GameSession(
                player = gameSession.player,
                gameState = gameSession.gameState,
                connectionState = connectionState,
            )
        }
        .onStart {
            emit(GameSession(PlayerDto(
                "",
                "",
                "",
                false,
                0,
                PlayerStateEnum.LOBBY,

            ), GameStateEnum.LOBBY, connectionStateFlow.value))
        }
    }

    override fun getLobbyStream(): Flow<LobbyDto> {

        if(connection.connectionState.value != HubConnectionState.CONNECTED) {
            runBlocking {
                connection.start()
            }
        }
        return flow {
            connection.on("ReceiveLobby", paramType1 = LobbyDto::class).collect { lobbyDto ->
                emit(lobbyDto.arg1)
            }
        }.onStart {
            connection.send("sendLobby")
        }
    }

    override suspend fun close() {
        connection.send("LeaveLobby")
        connection.stop()
    }

    override suspend fun joinLobby(playerDto: PlayerDto) {
        if(connection.connectionState.value != HubConnectionState.CONNECTED) {
            runBlocking {
                connection.start()
            }
        }
        connection.send("JoinLobby", playerDto.name, playerDto.isUsingMobileDevice)
    }


    override fun receivePlayerStateEnum(): Flow<PlayerStateEnum> {
        return flow {
            CoroutineScope(Dispatchers.IO).launch {
                connection.on("ReceivePlayerState", paramType1 = PlayerStateEnum::class)
                    .collect { state ->
                        emit(state.arg1)
                    }
            }
            CoroutineScope(Dispatchers.IO).launch {
                emit(PlayerStateEnum.LOBBY)
            }
        }
    }

    override fun receiveGameStatePlaying(): Flow<GameStatePlayingDto> {
        return flow {

            connection.on("BroadcastGameStatePlaying", paramType1 = GameStatePlayingDto::class)
                .onStart {
                    connection.send("BroadcastGameStatePlaying")

                }
                .collect { state ->
                    emit(state.arg1)
                }
        }
    }

    override fun getRoundResult(): Flow<ResultDto>
    {
        return flow {
            connection.on("ReceiveRoundResult", paramType1 = ResultDto::class)
                .onStart {

                    connection.send("SendRoundResult")
                }
                .collect { state ->
                    println("xddddddduu")
                    emit(state.arg1)
                }
        }
    }
    override fun finishReviewResult()
    {
        connection.send("FinishReviewResult")
    }
    override fun getPlayers(): Flow<PlayersDto>
    {
        return flow {
            connection.on("ReceivePlayers", paramType1 = PlayersDto::class)
                .onStart {
                    connection.send("SendPlayers")
                }
                .collect { state ->
                    println("plaaaaaaaaaaaaayers")
                    emit(state.arg1)
                }
        }
    }

    override fun getMemeVotingStream(): Flow<MemeInGameDto>
    {
        return flow {

            connection.on("ReceiveNextMemeForVoting", paramType1 = MemeInGameDto::class)
                .onStart {
                    connection.send("BroadcastNextMemeForVoting")
                    println("lol")
                }
                .collect { state ->
                    println("lololo")
                    emit(state.arg1)
                }
        }
    }



    override fun receiveGameStateVotingDto(): Flow<GameStateVotingDto> {
        return flow {

            connection.on("BroadcastGameStateVotingDto", paramType1 = GameStateVotingDto::class)
                .onStart {
                    connection.send("BroadcastGameStateVotingDto")
                    println("xd")
                }
                .collect { state ->
                    println("xddd")
                    emit(state.arg1)
                }
        }
    }

    override suspend fun postMeme(meme: MemeTemplateDto) {
        connection.send("PostMeme", meme)
    }

    override suspend fun getMemeTemplate(): Result<MemeTemplateDto, DataError.Remote> {
        return safeCall {
            client.get(urlString = "$BASE_URL/api/MemeTemplate/random")
        }
    }



    override suspend fun broadcastPlayers() {
        connection.start()
        connection.send("BroadcastPlayers")
    }

    override suspend fun startGame() {
        connection.start()
        connection.send("StartGame")
    }

    override suspend fun vote(memeId: String, score: Int)
    {
        println("ust")
        connection.start()
        connection.send("MakeVote", memeId, score)
    }

}