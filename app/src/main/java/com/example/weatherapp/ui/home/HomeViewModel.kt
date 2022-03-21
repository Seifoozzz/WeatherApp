package com.example.weatherapp.ui.home
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Utils
import com.example.weatherapp.constants.API_KEY
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.reposetries.WeatherRepository
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.math.BigDecimal as BigDecimal

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val currentWeather: MutableLiveData<WeatherResponse?> = MutableLiveData()
    private val weatherRepository = WeatherRepository(getApplication())
    lateinit var city: WeatherResponse
    lateinit var lat:BigDecimal
    lateinit var lon:BigDecimal


    companion object {
        var context: Context? = null
    }

    init {
        context = getApplication<Application>().applicationContext
        Utils.getCurrentLocation()

    }
    fun getCurrentData() {
        var biglat:Double=Hawk.get("CLAT")?:30.3077
        var biglon:Double=Hawk.get("CLON")?:30.2958
        lat = BigDecimal(biglat).setScale(4, RoundingMode.HALF_EVEN)
        lon=BigDecimal(biglon).setScale(4, RoundingMode.HALF_EVEN)
        Log.d("seiiif", "" + Hawk.get("CLAT")+ "    ya mosahel     " + Hawk.get("CLON")+ "")
        viewModelScope.launch(Dispatchers.IO) {
                val response = weatherRepository.getWeatherData(
                    lat.toDouble(),
                   lon.toDouble(),
                    API_KEY,
                    Hawk.get("units") ?: "metric",
                    Hawk.get("language") ?: "en"
                )
                if (response.isSuccessful) {
                    Log.d("seiffff", "" + Hawk.get("CLAT")+ "    ya mosahel     " + Hawk.get("CLON")+ "")
                    weatherRepository.addCityToLocal(response.body()!!)
                    Log.d("isa teshtaghal",""+response.body()!!.lat+"   haaaa"  +response.body()!!.lon)
                    city = weatherRepository.getCity(lat.toDouble(),lon.toDouble())
                    currentWeather.postValue(city)
                    Log.d("yaraab", city.current!!.temp.toString())
                }else {
                    Log.d("mm",response.message())
                }
            }
        }



    fun checkForInternet(context: Context): Boolean {


        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false

            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun getFromLocale() {
        viewModelScope.launch(Dispatchers.IO) {
            city = weatherRepository.getCity(lat.toDouble(),lon.toDouble())
            currentWeather.postValue(city)
        }

    }
}


