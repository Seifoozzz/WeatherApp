package com.example.weatherapp.database

import android.app.Application
import androidx.room.Room



object LocalDataSource {

    fun getInstance(application: Application): WeatherDao {
        return Room.databaseBuilder(application, WeatherDatabase::class.java, "WeatherData")
                .fallbackToDestructiveMigration().build().weatherDao()
    }



}