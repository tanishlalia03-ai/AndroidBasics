package com.example.androidbasics.mvvm.model

import retrofit2.http.GET

interface ApiInterface {
    @GET("comments")
    suspend fun getComments(): List<Comment>
}