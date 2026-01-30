package com.example.androidbasics.Recyler_view

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidbasics.R
import com.example.androidbasics.RoomDatabase.AppDatabase
import com.example.androidbasics.databinding.ActivityRecylerviewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerviewActivity : AppCompatActivity(), StudentAdapter.OnItemClickListener {

    private lateinit var adapter: StudentAdapter
    private lateinit var database: AppDatabase // Global property
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

        // 1. Initialize the global database instance
        database = AppDatabase.getDatabase(this)
        val studentDao = database.studentDao()

        // 2. Setup RecyclerView
        adapter = StudentAdapter(studentList, studentDao, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // 3. Add Student Button Logic
        binding.btnAddStudent.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update, null)
            val dialog = AlertDialog.Builder(this).setView(dialogView).create()

            val etName = dialogView.findViewById<EditText>(R.id.etUpdateName)
            val etAge = dialogView.findViewById<EditText>(R.id.etUpdateAge)
            val etMarks = dialogView.findViewById<EditText>(R.id.etUpdateMarks)
            val btnSubmit = dialogView.findViewById<Button>(R.id.btnUpdate)

            btnSubmit.text = "Save Student"

            btnSubmit.setOnClickListener {
                val name = etName.text.toString()
                val age = etAge.text.toString().toIntOrNull() ?: 0
                val marks = etMarks.text.toString().toIntOrNull() ?: 0

                // Insert into Room
                CoroutineScope(Dispatchers.IO).launch {
                    val newStudent = Student(0, name, age, marks)
                    database.studentDao().insertStudent(newStudent)

                    withContext(Dispatchers.Main) {
                        addStudentData()
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }

        addStudentData()
    }

    private fun addStudentData() {
        CoroutineScope(Dispatchers.IO).launch {
            val studentsFromDb = database.studentDao().getAllStudents()
            withContext(Dispatchers.Main) {
                studentList.clear()
                studentList.addAll(studentsFromDb)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onItemClick(position: Int) {
        // Toast commented out as requested
    }
}