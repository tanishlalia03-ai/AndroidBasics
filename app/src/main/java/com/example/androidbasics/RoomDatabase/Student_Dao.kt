package com.example.androidbasics.RoomDatabase

import androidx.room.*
import com.example.androidbasics.Recyler_view.Student

@Dao
interface StudentDao {
    @Insert
    suspend fun insertStudent(student: Student)

    @Query("SELECT * FROM student_table")
    suspend fun getAllStudents(): List<Student>

    @Query("DELETE FROM student_table WHERE id = :studentId")
    suspend fun deleteStudentById(studentId: Int)

    @Update
    suspend fun updateStudent(student: Student)
}