package com.haithamghanem.weatherwizard.ui.weather.features.alerts

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.weatherapp.ui.home.alert.AlertFragment
import kotlinx.android.synthetic.main.alert_fragment.*
import java.util.*

class TimePickerFragment : DialogFragment(),TimePickerDialog.OnTimeSetListener{

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c[Calendar.HOUR_OF_DAY]
        val minute = c[Calendar.MINUTE]
        return TimePickerDialog(
            activity, this, hour, minute,
            DateFormat.is24HourFormat(activity));
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        AlertFragment.cal = Calendar.getInstance();
        AlertFragment.cal[Calendar.HOUR_OF_DAY] = hourOfDay
        AlertFragment.cal[Calendar.MINUTE] = minute
        AlertFragment.cal[Calendar.SECOND] = 0
        Log.d("calenderTimeafter", "onCreate: ${java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(AlertFragment.cal.time)} ")
//        updateTimeText(AlertFragment.cal)
    }
    private fun updateTimeText(c: Calendar) {
        var timeText: String? = "Alarm set for: "
        timeText += java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.time)
        alertTime.text = timeText
    }
}