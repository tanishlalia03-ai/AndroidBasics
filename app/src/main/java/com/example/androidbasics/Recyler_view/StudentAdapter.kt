package com.example.androidbasics.Recyler_view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbasics.R

class StudentAdapter(var list : ArrayList<Student>): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student,parent,false)
        return StudentViewHolder(view)

    }

    override fun onBindViewHolder(
        holder: StudentViewHolder,
        position: Int
    ) {
        var item = list[position]

        holder.name.text = item.name
        holder.age.text = item.age.toString()
        holder.marks.text = item.marks.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class StudentViewHolder(val view: View) : RecyclerView.ViewHolder(view){

        val age = view.findViewById<TextView>(R.id.age)
        val name = view.findViewById<TextView>(R.id.name)
        val marks = view.findViewById<TextView>(R.id.marks)



    }
}