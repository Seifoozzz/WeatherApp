package com.example.weatherapp.database.remote


import com.example.weatherapp.api.ApiManager
import com.example.weatherapp.api.WebServices
import com.example.weatherapp.models.WeatherResponse
import retrofit2.Response

class RemoteDataSource {

    private val retrofit: WebServices = ApiManager.getWeatherService()
    suspend fun getWeatherData(lat: Double, lon: Double, appid: String, units: String, lang: String): Response<WeatherResponse> {
        return retrofit.getWeather(lat,lon,appid,units, lang)
    }

}
