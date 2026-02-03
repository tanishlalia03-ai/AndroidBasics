package com.example.androidbasics.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.androidbasics.R
import com.example.androidbasics.databinding.FragmentHomeBinding
import com.example.androidbasics.databinding.AlertBoxBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        binding.datePicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Tanish")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()


            datePicker.show(parentFragmentManager,"DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener { select->
                val dateString = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault()).format(Date(select))

                binding.datetext.text = "Selected Date: $dateString"
            }
        }


        binding.timePicker.setOnClickListener {

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select time")
                .build()


            timePicker.show(parentFragmentManager,"Time_Picker")

            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour
                val minute = timePicker.minute

                val formattedTime = String.format(Locale.getDefault(),"%02d:%02d",hour,minute)

                binding.timetext.text = "Selected Time: $formattedTime"
            }
        }


        binding.bottomSheetBtn.setOnClickListener {

//            creating dialog for bottom sheet
            val dialog = BottomSheetDialog(requireContext())

            var view = layoutInflater.inflate(R.layout.bottom_sheet_layout,null)

            var nameEt: EditText = view.findViewById<EditText>(R.id.nameET)
            var ageEt: EditText = view.findViewById<EditText>(R.id.ageET)

            view.findViewById<Button>(R.id.submitBtn).setOnClickListener {

                if(nameEt.text.toString().isEmpty()) {
                    nameEt.error = "Please enter name"
                } else if(ageEt.text.toString().isEmpty()) {
                    ageEt.error = "Please enter age"
                } else {
                    Toast.makeText(requireContext(), "Options are filled", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }


            view.findViewById<Button>(R.id.closeBtn).setOnClickListener {
                dialog.dismiss()
            }


            dialog.setContentView(view)
            dialog.show()
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