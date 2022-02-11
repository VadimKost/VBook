package com.example.vbook.data.db.dao

import androidx.room.*
import com.example.vbook.data.db.TypeConvertor
import com.example.vbook.data.db.model.MediaItemDownloadEntity

@Dao
@TypeConverters(TypeConvertor::class)
interface MediaItemDownloadDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(downloadEntity: MediaItemDownloadEntity)

    @Update
    suspend fun update(downloadEntity: MediaItemDownloadEntity):Int

    @Query("SELECT * FROM MediaItemDownloadEntity")
    suspend fun getAllDownloads(): List<MediaItemDownloadEntity>

    @Query("SELECT * FROM MediaItemDownloadEntity WHERE mediaOnlineUri =:downloadId")
    suspend fun getDownloadsByDownloadId(downloadId:Long): MediaItemDownloadEntity?

    @Query("SELECT * FROM MediaItemDownloadEntity WHERE bookUrl =:url")
    suspend fun getDownloadsByBookUrl(url:String): List<MediaItemDownloadEntity>


}