package com.example.easyparking


import android.app.DialogFragment
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class ParkingCheck : AppCompatActivity() {
    var minuti: Int = 0;
    var ore: Int = 0;
    var giorno: Int = 0;
    var mese: Int = 0;
    var anno: Int = 0;
    var latitudine: Float = 0.0F
    var longitudine: Float = 0.0F
    lateinit var parkShower: ImageView
    var pathing : String =""


    companion object{
        var b:Bitmap?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_check)
        parkShower = findViewById(R.id.parkShower)
        val parkingFinder: Button = findViewById(R.id.parkingFinder)




        load()
        updateText()
        loadImageFromStorage(pathing)



        parkingFinder.setOnClickListener() {
            val goggleUrl = "https://maps.google.com/?q="
            val url = "$goggleUrl$longitudine,$latitudine"
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
        latitudine = SharedPreferences.getFloat("long", 0.0F)
        longitudine = SharedPreferences.getFloat("lati", 0.0F)
        pathing= SharedPreferences.getString("path","not found")!!
    }

    private fun loadImageFromStorage(path: String) {
        try {
            val f = File(path, "profile.jpg")
             b = BitmapFactory.decodeStream(FileInputStream(f))

            parkShower.setImageBitmap(b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

    }

    fun fullScreenOpener(view: View) { startActivity(
        Intent(
            this,
            immagineFullScreen::class.java
        ))
    }


}