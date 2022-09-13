package com.example.easyparking

import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class ParkingCheck : AppCompatActivity() {
    var minuti: Int = 0;
    var ore: Int = 0;
    var giorno: Int = 0;
    var mese: Int = 0;
    var anno: Int = 0;
    var latitudine: Double = 0.0
    var longitudine: Double = 0.0
    lateinit var parkShower: ImageView
    var pathing : String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_check)
        parkShower = findViewById(R.id.parkShower)
        val parkingFinder: Button = findViewById(R.id.parkingFinder)


        Log.d("ddd dataSHare: ",dataShare.pathShare)
        Log.d("ddd  shared preferences",pathing)

        load()
        updateText()
        loadImageFromStorage(pathing)
        Log.d("ddd classe shared",dataShare.pathShare)
        Log.d("ddd shared preferences",pathing)

        parkingFinder.setOnClickListener() {
            val goggleUrl = "https://maps.google.com/?q="
            val url = "$goggleUrl$latitudine,$longitudine"
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(i)
        }
        val goBack: Button = findViewById(R.id.goBack)
        goBack.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        })

    }

    private fun updateText() {
        val dataScelta: TextView = findViewById(R.id.dataScelta)
        dataScelta.setText("$giorno/$mese/$anno")
        val oraScelta: TextView = findViewById(R.id.oraScelta)
        oraScelta.setText("$ore:$minuti")
    }

    fun load() {
        val SharedPreferences = getSharedPreferences("variabili", MODE_PRIVATE)
        minuti = SharedPreferences.getInt("minu", 0)
        ore = SharedPreferences.getInt("ore", 0)
        giorno = SharedPreferences.getInt("giorno", 0)
        mese = SharedPreferences.getInt("mese", 0)
        anno = SharedPreferences.getInt("anno", 0)
        latitudine = SharedPreferences.getString("long", "0.0")?.toDouble()!!
        longitudine = SharedPreferences.getString("lati", "0.0")?.toDouble()!!
        pathing= SharedPreferences.getString("path","not found")!!
    }

    private fun loadImageFromStorage(path: String) {
        try {
            val f = File(path, "profile.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))

            parkShower.setImageBitmap(b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

    }
}