package com.example.vbook.data.db.dao

import androidx.room.*
import com.example.vbook.data.db.TypeConvertor
import com.example.vbook.data.db.model.DownloadingItemEntity

@Dao
@TypeConverters(TypeConvertor::class)
interface MediaItemDownloadDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(downloadEntity: DownloadingItemEntity)

    @Update
    suspend fun update(downloadEntity: DownloadingItemEntity):Int

    @Query("SELECT * FROM MediaItemDownloadEntity")
    suspend fun getAllDownloads(): List<DownloadingItemEntity>

    @Query("SELECT * FROM MediaItemDownloadEntity WHERE mediaUri =:uri")
    suspend fun getDownloadByUri(uri:String): DownloadingItemEntity

    @Query("SELECT * FROM MediaItemDownloadEntity WHERE bookUrl =:url")
    suspend fun getDownloadsByBookUri(url:String): List<DownloadingItemEntity>

    @Query("SELECT * FROM MediaItemDownloadEntity WHERE mediaUri =:downloadId")
    suspend fun getDownloadsByDownloadId(downloadId:Long): DownloadingItemEntity?
}