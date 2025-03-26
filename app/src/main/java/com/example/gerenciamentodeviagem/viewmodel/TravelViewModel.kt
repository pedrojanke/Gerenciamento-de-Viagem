package com.example.gerenciamentodeviagem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.gerenciamentodeviagem.data.models.Travel
import com.example.gerenciamentodeviagem.data.repository.TravelRepository

class TravelViewModel : ViewModel() {
    private val repository = TravelRepository()
    var travels = mutableStateListOf<Travel>()
        private set

    init {
        travels.addAll(repository.getAllTravels())
    }

    fun addTravel(travel: Travel) {
        repository.addTravel(travel)
        travels.add(travel)
    }

    fun removeTravel(travel: Travel) {
        repository.removeTravel(travel)
        travels.remove(travel)
    }
}