package com.example.androidbasics.assignement

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.androidbasics.R

class SensorDisplayActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView

    // The Receiver listens for the "PROXIMITY_UPDATE" broadcast from the Service
    private val sensorReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Get the value sent by ProximityService
            val value = intent?.getFloatExtra("sensor_value", -1f)

            // Update UI based on distance
            if (value == 0f) {
                statusTextView.text = "Status: NEAR\n(Screen should dim/off)"
                statusTextView.setTextColor(ContextCompat.getColor(context!!, android.R.color.holo_red_dark))
            } else {
                statusTextView.text = "Status: FAR\n(Screen is ON)"
                statusTextView.setTextColor(ContextCompat.getColor(context!!, android.R.color.holo_green_dark))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        statusTextView = findViewById(R.id.sensorDataText)

        // Start the background service that handles the WakeLock
        val serviceIntent = Intent(this, ProximityService::class.java)
        startService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()

        // Register the receiver.
        // Note: For Android 14+, we specify RECEIVER_EXPORTED or NOT_EXPORTED
        val filter = IntentFilter("PROXIMITY_UPDATE")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(sensorReceiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(sensorReceiver, filter)
        }
    }

    override fun onPause() {
        super.onPause()
        // We unregister the receiver so the UI doesn't update when the app is in background,
        // but the SERVICE will keep running (and the screen will still turn off).
        unregisterReceiver(sensorReceiver)
    }

    override fun onDestroy() {
        // Optional: If you want the proximity feature to stop when you close the app:
        stopService(Intent(this, ProximityService::class.java))
        super.onDestroy()
    }
}