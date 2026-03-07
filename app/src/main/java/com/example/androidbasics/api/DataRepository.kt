package com.example.androidbasics.api

class DataRepository(val apiInterface: ApiInterface) {
    suspend fun  getUsers(): List<UsersItem> = apiInterface.getUsers()

}