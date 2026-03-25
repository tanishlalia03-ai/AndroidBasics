package com.example.androidbasics.assignement

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.os.PowerManager

class ProximityService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        // Initialize PowerManager to control the screen
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager

        // PROXIMITY_SCREEN_OFF_WAKE_LOCK is what turns the screen off
        if (powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)) {
            wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "MyApp:ProximityLock")
        }

        proximitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val distance = event?.values?.get(0) ?: 0f

        // BROADCAST for the Activity UI
        val intent = Intent("PROXIMITY_UPDATE")
        intent.putExtra("sensor_value", distance)
        sendBroadcast(intent)

        // LOGIC to turn screen OFF/ON
        if (distance == 0f) {
            if (wakeLock?.isHeld == false) {
                wakeLock?.acquire() // This turns the screen OFF
            }
        } else {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release() // This turns the screen back ON
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        // Safety check: release the lock if the service is destroyed
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
        }
    }
}