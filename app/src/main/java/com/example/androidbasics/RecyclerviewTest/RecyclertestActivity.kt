package com.example.androidbasics.RecyclerviewTest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbasics.R

class RecyclertestActivity : AppCompatActivity() {

    private lateinit var adapter: employeeAdapter
    private lateinit var recyclerView1: RecyclerView
    private val employeeList = ArrayList<employee>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclertest)

        // 1. Initialize RecyclerView
        recyclerView1 = findViewById(R.id.recyclerView1)

        // 2. Add Simple Sample Data
        employeeList.add(employee(1, "John Doe", "Manager"))
        employeeList.add(employee(2, "Jane Smith", "Developer"))
        employeeList.add(employee(3, "Mike Ross", "Designer"))
        employeeList.add(employee(4, "Rachel Zane", "HR"))

        // 3. Setup Adapter
        adapter = employeeAdapter(employeeList)
        recyclerView1.layoutManager = LinearLayoutManager(this)
        recyclerView1.adapter = adapter
    }
}