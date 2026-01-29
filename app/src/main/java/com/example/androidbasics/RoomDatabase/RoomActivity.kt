package com.example.androidbasics.RoomDatabase

import android.R.attr.targetId
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.androidbasics.R
import com.example.androidbasics.databinding.ActivityRoomBinding
import kotlinx.coroutines.launch

class RoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        // Fetch and Log existing users on startup
        lifecycleScope.launch {
            val userList = userDao.getAllUSer()
            Log.d("RoomDB", "List of Users: $userList")
        }

        binding.submitBtn.setOnClickListener {
            val name = binding.name.text.toString()

            val ageString = binding.age.text.toString()

            if (name.isNotEmpty() && ageString.isNotEmpty()) {
                val age = ageString.toInt()

                lifecycleScope.launch {
                    userDao.insertUser(User(name = name, age = age))

                    val updatedList = userDao.getAllUSer()
                    Log.d("RoomDB", "Updated List: $updatedList")


                }
            }
        }

        binding.btnDelete.setOnClickListener {
            val id = binding.etUserId.text.toString().toInt()

            lifecycleScope.launch {
                userDao.deleteUserById(id)

            }
        }
        binding.btnUpdate.setOnClickListener {
            // 1. Get the ID of the user you want to change
            val idToUpdate = binding.etUserId.text.toString().toInt()

            // 2. Get the new name you want to give them
            val newName = binding.name.text.toString()

            lifecycleScope.launch {
                // 3. Use the query you already wrote in UserDao
                userDao.updateUserName(userId = idToUpdate, newName = newName)

                Log.d("RoomDB", "Updated user $idToUpdate with new name: $newName")
            }
        }
    }
}

