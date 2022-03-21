package com.example.weatherapp.ui.home.alert

import android.app.AlertDialog

import android.content.Intent

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import androidx.preference.PreferenceManager
import com.example.weatherapp.MainActivity



class AlertActivity : AppCompatActivity() {


    lateinit var r: Ringtone

    companion object{
       var title:String=""
       var message:String=""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        loadSettings()
        super.onCreate(savedInstanceState)
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
             r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        AlertDialog.Builder(this)
            .setMessage(message)
            .setTitle(title)
            .setCancelable(true)
            .setPositiveButton("Ok") { dialog, which ->
                val intent = Intent(applicationContext, MainActivity::class.java)
                dialog.dismiss()
                finish()
                r.stop()
                startActivity(intent)
            }
            .create().show()
    }



    private fun loadSettings() {
        val sp = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val language = sp.getString("languages", "")
        val units = sp.getString("units", "")



    }
}