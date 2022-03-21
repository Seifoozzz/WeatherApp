package com.example.weatherapp.database.converters

import androidx.room.TypeConverter
import com.example.weatherapp.models.WeatherItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherConverter {

    companion object{
        @TypeConverter
        @JvmStatic
        fun fromAlertItemList(value: MutableList<WeatherItem?>?): String? {
            val gson = Gson()
            val type = object : TypeToken<MutableList<WeatherItem>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toAlertItemList(value: String?): MutableList<WeatherItem?>? {
            if (value == null) {
                return null
            }
            val gson = Gson()
            val type = object : TypeToken<MutableList<WeatherItem>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}