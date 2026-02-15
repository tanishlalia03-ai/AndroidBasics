package com.example.androidbasics.Firebase

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidbasics.R
import com.example.androidbasics.databinding.ActivityRecyclerDbactivityBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class RecyclerDBActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerDbactivityBinding
    private lateinit var adapter: RealDBAdapter
    private var listOfUsers = arrayListOf<User>()
    private var db = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRecyclerDbactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Setup RecyclerView
        adapter = RealDBAdapter(listOfUsers)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.addFAB.setOnClickListener {
            showAddUserDialog()
        }

        getUserData()
    }

    private fun showAddUserDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null)

        val etName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editName)
        val etEmail = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editEmail)

        builder.setView(dialogView)
            .setTitle("Add New User")
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()

                if (name.isNotEmpty() && email.isNotEmpty()) {
                    val userId = email.replace(".", ",")
                    val newUser = User(name, email)

                    db.child("users").child(userId).setValue(newUser)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun getUserData() {
        db.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfUsers.clear() // Prevent duplicates
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let { listOfUsers.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", error.message)
            }
        })

    }
}