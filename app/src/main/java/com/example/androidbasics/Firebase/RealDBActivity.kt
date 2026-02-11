package com.example.androidbasics.Firebase


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidbasics.databinding.ActivityRealDbactivityBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class RealDBActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRealDbactivityBinding
    private val database = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRealDbactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnLogin.setOnClickListener {
            val name = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                // Using timestamp as a simple unique ID
                val userId = "user_${System.currentTimeMillis()}"
                writeNewUser(userId, name, email)
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)

        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                // Fetch the data back once saved to verify
                getUserData(userId)
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error writing user", e)
            }
    }

    fun getUserData(userId: String) {
        val userRef = database.child("users").child(userId)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    // Updating UI or logging the fetched data
                    Log.d("Firebase", "Fetched User: ${user.username}, Email: ${user.email}")
                    binding.tvStatus.text = "Last Saved: ${user.username}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "loadPost:onCancelled", error.toException())
            }
        })
    }
}