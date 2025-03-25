package com.example.gerenciamentodeviagem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciamentodeviagem.data.models.User
import com.example.gerenciamentodeviagem.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    fun registerUser(user: User, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.insertUser(user)
            onResult(true)
        }
    }

    fun loginUser(username: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUser(username, password)
            onResult(user)
        }
    }
}