package com.example.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity2 : AppCompatActivity() {
    private lateinit var sharedPreferences: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        val toolbar = findViewById<Toolbar>(R.id.myToolbar)
     setSupportActionBar(toolbar)

        val color = findViewById<TextView>(R.id.textView)
        registerForContextMenu(color)

        val button3 = findViewById<Button>(R.id.popbutton)
        button3.setOnClickListener { view ->
            showpopupmenu(view)
        }

        val button4=findViewById<Button>(R.id.locbutton)
        button4.setOnClickListener{
            val tv = findViewById<TextView>(R.id.loc)
            val lat = 28.8768
            val long = 76.6211
            val add = getadd(lat, long)
            tv.text = add ?: "address not found"
        }



        if (intent?.action == "SHOW_NOTIFICATION") {
            showScheduledNotification()
        }

        val hotel1 = findViewById<Button>(R.id.hotel1)
        val hotel2 = findViewById<Button>(R.id.hotel2)
        val hotel3 = findViewById<Button>(R.id.hotel3)
        val hotel4 = findViewById<Button>(R.id.hotel4)

        sharedPreferences = getSharedPreferences("HotelPrefs",MODE_PRIVATE)

        hotel1.setOnClickListener { handleHotelBooking("Hotel 1") }
        hotel2.setOnClickListener { handleHotelBooking("Hotel 2") }
        hotel3.setOnClickListener { handleHotelBooking("Hotel 3") }
        hotel4.setOnClickListener { handleHotelBooking("Hotel 4") }

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notification -> {
                Toast.makeText(this, "Notification ON", Toast.LENGTH_SHORT).show()
                showDateTimePicker()
                return true
            }

            R.id.settings -> {
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                Toast.makeText(this, "Edit selected", Toast.LENGTH_SHORT).show()
            }

            R.id.delete -> {
                Toast.makeText(this, "Delete selected", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun showpopupmenu(view: View?) {
        val pop = android.widget.PopupMenu(this, view)
        pop.menuInflater.inflate(R.menu.pop, pop.menu)
        pop.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.about -> {
                    Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
        pop.show()
    }

    private fun getadd(lat: Double, long: Double): String? {
        return try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, long, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                address.getAddressLine(0)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun showScheduledNotification() {
        val channelId = "scheduled_notification"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Scheduled Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Reminder!")
            .setContentText("It's time for your scheduled event.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)

        notificationManager.notify(999, builder.build())
    }

    private fun showDateTimePicker() {
        val calendar = java.util.Calendar.getInstance()

        val datePicker = android.app.DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val timePicker = android.app.TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute, 0)
                        scheduleNotification(calendar.timeInMillis)
                    },
                    calendar.get(java.util.Calendar.HOUR_OF_DAY),
                    calendar.get(java.util.Calendar.MINUTE),
                    true
                )
                timePicker.show()
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun scheduleNotification(timeInMillis: Long) {
        val intent = Intent(this, MainActivity2::class.java)
        intent.action = "SHOW_NOTIFICATION"
        val pendingIntent = android.app.PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as android.app.AlarmManager
        alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

        Toast.makeText(this, "Notification Scheduled", Toast.LENGTH_SHORT).show()
    }


    private fun handleHotelBooking(hotelKey: String) {
        val isBooked = sharedPreferences.getBoolean(hotelKey, false)

        if (isBooked) {
            AlertDialog.Builder(this)
                .setTitle("Hotel Booking")
                .setMessage("This hotel is already booked!")
                .setPositiveButton("OK", null)
                .show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Hotel Booking")
                .setMessage("Do you want to book this hotel?")
                .setPositiveButton("Yes") { dialog, which ->
                    sharedPreferences.edit().putBoolean(hotelKey, true).apply()
                    Toast.makeText(this, "$hotelKey is booked!", Toast.LENGTH_SHORT).show()
                    showBookingNotification(hotelKey)
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun showBookingNotification(hotelName: String) {
        val channelId = "hotel_booking_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Hotel Booking",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Hotel Booked!")
            .setContentText("$hotelName is successfully booked.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)

        notificationManager.notify(hotelName.hashCode(), builder.build())
    }
}
