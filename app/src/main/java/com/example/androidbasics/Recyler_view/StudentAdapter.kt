package com.example.androidbasics.Recyler_view

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbasics.R
import com.example.androidbasics.RoomDatabase.StudentDao

class StudentAdapter(var list : ArrayList<Student>,
                     private val studentDao: StudentDao,
                     var itemClickListener: OnItemClickListener
                    ): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val item = list[position]

        holder.name.text = item.name
        holder.age.text = item.age.toString()
        holder.marks.text = item.marks.toString()

        // --- DELETE BUTTON LOGIC ---
        holder.btnDelete.setOnClickListener {
            val context = holder.itemView.context
            AlertDialog.Builder(context)
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete ${item.name}?")
                .setPositiveButton("Yes") { _, _ ->
                    list.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, list.size)
                }
                .setNegativeButton("No", null)
                .show()

        }

        // --- UPDATE BUTTON LOGIC ---
        holder.btnUpdate.setOnClickListener {
            val context = holder.itemView.context
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.dialog_update, null)

            val etName = dialogView.findViewById<EditText>(R.id.etUpdateName)
            val etMarks = dialogView.findViewById<EditText>(R.id.etUpdateMarks)
            val etAge = dialogView.findViewById<EditText>(R.id.etUpdateAge)

            // Fill the current data into the EditTexts
            etName.setText(item.name)
            etMarks.setText(item.marks.toString())
            etAge.setText(item.age.toString())

            AlertDialog.Builder(context)
                .setView(dialogView)
                .setPositiveButton("Update") { _, _ ->
                    // Update the local list
                    item.name = etName.text.toString()
                    item.marks = etMarks.text.toString().toInt()
                    item.age = etAge.text.toString().toInt()

                    notifyItemChanged(position)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class StudentViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val age = view.findViewById<TextView>(R.id.age)
        val name = view.findViewById<TextView>(R.id.name)
        val marks = view.findViewById<TextView>(R.id.marks)

        val btnUpdate = view.findViewById<ImageButton>(R.id.btnEdit)
        val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)
    }
}