package com.example.freshworktest.presenter.database

import androidx.room.TypeConverter
import com.example.freshworktest.entity.Gifsroom
import com.example.freshworktest.model.Gifsmodel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

class ListConverter {

    var gson = Gson()

    @TypeConverter
    fun fromTimestamp(data: String?): List<Gifsmodel.Data>? {

        if (data == null){
            return Collections.emptyList()
        }
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return gson.fromJson(data, listType)


    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Gifsmodel.Data>?): String? {
        return gson.toJson(someObjects)
    }

}
