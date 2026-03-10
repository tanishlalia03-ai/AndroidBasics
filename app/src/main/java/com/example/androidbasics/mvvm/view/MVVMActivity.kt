package com.example.androidbasics.mvvm.view

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidbasics.R
import com.example.androidbasics.databinding.ActivityMvvmactivityBinding
import com.example.androidbasics.mvvm.model.CommentRepository
import com.example.androidbasics.mvvm.model.RetrofitInstance
import com.example.androidbasics.mvvm.viewModel.CommentViewModel

class MVVMActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMvvmactivityBinding
    private lateinit var viewModel: CommentViewModel
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewBinding
        binding = ActivityMvvmactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupViewModel()
        observeData()

        viewModel.fetchComments()
    }

    private fun setupRecyclerView() {
        commentAdapter = CommentAdapter()
        binding.recyclerView.apply { // Assuming your XML has a RecyclerView with ID 'recyclerView'
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(this@MVVMActivity)
        }
    }

    private fun setupViewModel() {
        val apiService = RetrofitInstance.api
        val repository = CommentRepository(apiService)
        val factory = CommentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CommentViewModel::class.java]
    }

    private fun observeData() {
        viewModel.comments.observe(this) { list ->
            if (list != null) {
                Log.d("MVVMActivity", "Fetched ${list.size} comments")
                commentAdapter.submitList(list)
            }
        }
    }
}

class CommentViewModelFactory(private val repository: CommentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}