package com.example.androidbasics.chatboat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.androidbasics.databinding.ActivityChatboatBinding
import kotlinx.coroutines.launch

class ChatboatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatboatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityChatboatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnsend.setOnClickListener {
            val userPrompt = binding.etMessage.text.toString().trim()
            if (userPrompt.isNotEmpty()) {
                fetchResponse(userPrompt)
                binding.etMessage.text?.clear()
            }
        }
    }

    private fun fetchResponse(prompt: String) {
        // Visual feedback that the AI is working
        binding.tvGeminiResponse.text = "Thinking..."
        binding.tvGeminiResponse.alpha = 0.5f

        lifecycleScope.launch {
            Chatboat.getGeminiTextResponse(prompt) { response ->
                runOnUiThread {
                    binding.tvGeminiResponse.alpha = 1.0f
                    // Update the simple TextView with the result
                    binding.tvGeminiResponse.text = response
                }
            }
        }
    }
}