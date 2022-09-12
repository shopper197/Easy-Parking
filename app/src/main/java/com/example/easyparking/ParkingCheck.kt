package com.example.easyparking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ParkingCheck : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_check)

        val goBack : Button = findViewById(R.id.goBack)
        goBack.setOnClickListener(View.OnClickListener { startActivity(Intent(this, MainActivity::class.java)) })

    }
}