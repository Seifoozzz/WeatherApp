package com.example.weatherapp.ui.home.alert

import android.annotation.SuppressLint

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.work.*
import com.haithamghanem.weatherwizard.ui.weather.features.alerts.DatePickerFragment
import com.haithamghanem.weatherwizard.ui.weather.features.alerts.TimePickerFragment

import kotlinx.android.synthetic.main.alert_fragment.*
import kotlinx.android.synthetic.main.alert_settings_menu.*


import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit



const val INPUT_TIME = "input_time"
const val ALARM_TYPE = "ALARM_TYPE"
const val NOTIFICATION_TYPE = "NOTIFICATION_TYPE"
class AlertFragment : Fragment() {



    val prefrences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(requireContext())


    companion object {
        fun newInstance() = AlertFragment()
        lateinit var cal: Calendar
        private var isFirstTime = true

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.alert_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        askUserForNotificationPermissions()

        initComponents()

        loadSettings()

        val alertSettingsMenu = layoutInflater.inflate(R.layout.alert_settings_menu, null)
        alertsDeleteIcon.setOnClickListener {

            WorkManager.getInstance(this.requireContext()).cancelAllWorkByTag("oneTimeRequest")
            cardViewAlerts.visibility = View.GONE

        }

        fabAlert.setOnClickListener {

            if (alertSettingsMenu.parent != null) { //edtaret a3ml el condition da 3shan kan bytl3li error lma ados 3la el fab mrten: 3shan el menuSettings eli bttl3 byb2a already leha parent fa lazm ashelo abl mados tani
                (alertSettingsMenu.parent as ViewGroup).removeView(alertSettingsMenu)
            }
            alertSettingsContainer.addView(alertSettingsMenu)
            btnAlertCancel.setOnClickListener {

                Log.d("alertClickActions", "onViewCreated: clicked cancel")
                alertSettingsContainer.removeAllViews()
            }

            btnAlertSave.setOnClickListener {

                alertTime.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(cal.time)
                alertDate.text = DateFormat.getDateInstance(DateFormat.FULL).format(cal.time)
                Log.d("alertClickActions", "onViewCreated: clicked save")
                cardViewAlerts.visibility = View.VISIBLE
                alertSettingsContainer.removeAllViews()

                setOneTimeWorkRequest()
//                setPeriodicWorkRequest()

            }

            datePickerBtn.setOnClickListener {

                val ft: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
                val newFragment: DialogFragment = DatePickerFragment()
                if (ft != null) {
                    newFragment.show(ft, "dialog")
                }
            }

            timePickerBtn.setOnClickListener {
                val ft: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
                val newFragment: DialogFragment = TimePickerFragment()
                if (ft != null) {
                    newFragment.show(ft, "dialog")
                }
            }



//            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.alertSettingsContainer, alertSettingsMenu)?.addToBackStack("mapFragment")?.commit()
        }


    }



    private fun askUserForNotificationPermissions() {
        if(!prefrences.getBoolean("USE_NOTIFICATIONS_ALERT",false) && isFirstTime==true) {
            val builder = android.app.AlertDialog.Builder(this.requireContext())
            builder.setTitle(changinglanguageOfTitle())
            builder.setMessage(changinglanguageOfMessage())
            builder.setPositiveButton(R.string.Yes) { dialog, which ->

                prefrences.edit().putBoolean("USE_NOTIFICATIONS_ALERT", true).apply()
                isFirstTime=false
            }
            builder.setNegativeButton(
                R.string.No
            ) { dialog, which ->
                isFirstTime= false
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
        }
    }

    fun changinglanguageOfTitle(): String{
        val lang = prefrences.getString("languages", "")
        if(lang == "en"){
            return "Confirm"
        }else{
            return "تأكيد"
        }
    }

    fun changinglanguageOfMessage(): String{
        val lang = prefrences.getString("languages", "")
        if(lang == "en"){
            return "Would you like for weather wizard to send you notifications of the weather updates?"
        }else{
            return "هل تريد أن يرسل !ليك التطبيق !شعارات دورية بالطقس؟"
        }
    }

    private fun initComponents() {



        cal = Calendar.getInstance()
    }


//    private fun setPeriodicWorkRequest() {
//        val constraints =
//            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
////        val currentDate = Calendar.getInstance().timeInMillis
////
////        val delay = cal.timeInMillis - currentDate
//
//        val workManager: WorkManager? = this.context?.let { WorkManager.getInstance(it) }
//        val periodicWorkRequest = PeriodicWorkRequest.Builder(
//            UploadWorker::class.java, 16,
//            TimeUnit.MINUTES
//        ).setConstraints(constraints)
//            .build()
//
//        workManager?.enqueue(periodicWorkRequest)
//        workManager?.getWorkInfoByIdLiveData(periodicWorkRequest.id)?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            Log.d("WorkInProgress", "${it.state} ")
//        })
//    }

    private fun setOneTimeWorkRequest() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //hna h3ml data input lel worker class a7ot feh el time eli el user hyd5lo bel melli seconds
        val inputTime = Data.Builder()
            .putLong(INPUT_TIME, cal.timeInMillis)
            .build()
//        Log.d("time sent", "setOneTimeWorkRequest: ${cal.timeInMillis} ")



        val current = Calendar.getInstance().timeInMillis
        val delay = cal.timeInMillis - current


        val workManager: WorkManager? = this.context?.let { WorkManager.getInstance(it) }
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setInputData(inputTime)
            .setConstraints(constraints)
            .addTag("oneTimeRequest")
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()
        workManager?.enqueue(uploadRequest)
        workManager?.getWorkInfoByIdLiveData(uploadRequest.id)
            ?.observe(viewLifecycleOwner, {
                Log.d("WorkInProgress", "${it.state} ")
            })
    }




//    fun hideAndShowDetails() {
//        if (cardViewAlerts.visibility == View.GONE) {
//            TransitionManager.beginDelayedTransition(
//                cardViewAlerts,
//                AutoTransition()
//            )
//            cardViewAlerts.visibility = View.VISIBLE
//        } else {
//            TransitionManager.beginDelayedTransition(
//                cardViewAlerts,
//                AutoTransition()
//            )
//            cardViewAlerts.visibility = View.GONE
//        }
//    }

    private fun loadSettings() {
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val language = sp.getString("languages", "")
        val units = sp.getString("units", "")



    }

}