package com.example.androidbasics.Bottomnavigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.androidbasics.R
import com.example.androidbasics.databinding.ActivityBottomLayoutBinding

class BottomLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup View Binding
        binding = ActivityBottomLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }
}