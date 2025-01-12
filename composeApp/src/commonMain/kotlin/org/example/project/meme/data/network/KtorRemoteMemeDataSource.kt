package org.example.project.meme.data.network

import io.ktor.client.HttpClient

private const val BASE_URL = "URL"

/***
 * TODO
 * This class fill be responsible for http calls, do when api will be ready
 */

class KtorRemoteMemeDataSource(
    private val httpClient: HttpClient
): RemoteMemeDataSource {

}