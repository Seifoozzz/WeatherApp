package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.example.ecommerce_kotlin.base.BaseActivity

class SplashActivity :  BaseActivity() {
    val MY_LOCATION_PERMISSIONS_REQUEST_Code = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


                if (isLocationPermissionGranted()) {

                    val intent =Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

         else {
            requestLocationPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isLocationPermissionGranted()) {
            val intent =Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        else {
            requestLocationPermission()
        }
    }
    fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@SplashActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            showMessage(
                "Apps Want To Access your location",
                "ok", DialogInterface.OnClickListener { dialogInterface, i ->
                    ActivityCompat.requestPermissions(
                        this@SplashActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_LOCATION_PERMISSIONS_REQUEST_Code
                    )
                }, true
            )
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_LOCATION_PERMISSIONS_REQUEST_Code
            )
        }
    }
    fun isLocationEnabled(): Boolean {
        val service =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enabled = service
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
        return enabled
    }
    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}