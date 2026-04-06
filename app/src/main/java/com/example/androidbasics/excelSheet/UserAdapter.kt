package com.example.androidbasics.excelSheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbasics.R
import com.example.androidbasics.recyclerviewTest.Employee

class UserAdapter(private val list: List<Employee>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Updated IDs to match your item_excel_row.xml
        val tvId: TextView = view.findViewById(R.id.tvColId)
        val tvName: TextView = view.findViewById(R.id.tvColName)
        val tvEmail: TextView = view.findViewById(R.id.tvColEmail)
        val tvRole: TextView = view.findViewById(R.id.tvColPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // Inflate the excel row layout instead of item_user
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_excel_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = list[position]

        // Binding data to the specific columns
        holder.tvId.text = item.id.toString()
        holder.tvName.text = item.name
        holder.tvRole.text = item.role

        // Placeholder for Email since Employee model might not have it yet
        holder.tvEmail.text = "${item.name.lowercase()}@company.com"

        holder.itemView.setOnClickListener {
            Toast.makeText(it.context, "Clicked ${item.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = list.size
}