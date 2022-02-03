package com.example.vbook.data.di

import com.example.vbook.data.repository.BookRepositoryImpl
import com.example.vbook.data.repository.MediaUriRepositoryImpl
import com.example.vbook.domain.repository.BookRepository
import com.example.vbook.domain.repository.MediaUriRepository
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
    fun provideBookRepository(impl: BookRepositoryImpl):BookRepository = impl

    @Provides
    @Singleton
    fun provideMediaUriRepository(impl: MediaUriRepositoryImpl):MediaUriRepository = impl
}