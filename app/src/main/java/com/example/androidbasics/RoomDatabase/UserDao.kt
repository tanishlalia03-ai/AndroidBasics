package com.example.androidbasics.RoomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UserDao {


    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    suspend fun getAllUSer(): List<User>



    @Query("UPDATE user_table SET user_Name = :newName WHERE id = :userId")
    suspend fun updateUserName(userId: Int, newName: String)


    @Query("DELETE FROM user_table WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)



}