package com.example.androidbasics.Webview

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.RoomDatabase
import com.example.androidbasics.R
import com.example.androidbasics.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Setup WebView
        binding.myWebView.settings.javaScriptEnabled = true
        binding.myWebView.webViewClient = WebViewClient()
        binding.myWebView.loadUrl("https://huggingface.co/")

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.myWebView.canGoBack()) {
                    binding.myWebView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}
