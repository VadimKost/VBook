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

    @Query("SELECT * FROM MediaItemDownloadEntity WHERE mediaUri =:uri")
    suspend fun getDownloadsByUri(uri:String): MediaItemDownloadEntity

    @Query("SELECT * FROM MediaItemDownloadEntity WHERE bookUrl =:url")
    suspend fun getDownloadsByBookUri(url:String): List<MediaItemDownloadEntity>


}