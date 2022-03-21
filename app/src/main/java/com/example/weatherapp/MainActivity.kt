package com.example.weatherapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.ui.home.HomeFragment
import com.example.weatherapp.ui.home.alert.AlertFragment
import com.example.weatherapp.ui.home.favourite.FavouriteFragment
import com.example.weatherapp.ui.home.setting.SettingsFragment

class MainActivity : AppCompatActivity() {
  // var context: Context?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment = HomeFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(R.id.homeContainer, fragment)
        transaction.commit()
        setContentView(R.layout.activity_main)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                val fragment = SettingsFragment()
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.homeContainer, fragment)
                transaction.addToBackStack(" ")
                transaction.commit()
            }
            R.id.menu_favourite->{
                val fragment = FavouriteFragment()
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.homeContainer, fragment)
                transaction.addToBackStack(" ")
                transaction.commit()
            }

            R.id.menu_alarm -> {
                val fragment = AlertFragment()
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.homeContainer, fragment)
                transaction.addToBackStack(" ")
                transaction.commit()
            }


        }
        return true
    }

}