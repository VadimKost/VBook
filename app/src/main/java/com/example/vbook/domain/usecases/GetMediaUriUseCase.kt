package com.example.vbook.domain.usecases

import com.example.vbook.ThrowableResult
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.repository.BookRepository
import com.example.vbook.domain.repository.MediaUriRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetMediaUriUseCase @Inject constructor(
    val mediaUriRepository: MediaUriRepository
) {
    suspend operator fun invoke(uri: String): Result<String> {
        return try {
            mediaUriRepository.getMediaUri(uri)
        }catch (e:Throwable){
            ThrowableResult(e,"GetMediaUri")
        }
    }
}