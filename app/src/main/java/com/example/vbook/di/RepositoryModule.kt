package com.example.vbook.di

import com.example.vbook.data.InMemoryStorageImpl
import com.example.vbook.data.parsers.KnigaVUheParser
import com.example.vbook.data.repository.BookRepositoryImpl
import com.example.vbook.domain.common.InMemoryStorage
import com.example.vbook.domain.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideBookRepository(
        inMemoryStorage: InMemoryStorage,
        knigaVUheParser: KnigaVUheParser
    ): BookRepository = BookRepositoryImpl(inMemoryStorage, knigaVUheParser)
}