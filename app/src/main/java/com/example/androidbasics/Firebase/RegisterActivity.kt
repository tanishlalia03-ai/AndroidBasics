package com.example.androidbasics.Firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidbasics.Bottomnavigation.BottomLayoutActivity
import com.example.androidbasics.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        // Handle window insets for Edge-to-Edge (prevents UI from hiding under status bar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.registerBTN.setOnClickListener {
            val email = binding.emailET.text.toString().trim()
            val password = binding.passwordET.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailET.error = "Email is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.passwordET.error = "Password is required"
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.passwordET.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            // Firebase Registration Logic
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(this, "User created successfully!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, BottomLayoutActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Authentication Failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
        }

        binding.signOutBTN.setOnClickListener {
            // 1. Tell Firebase to sign out
            auth.signOut()

            // 2. Send user back to Login/Register screen
            val intent = Intent(this, BottomLayoutActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}