package com.example.weatherapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.database.converters.DailyConverter
import com.example.weatherapp.database.converters.HourlyConverter

import com.example.weatherapp.models.WeatherResponse

@Database(entities = [WeatherResponse::class], version = 2, exportSchema = false)
@TypeConverters( DailyConverter::class, HourlyConverter::class)
abstract class WeatherDatabase : RoomDatabase(){

    abstract fun weatherDao(): WeatherDao

}