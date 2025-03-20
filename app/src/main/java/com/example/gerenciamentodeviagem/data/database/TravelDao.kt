package com.example.travelapp.data.database

import androidx.room.*
import com.example.travelapp.data.model.Travel
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelDao {

    // Função para buscar uma viagem pelo ID
    @Query("SELECT * FROM travels WHERE id = :travelId")
    suspend fun getTravelById(travelId: Int): Travel?

    // Função para inserir uma viagem
    @Insert
    suspend fun insertTravel(travel: Travel)

    // Função para atualizar uma viagem
    @Update
    suspend fun updateTravel(travel: Travel)

    // Função para excluir uma viagem
    @Delete
    suspend fun deleteTravel(travel: Travel)

    // Função para buscar todas as viagens
    @Query("SELECT * FROM travels")
    suspend fun getAllTravels(): List<Travel>
}

