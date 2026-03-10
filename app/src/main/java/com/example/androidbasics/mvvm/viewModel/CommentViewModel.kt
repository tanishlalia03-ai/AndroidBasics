package com.example.androidbasics.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidbasics.mvvm.model.Comment
import com.example.androidbasics.mvvm.model.CommentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentViewModel(private val repository: CommentRepository) : ViewModel() {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    val isLoading = MutableLiveData<Boolean>(false)

    fun fetchComments() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = repository.getComments()
                withContext(Dispatchers.Main){
                    if(response.isNotEmpty()){
                        _comments.postValue(response)
                    }else{
                        _errorMessage.postValue("Error: ${response}")
                    }
                    isLoading.value= false
                }
            } catch (e:Exception){
                _errorMessage.postValue(e.message ?:"An error occured")
                isLoading.value =false
            }

        }
    }
}