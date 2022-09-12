package com.example.easyparking

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.easyparking.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val bottoneAvvio : Button = findViewById(R.id.bottoneAvvio)
        bottoneAvvio.setOnClickListener(View.OnClickListener { startActivity(Intent(this, ParkingActivity2::class.java)) })


    }



}