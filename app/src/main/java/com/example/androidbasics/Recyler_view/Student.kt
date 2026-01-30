package com.example.androidbasics.Recyler_view

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_table")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var marks: Int,
    var age: Int
)