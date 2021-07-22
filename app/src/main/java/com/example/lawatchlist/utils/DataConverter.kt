package com.example.lawatchlist.utils

import androidx.room.TypeConverter
import com.example.lawatchlist.model.Image
import com.google.gson.Gson


class DataConverter {

    @TypeConverter
    fun fromImage(item: Image?): String? {
        val mediumUrl = item?.posterPath
        val orginalUrl = item?.backdropPath
        if (mediumUrl != null && orginalUrl != null) {
            return "$mediumUrl,$orginalUrl"
        }
        return null
    }

    @TypeConverter
    fun toImage(item: String?): Image {
        return Image(item?.substringBefore(","), item?.substringAfter(","))

    }
    @TypeConverter
    fun listToJsonString(value: List<Int>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String): List<Int> = Gson().fromJson(value, Array<Int>::class.java).toList()

}
