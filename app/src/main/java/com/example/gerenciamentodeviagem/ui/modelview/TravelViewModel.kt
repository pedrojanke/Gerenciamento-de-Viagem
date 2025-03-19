package com.example.gerenciamentodeviagem.ui.modelview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciamentodeviagem.data.database.TravelDatabase
import com.example.gerenciamentodeviagem.data.model.Travel
import kotlinx.coroutines.launch

class TravelViewModel(application: Application) : AndroidViewModel(application) {
    private val travelDao = TravelDatabase.getDatabase(application).travelDao()

    val allTravels = travelDao.getAllTravels()

    fun addTravel(travel: Travel) {
        viewModelScope.launch {
            travelDao.insertTravel(travel)
        }
    }

    fun updateTravel(travel: Travel) {
        viewModelScope.launch {
            travelDao.updateTravel(travel)
        }
    }

    fun deleteTravel(travel: Travel) {
        viewModelScope.launch {
            travelDao.deleteTravel(travel)
        }
    }
}
