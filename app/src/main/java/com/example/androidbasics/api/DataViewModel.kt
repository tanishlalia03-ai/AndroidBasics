package com.example.androidbasics.api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DataViewModel(val repository: DataRepository): ViewModel(){
    var userList = MutableLiveData<List<UsersItem>>()
    var error= MutableLiveData<String>()


    fun getUsersInViewModel(){
        viewModelScope.launch {
            try {
                userList.value = repository.getUsers()
            }catch (e : Exception){
                error.value= e.toString()
            }

        }
    }
}