package com.example.androidbasics.RecyclerviewTest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbasics.R

class employeeAdapter(private val employeeList: List<employee>) :
    RecyclerView.Adapter<employeeAdapter.EmployeeViewHolder>() {

        // Inner class to hold the views for each item
    class EmployeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvRole: TextView = view.findViewById(R.id.tvRole)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_test, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val currentEmployee = employeeList[position]
        holder.tvName.text = currentEmployee.name
        holder.tvRole.text = currentEmployee.role
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }
}