package com.example.gerenciamentodeviagem.data.repository

import com.example.gerenciamentodeviagem.data.local.TravelDao
import com.example.gerenciamentodeviagem.data.models.Travel

class TravelRepository(private val appDatabase: TravelDao) {

    suspend fun getAllTravels(): List<Travel> = appDatabase.getAllTravels()

    suspend fun addTravel(travel: Travel) {
        appDatabase.addTravel(travel)
    }

    suspend fun removeTravel(travel: Travel) {
        appDatabase.deleteTravel(travel)
    }

    suspend fun updateTravel(updatedTravel: Travel) {
        appDatabase.updateTravel(updatedTravel)
    }
}
