package com.app.skyss_companion.database

import androidx.room.TypeConverter
import com.app.skyss_companion.model.RouteDirection
import com.app.skyss_companion.model.Stop
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

class Converters {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun stringFromStringArray(value: List<String>?) : String? {
        return if(value != null) {
            val type: Type = Types.newParameterizedType(List::class.java, String::class.java)
            val adapter: JsonAdapter<List<*>> = moshi.adapter(type)
            adapter.toJson(value)
        }
        else null
    }

    @TypeConverter
    fun stringToStringArray(value: String?) : List<String>? {
        return if(value != null) {
            val type: Type = Types.newParameterizedType(List::class.java, String::class.java)
            val adapter: JsonAdapter<List<String>> = moshi.adapter(type)
            adapter.fromJson(value)
        } else null
    }

    @TypeConverter
    fun stringFromRouteDirection(value: List<RouteDirection>?) : String? {
        return if(value != null) {
            val type: Type = Types.newParameterizedType(List::class.java, RouteDirection::class.java)
            val adapter: JsonAdapter<List<*>> = moshi.adapter(type)
            adapter.toJson(value)
        }
        else null
    }

    @TypeConverter
    fun stringToRouteDirection(value: String?) : List<RouteDirection>? {
        return if(value != null) {
            val type: Type = Types.newParameterizedType(List::class.java, RouteDirection::class.java)
            val adapter: JsonAdapter<List<RouteDirection>> = moshi.adapter(type)
            adapter.fromJson(value)
        } else null
    }

    @TypeConverter
    fun stringFromStop(value: List<Stop>?) : String? {
        return if(value != null) {
            val type: Type = Types.newParameterizedType(List::class.java, Stop::class.java)
            val adapter: JsonAdapter<List<*>> = moshi.adapter(type)
            adapter.toJson(value)
        }
        else null
    }

    @TypeConverter
    fun stringToStop(value: String?) : List<Stop>? {
        return if(value != null) {
            val type: Type = Types.newParameterizedType(List::class.java, Stop::class.java)
            val adapter: JsonAdapter<List<Stop>> = moshi.adapter(type)
            adapter.fromJson(value)
        } else null
    }



}