package com.example.androidbasics.mvvm.model

class CommentRepository(private val apiService: ApiInterface) {
    suspend fun getComments() = apiService.getComments()
}