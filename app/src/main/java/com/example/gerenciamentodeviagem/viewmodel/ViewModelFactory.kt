package com.example.gerenciamentodeviagem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gerenciamentodeviagem.data.repository.TravelRepository
import com.example.gerenciamentodeviagem.data.repository.UserRepository

class ViewModelFactory(
    private val userRepository: UserRepository? = null,
    private val travelRepository: TravelRepository? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserViewModel::class.java) && userRepository != null -> {
                @Suppress("UNCHECKED_CAST")
                UserViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(TravelViewModel::class.java) && travelRepository != null -> {
                @Suppress("UNCHECKED_CAST")
                TravelViewModel(travelRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

