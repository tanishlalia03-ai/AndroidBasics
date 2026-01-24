package com.example.androidbasics.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.androidbasics.R
import com.example.androidbasics.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment(R.layout.fragment_notification) {
    private lateinit var binding: FragmentNotificationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNotificationBinding.bind(view)

        val options = arrayListOf("Tanish", "King", "Champion", "Tanshi")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        binding.spinnerAtTop.adapter = adapter

        binding.spinnerAtTop.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = options[position]
                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.navBtn.setOnClickListener {
            val selectedName = binding.spinnerAtTop.selectedItem.toString()

            val bundle = Bundle()
            bundle.putString("userName", selectedName)


            findNavController().navigate(R.id.navigation_profile, bundle)
        }
        }
    }
