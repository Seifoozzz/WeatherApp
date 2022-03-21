package com.example.weatherapp.reposetries

import android.app.Application
import com.example.weatherapp.database.LocalDataSource
import com.example.weatherapp.database.remote.RemoteDataSource
import com.example.weatherapp.models.WeatherResponse
import retrofit2.Response

class WeatherRepository(val application: Application) {

    private val remoteDataSource = RemoteDataSource()
    private val localDataSource = LocalDataSource.getInstance(application)


    suspend fun getWeatherData(
        lat: Double,
        lon: Double,
        appid: String,
        units: String,
        lang: String,
    ): Response<WeatherResponse> { //from Retrofit
        return remoteDataSource.getWeatherData(lat, lon, appid, units, lang)
    }

    suspend fun deleteOldCurrent() {
        return localDataSource.deleteOldCurrent()
    }

    suspend fun getCity(lat: Double, lon: Double): WeatherResponse {  //from local
        return localDataSource.getCityData(lat, lon)
    }

    suspend fun getFavCities(): List<WeatherResponse> {
        return localDataSource.getAllCities()

    }

    suspend fun addCityToLocal(city: WeatherResponse) {
        localDataSource.insert(city)

    }

    suspend fun deleteCityData(lat: Double, lon: Double) {
        localDataSource.deleteCityData(lat, lon)
    }


}