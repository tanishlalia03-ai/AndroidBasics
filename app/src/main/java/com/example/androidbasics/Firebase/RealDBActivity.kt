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

        // SAVE BUTTON
        binding.btnLogin.setOnClickListener {
            val name = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                // We use the email as the ID (replacing dots because Firebase doesn't allow them in keys)
                val userId = email.replace(".", ",")
                writeNewUser(userId, name, email)
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // UPDATE BUTTON
        binding.btnUpdate.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val newName = binding.etUsername.text.toString().trim()

            if (email.isNotEmpty() && newName.isNotEmpty()) {
                // Use the same email-to-ID logic to find the record
                val userId = email.replace(".", ",")
                updateUserName(userId, newName)
            } else {
                Toast.makeText(this, "Please enter the Email and New Name", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDelete.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            if (email.isNotEmpty()) {
                val userId = email.replace(".", ",")
                deleteUser(userId)
            } else {
                Toast.makeText(this, "Enter Email to delete user", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)

        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "User Saved!", Toast.LENGTH_SHORT).show()
                getUserData(userId)
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error writing user", e)
            }
    }

    fun updateUserName(userId: String, newName: String) {
        val userRef = database.child("users").child(userId)

        // Using "username" because that is what is in your User data class
        val updates = mapOf<String, Any>(
            "username" to newName
        )

        userRef.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Update Successful!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Update failed - User might not exist", e)
            }
    }

    fun deleteUser(userId: String) {
        val userRef = database.child("users").child(userId)

        userRef.removeValue()
            .addOnSuccessListener {
                binding.etUsername.text.clear()
                binding.etEmail.text.clear()
                binding.tvStatus.text = "Status: User Deleted"
                Toast.makeText(this, "User deleted successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Delete failed", e)
            }
    }

    fun getUserData(userId: String) {
        val userRef = database.child("users").child(userId)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    Log.d("Firebase", "Fetched User: ${user.username}")
                    binding.tvStatus.text = "Status: ${user.username} (${user.email})"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Firebase", "onCancelled", error.toException())
            }
        })
    }
}