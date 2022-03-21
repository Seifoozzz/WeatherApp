package com.example.weatherapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.HomeFragmentBinding
import com.example.weatherapp.models.DailyItem
import com.example.weatherapp.models.HourlyItem
import com.example.weatherapp.models.WeatherResponse
import com.orhanobut.hawk.Hawk
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private var daysListAdapter = DaysAdapter(arrayListOf())
    private var hoursListAdapter = HoursAdapter(arrayListOf())

    private val vm: HomeViewModel by viewModels()

    private fun initRecyclers() {
        binding.daysRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = daysListAdapter
        }
        binding.hoursRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = hoursListAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeLanguage()
        loadSettings()
        subscribeData()

    }

    private fun loadSettings() {
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val language = sp.getString("languages", "")
        val units = sp.getString("units", "")
        Hawk.put("language", language)
        Hawk.put("units", units)
        if(vm.checkForInternet(requireContext())){
            vm.getCurrentData()
        }else vm.getFromLocale()

    }
    private fun setLocale(langCode: String){
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val resources = resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    fun changeLanguage(){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val language = sharedPreferences.getString("languages","")
        if (language != null) {
            setLocale(language)
        }
    }

    private fun subscribeData() {
        vm.currentWeather.observe(viewLifecycleOwner) {
            binding.progress.isVisible=false
            initRecyclers()
            binding.mainBack.setBackgroundResource(getModeRes(it!!))
            binding.date.text = it!!.current!!.dt.let {
                dateConverter(it)
            }
            binding.cityname.text = it.timezone
            binding.dsc.text = it.current!!.weather!!.get(0)!!.description
            Glide.with(requireContext()).load(getResId(it.current.weather!!.get(0)?.icon))
                .into(binding.icon)
            binding.temp.text = it.current!!.temp.toString().plus("°")
            // binding.humiditynum.text=it.current.humidity.toString().plus("%")
            binding.humidity.text =
                getString(R.string.humidity, "" + it.current.humidity.toString().plus("%"))
            binding.clouds.text =
                getString(R.string.clouds, "" + it.current.clouds.toString().plus("%"))
            binding.pressurenum.text = it.current.pressure.toString().plus("hpa")
            binding.windnum.text = it.current.windSpeed.toString().plus("m/s")
            daysListAdapter.updateDays(it.daily as List<DailyItem>)
            hoursListAdapter.updateHours(it.hourly as List<HourlyItem>)
            Log.d("iconn", it.current.weather!!.get(0)!!.icon.toString())

        }
    }

    private fun dateConverter(dt: Double?): String {
        val calender = Calendar.getInstance()
        calender.timeInMillis = ((dt)!! * 1000).toLong()
        val dateFormat = SimpleDateFormat( "EEE, d MMM", Locale(Hawk.get("language")?:"en"))
        return dateFormat.format(calender.time)
    }

    private fun getResId(icon: String?): Int {
        val res = when (icon) {
            "01d" -> R.drawable.d_oneone
            "01n" -> R.drawable.n_one_n
            "02d" -> R.drawable.n_two
            "02n" -> R.drawable.n_two_n
            "03d", "03n", "04d", "04n" -> R.drawable.n_three_four
            "09d", "09n" -> R.drawable.n_nine
            "10d", "10n" -> R.drawable.n_ten
            "11d", "11n" -> R.drawable.n_eleven
            "13d", "13n" -> R.drawable.n_thirteen
            "50d", "50n" -> R.drawable.n_fifty
            else -> R.drawable.n_two
        }
        return res
    }

    private fun getModeRes(weatherResponse: WeatherResponse): Int {
        if (weatherResponse.current?.weather?.get(0)?.icon?.get(2).toString() == "n") {
            return R.drawable.nightt
        }
        return R.drawable.background
    }

}