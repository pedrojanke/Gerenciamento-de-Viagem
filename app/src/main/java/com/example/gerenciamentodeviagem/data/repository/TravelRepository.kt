package com.example.gerenciamentodeviagem.data.repository

import com.example.gerenciamentodeviagem.data.models.Travel

class TravelRepository {
    private val travelList = mutableListOf<Travel>()
    fun getAllTravels(): List<Travel> = travelList.sortedBy { it.startDate }

    fun addTravel(travel: Travel) {
        travelList.add(travel)
    }

    fun removeTravel(travel: Travel) {
        travelList.remove(travel)
    }

    fun updateTravel(updatedTravel: Travel) {
        val index = travelList.indexOfFirst { it.id == updatedTravel.id }
        if (index != -1) {
            travelList[index] = updatedTravel
        }
    }
}
