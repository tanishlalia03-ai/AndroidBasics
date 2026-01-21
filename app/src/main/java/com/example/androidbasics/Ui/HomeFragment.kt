package com.example.androidbasics.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.androidbasics.R
import com.example.androidbasics.databinding.FragmentHomeBinding
import com.example.androidbasics.databinding.AlertBoxBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        // Listener for the Default Alert Dialog
        binding.btnShowDialog.setOnClickListener {
            showDefaultAlertDialog()
        }


        binding.btnShowDialog2.setOnClickListener {
            showCustomAlertDialog()
        }
    }

    private fun showDefaultAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Test Dialog")
        builder.setMessage("This is a default Android alert. Is the app still running?")

        builder.setPositiveButton("Yes, it works!") { dialog, _ ->
            Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun showCustomAlertDialog() {
        val dialogBinding = AlertBoxBinding.inflate(LayoutInflater.from(requireContext()))

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding.root)

        val alertDialog = builder.create()
        dialogBinding.btnConfirm.setOnClickListener {
            val inputText = dialogBinding.etConfirm.text.toString()

            if (inputText.equals("DELETE", ignoreCase = false)) {
                Toast.makeText(requireContext(), "Item Deleted Successfully", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
                dialogBinding.etConfirm.error = "Please type DELETE to confirm"
            }
        }

        dialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        // 5. Show the dialog
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}