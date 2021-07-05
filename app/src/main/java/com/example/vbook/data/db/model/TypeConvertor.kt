package com.example.vbook.data.db.model

import android.util.Log
import androidx.room.TypeConverter

class TypeConvertor {
    @TypeConverter
    fun fromPair(pair: Pair<String,String>): String {
//        Log.e("TypeConverterfromPair",pair.first+","+pair.second)
        return pair.first+","+pair.second
    }

    @TypeConverter
    fun toPair(pair: String): Pair<String,String> {
//        Log.e("TypeConvertertoPair",pair)
        val (first,second) = pair.split(",")
        return first to second
    }

    @TypeConverter
    fun fromPairList(pair: List<Pair<String,String>>?): String? {
//        Log.e("TypeCorfromPairLi",pair.toString())
        return pair?.asSequence()
            ?.map { it.first+"^"+it.second }
            ?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toPairList(pair: String?): List<Pair<String,String>>? {
//        Log.e("TypeConvertertoPairList",pair.toString())
        return pair?.split(",")?.asSequence()
            ?.map{val (first,second)=it.split("^")
                first to second
            }?.toList()
    }
}