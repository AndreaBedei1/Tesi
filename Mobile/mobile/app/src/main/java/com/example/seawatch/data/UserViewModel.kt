package com.example.seawatch.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository): ViewModel() {
    val all=repository.all

    fun insert(user:User)=viewModelScope.launch {
        repository.insert(user)
    }

    fun deleteAll()=viewModelScope.launch {
        repository.deleteAll()
    }

    fun getUserByMail(mail:String)=viewModelScope.launch {
        repository.getUserByMail(mail)
    }

}