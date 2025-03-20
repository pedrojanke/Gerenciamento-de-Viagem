package com.example.gerenciamentodeviagem.repository

import com.example.travelapp.data.database.TravelDao
import com.example.travelapp.data.model.Travel

class TravelRepository(private val travelDao: TravelDao) {

    // Função para obter uma viagem pelo ID
    suspend fun getTravelById(travelId: Int): Travel? {
        return travelDao.getTravelById(travelId)
    }

    // Função para adicionar uma nova viagem
    suspend fun addTravel(travel: Travel) {
        travelDao.insertTravel(travel)
    }

    // Função para atualizar uma viagem existente
    suspend fun updateTravel(travel: Travel) {
        travelDao.updateTravel(travel)
    }

    // Função para excluir uma viagem
    suspend fun deleteTravel(travel: Travel) {
        travelDao.deleteTravel(travel)
    }

    // Função para buscar todas as viagens
    suspend fun getAllTravels(): List<Travel> {
        return travelDao.getAllTravels()
    }
}
