package com.example.androidbasics.api

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbasics.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GetuserdataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_getuserdata)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Initialize RecyclerView (Make sure the ID in activity_getuserdata.xml matches)
        val recyclerView = findViewById<RecyclerView>(R.id.userRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 2. Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiInterface::class.java)

        // 3. Launch the Coroutine to fetch data
        lifecycleScope.launch {
            try {
                // Fetch data on IO thread
                val userListResponse = withContext(Dispatchers.IO) {
                    api.getUsers()
                }

                // 4. Update UI on Main thread
                withContext(Dispatchers.Main) {
                    Log.d("API_RESULT", "User list size: ${userListResponse.size}")

                    // Create the adapter and set it to RecyclerView
                    // Note: Ensure your 'api.getUsers()' returns the 'UserItem' list type
                    val adapter = UserAdapter(userListResponse as UserItem)
                    recyclerView.adapter = adapter
                }

            } catch (e: Exception) {
                Log.e("API_ERROR", "Message: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@GetuserdataActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}