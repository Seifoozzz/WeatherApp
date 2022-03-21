package com.example.weatherapp.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DaysItemBinding


import com.example.weatherapp.models.DailyItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToLong


class DaysAdapter(var days: ArrayList<DailyItem>) : RecyclerView.Adapter<DaysAdapter.DaysVH>() {

    lateinit var binding:DaysItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysVH {
        binding = DaysItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaysVH(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DaysVH, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int {
        return days.size
    }

    fun updateDays(newDays: List<DailyItem>){
        days.clear()
        days.addAll(newDays)
        notifyDataSetChanged()
    }



    class DaysVH(val binding: DaysItemBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bind(days: DailyItem) {
            binding.daysCellIcon.setImageResource(getResId(days.weather?.get(0)?.icon))  //(hours.weather[0].id)
            binding.daysDayTVVal.text = dateConverter(days.dt!!)
            Log.d("koooko",dateConverter(days.dt!!))
            binding.daysGrtTempTVVal.text = days.temp?.max?.toInt().toString().plus("°")
            binding.daysSmlTempTVVal.text = days.temp?.min?.toInt().toString().plus("°")
            binding.daysDescTVVal.text = days.weather?.get(0)?.main
            binding.daysHumidityTVVal.text = (days.humidity)?.toString().plus(" %")
            binding.daysCloudsTVVal.text = (days.clouds)?.toString().plus(" %")
            binding.daysPressureTVVal.text = (days.pressure)?.toString().plus(" ${ context.resources.getString(R.string.hPa)}")
            binding.daysWindTVVal.text = (days.windSpeed)?.toString().plus(" ${ context.resources.getString(R.string.met_per_sec)}")

        }

        private fun dateConverter(dt: Double): String {
            val calender = Calendar.getInstance()
            calender.timeInMillis = ((dt)*1000).roundToLong()
            val dateFormat = SimpleDateFormat("EEEE")
            Log.d("datee",dateFormat.format(calender.time))
            return getDay(dateFormat.format(calender.time))
            Log.d("daau",getDay(dateFormat.format(calender.time)))
        }

        private fun getDay(day: String): String { //for locale
            return when (day){
                "Monday" -> R.string.days_mon.toString()
                "Tuesday" -> context.resources.getString(R.string.days_tue)
                "Wednesday" -> context.resources.getString(R.string.days_wed)
                "Thursday" -> context.resources.getString(R.string.days_thu)
                "Friday" -> context.resources.getString(R.string.days_fri)
                "Saturday" -> context.resources.getString(R.string.days_sat)
                "Sunday" -> context.resources.getString(R.string.days_sun)
                else -> "null"
            }
        }

        private fun getResId(icon: String?): Int {
            return when(icon){
                "01d" -> R.drawable.d_oneone
                "01n" -> R.drawable.d_one_n
                "02d" -> R.drawable.d_two
                "02n" -> R.drawable.d_two_n
                "03d", "03n", "04d", "04n" -> R.drawable.d_three_four
                "09d", "09n" -> R.drawable.d_nine
                "10d", "10n" -> R.drawable.d_ten
                "11d", "11n" -> R.drawable.d_eleven
                "13d", "13n" -> R.drawable.d_thirteen
                "50d", "50n" -> R.drawable.d_fifty
                else -> R.drawable.d_two
            }
        }
    }
}






