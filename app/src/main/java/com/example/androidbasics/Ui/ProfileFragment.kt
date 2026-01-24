package com.example.androidbasics.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.example.androidbasics.R


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivedName = arguments?.getString("user_name")

        if (receivedName != null) {
            Toast.makeText(requireContext(), "Passed Name: $receivedName", Toast.LENGTH_LONG).show()
        }
    }
}