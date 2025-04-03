/*
 * Created by Vadim on 13.07.22, 12:17
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 09.07.22, 20:35
 *
 */

package com.example.vbook.data.di

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
    fun provideDB(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "VBookDB"
    ).build()

}