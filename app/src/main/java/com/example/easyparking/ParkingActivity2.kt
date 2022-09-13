package com.example.easyparking

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.easyparking.databinding.ActivityParking2Binding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ParkingActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivityParking2Binding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private  var oraScelta :Int = 0
    private  var minutiScelta :Int = 0
    private var meseScelta : Int = 0
    private var giornoScelta : Int = 0
    private var annoScelta : Int = 0
    private var longitudine : Double = 0.0
    private var latitudine : Double = 0.0
    private var path:String=""
    private lateinit var photo : Bitmap
    companion object{
        private const val CAMERA_PERMISSION_CODE =1
        private const val CAMERA =1

    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =ActivityParking2Binding.inflate(layoutInflater)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)


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
        var minutiOra :Int =rightNow.get(Calendar.MINUTE)
        var oreOra :Int= rightNow.get(Calendar.HOUR_OF_DAY)
        var Hour24Format : String ="$oreOra:$minutiOra"
        var giornoOra :Int = rightNow.get(Calendar.DAY_OF_MONTH)
        var meseOra :Int = rightNow.get(Calendar.MONTH)
        var annoOra :Int = rightNow.get(Calendar.YEAR)

        val dataOdierna : String="$giornoOra/$meseOra/$annoOra"


        val oraAttualeMostrata : TextView =findViewById(R.id.OraAttuale)
        val dataAttualeMostrata : TextView =findViewById(R.id.dataAttuale)

        oraAttualeMostrata.setText(Hour24Format)
        dataAttualeMostrata.setText(dataOdierna)

        val avvioSosta : Button = findViewById(R.id.Conferma)
        avvioSosta.setOnClickListener(sostaAvviata())

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

    private fun sostaAvviata(): View.OnClickListener? {
        return View.OnClickListener {
             salvaDati()
            scheduleNotification()
        }

    }

    private fun salvaDati() {

       //TODO fixare location
        /*fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            longitudine= location.longitude
            latitudine = location.latitude
            Log.d("logitudine",longitudine.toString())
            Log.d("latitudine",latitudine.toString())
        }*/
        val sharedPreferences = getSharedPreferences("variabili", MODE_PRIVATE)
        val editor =sharedPreferences.edit()
        editor.apply{
            putFloat("lati",latitudine.toFloat())
            putFloat("long",longitudine.toFloat())
            putInt("minu",minutiScelta)
            putInt("ore",oraScelta)
            putInt("giorno",giornoScelta)
            putInt("mese",meseScelta)
            putInt("anno",annoScelta)
            putString("path",path)
        }.apply()
        Log.d("ddd sono arrivato qui"," ddd")
        dataShare.pathShare=path.toString()



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
            .setMessage("la notifica per la tua sosta è stata impostata correttamente" )
            .setPositiveButton("okay"){_,_->startActivity(Intent(this, MainActivity::class.java))}
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

    fun goBackHome(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun takePhoto(view: View) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) ==PackageManager.PERMISSION_GRANTED){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA)
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE)
        }


        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== CAMERA_PERMISSION_CODE){
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA)
            }else{
                Toast.makeText(this,"Hai rifiutato i permessi, per favore vai a permettterli nelle impostazioni",
                Toast.LENGTH_LONG).show(
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK){
            if(requestCode ==CAMERA){
                 photo = data!!.extras!!.get("data") as Bitmap
                val photoShower :ImageView= findViewById(R.id.photoShower)
                photoShower.setImageBitmap(photo)
                saveToInternalStorage(photo)
            }
        }

    }
    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)

        val directory: File = cw.getDir("imageDir", MODE_PRIVATE)
        val mypath = File(directory, "profile.jpg")
        path=directory.absolutePath

        Log.d("ddd ",directory.absolutePath)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)

            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.getAbsolutePath()
    }


}


