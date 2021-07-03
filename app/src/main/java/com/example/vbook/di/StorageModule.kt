package com.example.vbook.di

import android.content.Context
import androidx.room.Room

import com.example.vbook.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context:Context):AppDatabase
    = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "VBookDB"
    ).build()
}