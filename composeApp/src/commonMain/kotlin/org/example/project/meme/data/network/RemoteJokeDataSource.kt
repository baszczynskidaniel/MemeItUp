package org.example.project.meme.data.network

import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result
import org.example.project.meme.data.dto.JokeDto

interface RemoteJokeDataSource {
    suspend fun getJoke(): Result<JokeDto, DataError.Remote>
}