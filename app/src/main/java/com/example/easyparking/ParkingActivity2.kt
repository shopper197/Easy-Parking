package com.example.easyparking

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.getLongDateFormat
import android.text.format.DateFormat.getTimeFormat

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.easyparking.databinding.ActivityMainBinding
import com.example.easyparking.databinding.ActivityParking2Binding
import com.google.android.material.timepicker.TimeFormat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min


class ParkingActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivityParking2Binding

    private  var oraScelta :Int = 0
    private  var minutiScelta :Int = 0
    private var meseScelta : Int = 0
    private var giornoScelta : Int = 0
    private var annoScelta : Int = 0

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityParking2Binding.inflate(layoutInflater)
        setContentView(R.layout.activity_parking2)

            creteNotificationChannel()

        val setDate : Button = findViewById(R.id.SetData)
        val setOra : Button = findViewById(R.id.SetOra)
        val oraSettata : TextView =findViewById(R.id.OraSettata)



        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { View, year, mounth, dayOfMounth->
            myCalendar.set(Calendar.YEAR, year)
            annoScelta = year
            meseScelta = mounth
            giornoScelta = dayOfMounth
            myCalendar.set(Calendar.MONTH, mounth)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMounth)
            updateLabel(myCalendar)
        }

        setDate.setOnClickListener{
            DatePickerDialog(this,datePicker,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(
                Calendar.DAY_OF_YEAR)).show()
        }


        //setto ora e data attuali

        val rightNow = Calendar.getInstance()
        var Hour24Format : String =rightNow.get(Calendar.HOUR_OF_DAY).toString()
        val dataOdierna : Int =rightNow.get(Calendar.DAY_OF_MONTH)
            Hour24Format=Hour24Format.toString()

        val oraAttualeMostrata : TextView =findViewById(R.id.OraAttuale)
        val dataAttualeMostrata : TextView =findViewById(R.id.dataAttuale)

        oraAttualeMostrata.setText(Hour24Format.toString())
        dataAttualeMostrata.setText(dataOdierna.toString())

        val avvioSosta : Button = findViewById(R.id.Conferma)
        avvioSosta.setOnClickListener(View.OnClickListener { startActivity(Intent(this, MainActivity::class.java));scheduleNotification() })

        setOra.setOnClickListener(){
            val currentTime = Calendar.getInstance()
            val startHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                oraSettata.setText("$hourOfDay:$minute")
                oraScelta = hourOfDay
                minutiScelta = minute

            }, startHour, startMinute, true ).show()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun creteNotificationChannel() {

        val name = "notification channel"
        val desc = "Description of the channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description= desc
        val notificationManager =getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    @SuppressLint("NewApi")
    private fun scheduleNotification() {

        val intent = Intent(applicationContext, Notification::class.java)
        val title = "Sosta terminata"
        val message= "La tua sosta è terminata non rischiare di prendere la multa"
        intent.putExtra(messageExtra, message)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String) {

        val date = Date(time)
        val dateFormat = "dd/MM/yyyy"
        val timeFormat = "HH/mm"
        AlertDialog.Builder(this)
            .setTitle("Notification scheduled")
            .setMessage("At:"+dateFormat.format(date)+ " "+timeFormat.format(date) )
            .setPositiveButton("okay"){_,_->}
            .show()


    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getTime(): Long {
        val hour1 = oraScelta
        val minute1 = minutiScelta
        val day1 = giornoScelta
        val month1 = meseScelta
        val year1 =annoScelta
        val calendar1 = Calendar.getInstance()
        calendar1.set(year1,month1,day1,hour1,minute1)
        return calendar1.timeInMillis

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(myCalendar: Calendar) {
        val myFormat="dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ITALY)
        val dataFine : TextView = findViewById(R.id.DataSettata)
        dataFine.setText(sdf.format(myCalendar.time))
    }



}