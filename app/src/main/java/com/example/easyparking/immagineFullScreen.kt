package com.example.easyparking

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class immagineFullScreen : AppCompatActivity() {
    var pathing:String=""
    lateinit var image: Bitmap
    lateinit var fullScreenImage:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.full_screen)
        fullScreenImage=findViewById(R.id.fullScreenImage)
        loadImage()
    }

    private fun loadImage() {
        val SharedPreferences = getSharedPreferences("variabili", MODE_PRIVATE)
        pathing= SharedPreferences.getString("path","not found")!!
        try {
            val f = File(pathing, "profile.jpg")
            ParkingCheck.b = BitmapFactory.decodeStream(FileInputStream(f))

            fullScreenImage.setImageBitmap(ParkingCheck.b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }


    }

    fun closeFullScreen(view: View) {
        startActivity(
            Intent(this,
                ParkingCheck::class.java))
    }
}