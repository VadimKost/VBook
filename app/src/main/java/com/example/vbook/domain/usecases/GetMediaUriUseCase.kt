package com.example.vbook.domain.usecases

import com.example.vbook.ThrowableResult
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.repository.MediaDownloadsRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetMediaUriUseCase @Inject constructor(
    val mediaDownloadsRepository: MediaDownloadsRepository
) {
    suspend operator fun invoke(uri: String): Result<String> {
        return try {
            mediaDownloadsRepository.getMediaUri(uri)
        }catch (e:Throwable){
            ThrowableResult(e,"GetMediaUri")
        }
    }
}