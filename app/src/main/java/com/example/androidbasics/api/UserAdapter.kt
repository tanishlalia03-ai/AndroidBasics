package com.example.androidbasics.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbasics.R

// UserItem is the ArrayList class you created earlier
class UserAdapter(private val userList: Users) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // ViewHolder holds the references to the views in item_user1.xml
    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvUserName)
        val tvEmail: TextView = view.findViewById(R.id.tvUserEmail)
        val tvCity: TextView = view.findViewById(R.id.tvUserCity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // We use your specific layout name here: item_user1
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user1, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        // Setting the data to the TextViews
        holder.tvName.text = currentUser.name
        holder.tvEmail.text = currentUser.email

        // Accessing the nested Address object to get the City
        holder.tvCity.text = "City: ${currentUser.address?.city}"
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}