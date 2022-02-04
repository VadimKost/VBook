package com.example.vbook.data.db

import androidx.room.TypeConverter
import com.example.vbook.data.db.model.MediaItemDownloadEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TypeConvertor{
    @Inject
    lateinit var gson:Gson

    @TypeConverter
    fun fromMediaItemDownloadList(downloadList: List<MediaItemDownloadEntity>):String{
        return gson.toJson(downloadList)
    }

    @TypeConverter
    fun toMediaItemDownloadList(string: String):List<MediaItemDownloadEntity>{
        val type = object :TypeToken<List<MediaItemDownloadEntity>>(){}.type
        return gson.fromJson(string,type)
    }

    @TypeConverter
    fun fromPair(pair: Pair<String,String>): String {
        return pair.first+"^"+pair.second
    }

    @TypeConverter
    fun toPair(pair: String): Pair<String,String> {
        val (first,second) = pair.split("^")
        return first to second
    }

    @TypeConverter
    fun fromPairList(pair: List<Pair<String,String>>?): String? {
        return pair?.asSequence()
            ?.map { fromPair(it) }
            ?.joinToString(separator = "$")
    }

    @TypeConverter
    fun toPairList(pair: String?): List<Pair<String,String>>? {
        return pair?.split("$")?.asSequence()
            ?.map{toPair(it)}
            ?.toList()
    }
}