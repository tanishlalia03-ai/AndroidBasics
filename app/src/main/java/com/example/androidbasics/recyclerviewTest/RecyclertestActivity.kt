package com.example.androidbasics.recyclerviewTest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbasics.R

class RecyclertestActivity : AppCompatActivity() {

    private lateinit var adapter: EmployeeAdapter
    private lateinit var recyclerView1: RecyclerView
    private val employeeList = ArrayList<Employee>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclertest)

        // 1. Initialize RecyclerView
        recyclerView1 = findViewById(R.id.recyclerView1)

        // 2. Add Simple Sample Data
        employeeList.add(Employee(1, "John Doe", "Manager"))
        employeeList.add(Employee(2, "Jane Smith", "Developer"))
        employeeList.add(Employee(3, "Mike Ross", "Designer"))
        employeeList.add(Employee(4, "Rachel Zane", "HR"))
        employeeList.add(Employee(5, "Tanish", "C.E.O"))
        employeeList.add(Employee(6, "Rachel Zane", "HR"))
        employeeList.add(Employee(7, "Mike Ross", "Designer"))
        employeeList.add(Employee(8, "Rachel Zane", "HR"))
        employeeList.add(Employee(9, "Tanish", "C.E.O"))


        // 3. Setup Adapter
        adapter = EmployeeAdapter(employeeList)
        recyclerView1.layoutManager = LinearLayoutManager(this)
        recyclerView1.adapter = adapter

    }
}