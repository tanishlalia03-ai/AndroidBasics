package com.example.androidbasics.Firebase

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbasics.R
import com.google.firebase.database.FirebaseDatabase

class RealDBAdapter(private val listOfSUsers: ArrayList<User>) :
    RecyclerView.Adapter<RealDBAdapter.ViewHolder>() {

    // Database reference to the "users" node
    private val database = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_fb, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfSUsers[position]
        val context = holder.itemView.context

        holder.name.text = item.username
        holder.email.text = item.email

        // 1. Create the unique Firebase Key (Email with commas)
        val firebaseKey = item.email?.replace(".", ",")

        // --- DELETE BUTTON LOGIC ---
        holder.deleteBtn.setOnClickListener {
            if (firebaseKey == null) return@setOnClickListener

            AlertDialog.Builder(context)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete ${item.username}?")
                .setPositiveButton("Yes") { _, _ ->
                    database.child(firebaseKey).removeValue().addOnSuccessListener {
                        Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show()

                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        // --- EDIT BUTTON LOGIC ---
        holder.editBtn.setOnClickListener {
            if (firebaseKey == null) return@setOnClickListener

            // Simple popup with EditText to change the name
            val editText = EditText(context)
            editText.setText(item.username)

            AlertDialog.Builder(context)
                .setTitle("Update Username")
                .setView(editText)
                .setPositiveButton("Save") { _, _ ->
                    val newName = editText.text.toString().trim()
                    if (newName.isNotEmpty()) {
                        // Update only the username field in Firebase
                        database.child(firebaseKey).child("username").setValue(newName)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun getItemCount(): Int = listOfSUsers.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nameTV)
        val email: TextView = view.findViewById(R.id.emailTV)

        val editBtn: ImageButton = view.findViewById(R.id.editBtn)
        val deleteBtn: ImageButton = view.findViewById(R.id.deleteBtn)
    }
}