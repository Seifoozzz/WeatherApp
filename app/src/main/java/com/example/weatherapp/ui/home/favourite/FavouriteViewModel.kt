package com.example.weatherapp.ui.home.favourite

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.constants.FAV_LATITUDE
import com.example.weatherapp.constants.FAV_LONGITUDE
import com.example.weatherapp.constants.SHARED_PREF
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.reposetries.WeatherRepository
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.*

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {
    private val weatherRepository = WeatherRepository(getApplication())

    //    val startCityDetailsActivityLiveData = MutableLiveData<WeatherData>()
    val citisListLiveData = MutableLiveData<List<WeatherResponse>>()


    lateinit var shPref: SharedPreferences
    lateinit var lat: String
    lateinit var lon: String
    lateinit var lang: String
    lateinit var units: String

    fun fetchFavCities() {

        CoroutineScope(Dispatchers.IO).launch {

            citisListLiveData.postValue(weatherRepository.getFavCities())

        }
    }


    fun fetchData() {
        initVar(getApplication())


        CoroutineScope(Dispatchers.IO).launch {
            val response = weatherRepository.getWeatherData(
                lat.toDouble(),
                lon.toDouble(),
                BuildConfig.ApiKey,
                units,
                lang
            )
            if (response.isSuccessful) {
                val res = response.body()
                res?.isFavorite = true
                weatherRepository.addCityToLocal(res!!)

                val favCities = weatherRepository.getFavCities()

                withContext(Dispatchers.Main) {

                    citisListLiveData.postValue(favCities)

                }
            } else {
                withContext(Dispatchers.Main) {
                    Log.d("ghalat", response.message())
                }
            }
        }
    }


    private fun initVar(app: Application) {
        shPref = app.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        lat = shPref.getString(FAV_LATITUDE, "0").toString()
        lon = shPref.getString(FAV_LONGITUDE, "0").toString()
        lang = Hawk.get("language")
        units = Hawk.get("units")
    }

    fun deleteCity(city: WeatherResponse) {
        val exceptionHandlerException = CoroutineExceptionHandler { _, th ->
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            weatherRepository.deleteCityData(city.lat, city.lon)
        }
    }

    fun refreshFavCitiesList() {


        CoroutineScope(Dispatchers.IO).launch {
            val def = async {
                weatherRepository.getFavCities().forEach {
                    fetchDataForRefresh(it.lat, it.lon)
                }
                weatherRepository.getFavCities()
            }
            val res = def.await()
            withContext(Dispatchers.Main) {

                citisListLiveData.postValue(res)

            }
        }
    }

    private fun fetchDataForRefresh(latit: Double, longit: Double) {
        initVar(getApplication())
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                weatherRepository.getWeatherData(latit, longit, BuildConfig.ApiKey, units, lang)
            if (response.isSuccessful) {
                val res = response.body()
                res?.isFavorite = true
                weatherRepository.addCityToLocal(res!!)
                val favCities = weatherRepository.getFavCities()

                withContext(Dispatchers.Main) {
                    //loadingLiveData.postValue(false)
                    citisListLiveData.postValue(favCities)
                }
            } else {
                withContext(Dispatchers.Main) {

                }
            }
        }
    }

}