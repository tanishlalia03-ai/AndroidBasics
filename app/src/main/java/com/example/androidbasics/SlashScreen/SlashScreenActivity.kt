package com.example.androidbasics.SlashScreen

import android.content.Intent
import android.os.Bundle
import com.example.androidbasics.R // CORRECT: This points to YOUR project resources
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.androidbasics.Bottomnavigation.BottomLayoutActivity

class SlashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_slash_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            delay(2000)
            val intent = Intent(this@SlashScreenActivity, BottomLayoutActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}