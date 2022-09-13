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
import androidx.appcompat.app.AlertDialog
import com.example.easyparking.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences("variabili", MODE_PRIVATE)
        val bottoneAvvio : Button = findViewById(R.id.bottoneAvvio)
        val oldSosta :Button = findViewById(R.id.oldSosta)
        bottoneAvvio.setOnClickListener(View.OnClickListener { startActivity(Intent(this, ParkingActivity2::class.java)) })
        oldSosta.setOnClickListener(View.OnClickListener {
            if(sharedPreferences.getInt("giorno",0)==0){
                AlertDialog.Builder(this)
                    .setTitle("Nessuna sosta")
                    .setMessage("Nessuna sosta in memoria" )
                    .setPositiveButton("okay"){_,_->}
                    .show()
            }else{
            startActivity(Intent(this,ParkingCheck::class.java))}
        })

    }



}