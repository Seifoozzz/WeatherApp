package com.example.weatherapp.ui.home.favourite.favouritedetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.reposetries.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteDetailsViewModel (application: Application) : AndroidViewModel(application) {
    private val weatherRepository = WeatherRepository(getApplication())
    val cityLiveData = MutableLiveData<WeatherResponse>()



    fun getCity(lat: Double, lon: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            cityLiveData.postValue(weatherRepository.getCity(lat, lon))
        }
    }

}