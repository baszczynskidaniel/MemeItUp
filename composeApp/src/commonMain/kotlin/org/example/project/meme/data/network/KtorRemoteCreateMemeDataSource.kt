package org.example.project.meme.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.example.project.core.data.network.safeCall
import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result
import org.example.project.meme.data.dto.MemeTemplateDto

private const val BASE_URL: String = "https://localhost:7206"

class KtorRemoteCreateMemeDataSource(
    private val client: HttpClient
): RemoteCreateMemeDataSource {
    override suspend fun getMemeTemplate(): Result<MemeTemplateDto, DataError.Remote> {
        return safeCall {
            client.get(urlString = "$BASE_URL/api/MemeTemplate/random")
        }
    }
}