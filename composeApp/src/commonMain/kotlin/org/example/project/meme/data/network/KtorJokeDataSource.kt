package org.example.project.meme.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.example.project.core.data.network.safeCall
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result
import org.example.project.meme.data.dto.JokeDto

private const val BASE_URL: String = "https://v2.jokeapi.dev/joke/Programming?type=single"

class KtorJokeDataSource(
    private val client: HttpClient,
): RemoteJokeDataSource {
    override suspend fun getJoke(): Result<JokeDto, DataError.Remote> {
        return safeCall {
            client.get(BASE_URL)
        }
    }
}