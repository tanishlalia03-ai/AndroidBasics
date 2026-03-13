package com.example.androidbasics.mlkit

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.androidbasics.R
import com.example.androidbasics.databinding.ActivityTranslateBinding
import kotlinx.coroutines.launch

class TranslateActivity : AppCompatActivity() {

    // Using ViewBinding to access XML elements
    private lateinit var binding: ActivityTranslateBinding
    private val translationHelper = TranslationHelper()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Binding
        binding = ActivityTranslateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Prepare Translator (Downloads model if needed)
        prepareTranslator()

        // 2. Set up Click Listener
        binding.btnTranslate.setOnClickListener {
            val textToTranslate = binding.editInput.text.toString()
            if (textToTranslate.isNotEmpty()) {
                performTranslation(textToTranslate)
            } else {
                Toast.makeText(this, "Please enter some text", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun prepareTranslator() {
        lifecycleScope.launch {
            binding.statusText.text = "Status: Downloading Language Model..."
            binding.btnTranslate.isEnabled = false  // Disable button until ready

            translationHelper.prepareTranslator()

            binding.statusText.text = "Status: Ready"
            binding.btnTranslate.isEnabled = true
        }
    }

    private fun performTranslation(text: String) {
        lifecycleScope.launch {
            binding.statusText.text = "Status: Translating..."
            val result = translationHelper.translateText(text)
            binding.txtOutput.text = result
            binding.statusText.text = "Status: Ready"
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        // Clean up resources
        translationHelper.close()
    }
}