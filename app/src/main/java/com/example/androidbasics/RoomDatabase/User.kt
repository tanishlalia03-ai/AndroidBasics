package com.example.androidbasics.RoomDatabase


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "user_Name") val name: String,
    val age: Int
)