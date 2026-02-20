package com.example.androidbasics.Firebase

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidbasics.databinding.ActivityCrashBinding // Import generated binding

class CrashActivity : AppCompatActivity() {

    // Initialize the binding variable
    private lateinit var binding: ActivityCrashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the layout using binding
        binding = ActivityCrashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use binding.main to access the ConstraintLayout with id "main"
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Trigger the crash using the button ID from your XML
        binding.btnCrash.setOnClickListener {
            throw RuntimeException("Test Crash using View Binding")
        }
    }
}