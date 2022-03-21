package com.example.weatherapp.ui.home.alert
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.Ringtone
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherapp.R
import com.example.weatherapp.constants.API_KEY
import com.example.weatherapp.models.Alerts
import com.example.weatherapp.reposetries.WeatherRepository
import com.example.weatherapp.ui.home.HomeViewModel
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*


val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "Shows notifications whenever work starts"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1
 class UploadWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {
     val application: Application
         get() = applicationContext as Application
     val prefrences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(applicationContext)
    private val weatherRepository = WeatherRepository(application)
    private var listOfAlerts: List<Alerts>? = ArrayList()
     lateinit var lat:BigDecimal
     lateinit var lon:BigDecimal


    override fun doWork(): Result {

        try {
            loadSettings()
            getResponseFromApi()
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }


    private fun changinglanguageOfTitle(): String {
        val lang = prefrences.getString("languages", "")
        if (lang == "en") {
            return "Weather Update"
        } else {
            return "!شعار بالطقس"
        }
    }

    private fun changinglanguageOfMessage(): String {
        val lang = prefrences.getString("languages", "")
        if (lang == "en") {
            return "There are no weather alerts have a nice day."
        } else {
            return "لا يوجد خطر !ستمتع بيوم جميل."
        }
    }


    private fun getResponseFromApi() {
        var biglat:Double=Hawk.get("CLAT")?:30.3077
        var biglon:Double=Hawk.get("CLON")?:30.2958
        lat = BigDecimal(biglat).setScale(4, RoundingMode.HALF_EVEN)
        lon= BigDecimal(biglon).setScale(4, RoundingMode.HALF_EVEN)
        GlobalScope.launch(Dispatchers.IO) {

            val response = weatherRepository.getWeatherData(
                lat.toDouble(),
                lon.toDouble(),
                API_KEY,
                Hawk.get("units") ?: "metric",
                Hawk.get("language") ?: "en"
            )


            withContext(Dispatchers.Main) {

                if (response.isSuccessful) {
                    listOfAlerts = response.body()?.alerts
                    if (listOfAlerts?.isNotEmpty() == true) { // de fe 7alet lw fe alerts rag3a m3 el response

                        for (item in listOfAlerts!!) {
                            val inputime = inputData.getLong(INPUT_TIME, 0)
                            Log.d("timeSendInWorker", "setOneTimeWorkRequest: ${inputime} ")
                            if ((inputime <= item.end * 1000) && (inputime >= item.start * 1000)
                            ) {
                                makeStatusNotification(
                                    changinglanguageOfTitle(),
                                    changinglanguageOfMessage(),
                                    applicationContext
                                )
                                AlertActivity.title = changinglanguageOfTitle()
                                AlertActivity.message = response.body()!!.alerts!!.get(0).description
                                Handler(Looper.getMainLooper()).post({
                                    val intent = Intent(applicationContext, AlertActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    applicationContext.startActivity(intent)
                                })
                            }
                        }
                    }else {
                        makeStatusNotification(
                            changinglanguageOfTitle(),
                            changinglanguageOfMessage(),
                            applicationContext
                        )
                    }

                }
            }
        }
    }

    fun makeStatusNotification(title: String, message: String, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
            val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
            channel.description = description
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.cloud_computing)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
    private fun loadSettings() {
        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val language = sp.getString("languages", "")
        val units = sp.getString("units", "")


    }
}