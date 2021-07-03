package com.example.vbook.data.db.model

import androidx.room.TypeConverter

class TypeConvertor {
    @TypeConverter
    fun fromPair(pair: Pair<String,String>): String {
        return pair.first+" "+pair.second
    }

    @TypeConverter
    fun toPair(pair: String): Pair<String,String> {
        val (first,second) = pair.split(" ")
        return first to second
    }

    @TypeConverter
    fun fromPairList(pair: List<Pair<String,String>>?): String? {
        return pair?.asSequence()
            ?.map { it.first+" "+it.second }
            ?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toPairList(pair: String): List<Pair<String,String>> {
        return pair.split(",").asSequence()
            .map{val (first,second)=it.split(" ")
                first to second
            }.toList()
    }
}