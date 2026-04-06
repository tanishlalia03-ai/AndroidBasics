package com.example.androidbasics.excelSheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbasics.R
import com.example.androidbasics.recyclerviewTest.Employee
import com.google.android.material.button.MaterialButton
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class ExcelActivity : AppCompatActivity() {

    private lateinit var rvExcel: RecyclerView
    private lateinit var adapter: UserAdapter
    private val employeeList = ArrayList<Employee>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excel)

        rvExcel = findViewById(R.id.rvExcel)
        val btnExport = findViewById<MaterialButton>(R.id.btnExport)

        prepareData()

        // RecyclerView Setup
        adapter = UserAdapter(employeeList)
        rvExcel.layoutManager = LinearLayoutManager(this)
        rvExcel.adapter = adapter

        btnExport.setOnClickListener {
            generateExcelFile()
        }
    }

    private fun prepareData() {
        employeeList.add(Employee(1, "Tanish", "C.E.O"))
        employeeList.add(Employee(2, "John Doe", "Manager"))
        employeeList.add(Employee(3, "Jane Smith", "Developer"))
        employeeList.add(Employee(4, "Mike Ross", "Designer"))
        employeeList.add(Employee(5, "Rachel Zane", "HR"))
        employeeList.add(Employee(6, "Harvey Specter", "Senior Partner"))
        employeeList.add(Employee(7, "Donna Paulsen", "COO"))
        employeeList.add(Employee(8, "Louis Litt", "Partner"))
    }

    private fun generateExcelFile() {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Employee List")

        // 1. Create Header Row
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("ID")
        headerRow.createCell(1).setCellValue("Name")
        headerRow.createCell(2).setCellValue("Role")

        // 2. Fill Data from List
        for (i in employeeList.indices) {
            val row = sheet.createRow(i + 1)
            row.createCell(0).setCellValue(employeeList[i].id.toDouble())
            row.createCell(1).setCellValue(employeeList[i].name)
            row.createCell(2).setCellValue(employeeList[i].role)
        }

        // 3. Save to Internal Storage
        try {
            val fileName = "EmployeeRecords.xlsx"
            val file = File(getExternalFilesDir(null), fileName)
            val outputStream = FileOutputStream(file)
            workbook.write(outputStream)
            outputStream.close()
            workbook.close()

            Toast.makeText(this, "Excel Created!", Toast.LENGTH_SHORT).show()
            shareExcelFile(file)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun shareExcelFile(file: File) {
        // The authority MUST match your Manifest/
        val uri: Uri = FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(Intent.createChooser(intent, "Open Excel with..."))
    }
}