package com.example.weatherapp.ui.home.favourite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CitiesItemBinding
import com.example.weatherapp.models.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*


class FavoriteAdapter(var cities: ArrayList<WeatherResponse>,val listener: (WeatherResponse) -> Unit) : RecyclerView.Adapter<FavoriteAdapter.CitiesVH>() {

    lateinit var binding: CitiesItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesVH {
        binding = CitiesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CitiesVH(binding, parent.context)
    }

    override fun onBindViewHolder(holder: CitiesVH, position: Int) {
        holder.bind(cities[position])
        holder.itemView.setOnClickListener { listener(cities[position]) }

    }

    override fun getItemCount(): Int {
        return cities.size
    }

    fun updateHours(newHours: List<WeatherResponse>) {
        cities.clear()
        cities.addAll(newHours)
        notifyDataSetChanged()
    }

    class CitiesVH(val binding: CitiesItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(city: WeatherResponse) {
            binding.cellContainer.setBackgroundResource(getModeRes(city))  //(hours.weather[0].id)
            binding.favDateTVVal.text =
                city.current?.dt?.let { it -> dateConverter(it, "EEE, d MMM") }.toString()
            binding.favCityNameTVVal.text = city.timezone
            binding.favTempTVVal.text = city.current?.temp?.toInt().toString().plus("°")
        }

        private fun getModeRes(weatherResponse: WeatherResponse): Int {
            if (weatherResponse.current?.weather?.get(0)?.icon?.get(2).toString() == "n") {
                return R.drawable.nightt
            }
            return R.drawable.background
        }

        private fun dateConverter(dt: Double, pattern: String): String {
            val calender = Calendar.getInstance()
            calender.timeInMillis = (((dt) * 1000L).toLong())
            val dateFormat = SimpleDateFormat(pattern)
            return dateFormat.format(calender.time)
        }

    }
}