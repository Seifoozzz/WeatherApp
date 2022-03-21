package com.example.weatherapp.database.converters

import androidx.room.TypeConverter
import com.example.weatherapp.models.Alerts

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlertConverter {

    companion object{
        @TypeConverter
        @JvmStatic
        fun fromAlertItemList(value: MutableList<Alerts?>?): String? {
            val gson = Gson()
            val type = object : TypeToken<MutableList<Alerts>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toAlertItemList(value: String?): MutableList<Alerts?>? {
            if (value == null) {
                return null
            }
            val gson = Gson()
            val type = object : TypeToken<MutableList<Alerts>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}