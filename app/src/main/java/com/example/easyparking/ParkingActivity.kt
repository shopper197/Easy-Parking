package com.example.easyparking

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

class ParkingActivity : AppCompatActivity() {

    var dataFine : TextView =findViewById(R.id.dataFine)
    var setDate : Button =findViewById(R.id.setDate)


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)


        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { View, year, mounth, dayOfMounth->
            myCalendar.set(Calendar.YEAR, year)
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
        val Hour24Format : Int =rightNow.get(Calendar.HOUR_OF_DAY)
        val dataOdierna : Int =rightNow.get(Calendar.DAY_OF_YEAR)

        var oraSettata = rightNow.get(Calendar.HOUR_OF_DAY)
        var dataSettata =rightNow.get(Calendar.DAY_OF_YEAR)
        val oraAttualeMostrata : TextView =findViewById(R.id.oraAttuale)
        val dataAttualeMostrata : TextView =findViewById(R.id.dataDiOggi)

        oraAttualeMostrata.setText(Hour24Format)
        dataAttualeMostrata.setText(dataOdierna)






    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(myCalendar: Calendar) {
        val myFormat="dd,MM,yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ITALY)
        dataFine.setText(sdf.format(myCalendar.time))
    }
}