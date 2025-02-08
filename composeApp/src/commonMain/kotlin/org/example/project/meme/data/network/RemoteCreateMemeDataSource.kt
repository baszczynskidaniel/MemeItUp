package org.example.project.meme.data.network

import org.example.project.core.domain.DataError
import org.example.project.core.domain.Result
import org.example.project.meme.data.dto.MemeTemplateDto

interface RemoteCreateMemeDataSource {
    suspend fun getMemeTemplate(): Result<MemeTemplateDto, DataError.Remote>
}