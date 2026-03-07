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

    private lateinit var viewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_getuserdata)

        // Handle system bar padding for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.userRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 2. Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiInterface::class.java)

        var repo= DataRepository(api)
        viewModel= DataViewModel(repo)


        viewModel.userList.observe(this){ userList ->
            Log.d("user List", userList.toString())

        }

        viewModel.error.observe(this){error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        viewModel.getUsersInViewModel()

        // 3. Fetch Data using Coroutines
//        lifecycleScope.launch {
//            try {
//                // Network call on IO thread
//                val responseList = withContext(Dispatchers.IO) {
//                    api.getUsers()
//                }
//
//                // 4. Update UI on Main thread
//                withContext(Dispatchers.Main) {
//                    if (responseList != null && responseList.isNotEmpty()) {
//                        Log.d("API_RESULT", "Users found: ${responseList.size}")
//
//                        // Safely convert standard List to your custom UserItem class
//                        val displayData = Users()
//                        displayData.addAll(responseList)
//
//                        // Set the adapter
//                        val adapter = UserAdapter(displayData)
//                        recyclerView.adapter = adapter
//                    } else {
//                        Toast.makeText(this@GetuserdataActivity, "No data found", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//            } catch (e: Exception) {
//                // This catches the "Java error" (ClassCast) or Network errors
//                Log.e("API_ERROR", "Error: ${e.message}")
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@GetuserdataActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
    }
}