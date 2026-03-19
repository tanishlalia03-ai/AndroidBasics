package com.example.androidbasics.androidSDK

import android.app.ApplicationErrorReport
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import android.widget.Toast

class SystemEventReceivers(
    private val onUpdate: (String) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        when (intent.action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                val state = intent.getBooleanExtra("state", false)
                onUpdate("Airplane Mode: ${if (state) "ON" else "OFF"}")
            }

            // Note: CONNECTIVITY_ACTION is deprecated but still used for older APIs
            // Use ConnectivityManager for modern network monitoring
            "android.net.conn.CONNECTIVITY_CHANGE" -> {
                onUpdate("Network status changed")
            }

            Intent.ACTION_BATTERY_CHANGED -> {
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

                if (level != -1 && scale != -1) {
                    val percent = (level.toFloat() / scale.toFloat() * 100).toInt()
                    onUpdate("Battery: $percent%")
                }
            }

            "com.example.ACTION_ALARM_TRIGGERED" -> {
                Toast.makeText(context, "ALARM RINGING!", Toast.LENGTH_LONG).show()
            }
        }
    }
}

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("Boot", "System restarted, app logic triggered.")
        }
    }
}