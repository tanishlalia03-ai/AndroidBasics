package com.example.androidbasics.Recyler_view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidbasics.databinding.ActivityRecylerviewBinding

class RecyclerviewActivity : AppCompatActivity() , StudentAdapter.OnItemClickListener{

    private lateinit var adapter: StudentAdapter
    private var studentList = ArrayList<Student>()
    private lateinit var binding: ActivityRecylerviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRecylerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = StudentAdapter(studentList, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        addStudentData()
    }

    private fun addStudentData() {
        studentList.add(Student(1, "Tanish", 20, 100))
        studentList.add(Student(2, "king", 20, 100))
        studentList.add(Student(3, "Tanshi", 20, 100))

        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {
            Toast.makeText(this, studentList[position].id.toString(), Toast.LENGTH_SHORT).show()

    }
}