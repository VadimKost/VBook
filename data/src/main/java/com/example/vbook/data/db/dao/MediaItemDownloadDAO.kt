///*
// * Created by Vadim on 13.07.22, 12:17
// * Copyright (c) 2022 . All rights reserved.
// * Last modified 09.07.22, 20:35
// *
// */
//
//package com.example.vbook.data.db.dao
//
//import androidx.room.*
//import com.example.vbook.data.db.model.DownloadingItemEntity
//
//@Dao
//@TypeConverters(TypeConvertor::class)
//interface MediaItemDownloadDAO {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(downloadEntity: DownloadingItemEntity)
//
//
//    @Query("SELECT * FROM MediaItemDownloadEntity")
//    suspend fun getAllDownloads(): List<DownloadingItemEntity>
//
//    @Query("SELECT * FROM MediaItemDownloadEntity WHERE onlineUri =:uri")
//    suspend fun getDownloadByOnlineUri(uri:String): DownloadingItemEntity?
//
//    @Query("SELECT * FROM MediaItemDownloadEntity WHERE bookUrl =:url")
//    suspend fun getDownloadsByBookUri(url:String): List<DownloadingItemEntity>
//
//    @Query("SELECT * FROM MediaItemDownloadEntity WHERE downloadId =:downloadId")
//    suspend fun getDownloadsByDownloadId(downloadId:Long): DownloadingItemEntity?
//}