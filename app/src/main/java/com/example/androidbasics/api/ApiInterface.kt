package com.example.androidbasics.api

import retrofit2.http.GET

interface ApiInterface {

    @GET("users")
    suspend fun getUsers(): List<UsersItem>
}