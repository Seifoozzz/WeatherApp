package com.example.weatherapp.database.converters

import androidx.room.TypeConverter

import com.example.weatherapp.models.HourlyItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HourlyConverter {

    companion object{
        @TypeConverter
        @JvmStatic
        fun fromAlertItemList(value: MutableList<HourlyItem?>?): String? {
            val gson = Gson()
            val type = object : TypeToken<MutableList<HourlyItem>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toAlertItemList(value: String?): MutableList<HourlyItem?>? {
            if (value == null) {
                return null
            }
            val gson = Gson()
            val type = object : TypeToken<MutableList<HourlyItem>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}