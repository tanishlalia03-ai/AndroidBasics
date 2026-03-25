package com.example.androidbasics.androidSDK

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidbasics.R

class SensorActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null
    private lateinit var dataTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)

        dataTextView = findViewById(R.id.sensorDataText)

        // 1. Initialize SensorManager
        // Added 'Context.' prefix to SENSOR_SERVICE
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // 2. Select the specific sensor (e.g., Proximity)
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null) {
            dataTextView.text = "No Proximity Sensor detected on this device."
        }
    }

    override fun onResume() {
        super.onResume()
        // 3. Register the listener
        proximitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        // 4. Unregister to save battery
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Typically used for calibration changes (e.g., Compass)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {
            val distance = event.values[0]
            dataTextView.text = "Proximity Distance: $distance cm"
        }
    }
}