package com.example.androidbasics.api

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbasics.R
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

    }
}