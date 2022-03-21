package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.constants.LATITUDE
import com.example.weatherapp.constants.LONGITUDE
import com.example.weatherapp.constants.SHARED_PREF
import com.example.weatherapp.ui.home.HomeViewModel.Companion.context


import com.google.android.gms.location.*
import com.orhanobut.hawk.Hawk

class Utils {

    companion object {


        lateinit var fusedLocationClient: FusedLocationProviderClient
        var myLocation: LocationResult?=null

        private lateinit var locationRequest: LocationRequest

        @SuppressLint("MissingPermission")
        fun getCurrentLocation() {
            fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context!!)
            getNewLocation()
        }

        @SuppressLint("MissingPermission")
        fun getNewLocation() {
            locationRequest = LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 1000
            locationRequest.fastestInterval = 100000
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()!!
            )
        }


        private val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                myLocation = p0
                Hawk.put("CLAT", myLocation!!.lastLocation.latitude)
                Hawk.put("CLON", myLocation!!.lastLocation.longitude)
                Log.d("seiiiif", "" + myLocation!!.lastLocation.latitude + "         " + myLocation!!.lastLocation.longitude+ "")
            }
        }
    }
}