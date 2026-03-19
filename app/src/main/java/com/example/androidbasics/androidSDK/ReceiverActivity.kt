package com.example.androidbasics.androidSDK

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidbasics.R

class ReceiverActivity : AppCompatActivity() {
    private lateinit var systemReceiver: SystemEventReceivers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_receiver)

        systemReceiver = SystemEventReceivers { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)

            addAction("android.net.conn.CONNECTIVITY_CHANGE")
            addAction(Intent.ACTION_BATTERY_CHANGED)

            addAction("com.example.ACTION_ALARM_TRIGGERED")
        }

        // Note: For Battery and Airplane mode, NOT_EXPORTED is usually fine for internal app logic
        registerReceiver(systemReceiver, filter, RECEIVER_NOT_EXPORTED)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(systemReceiver)
    }
}