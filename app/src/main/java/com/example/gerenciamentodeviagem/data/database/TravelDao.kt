package com.example.gerenciamentodeviagem.data.database

import androidx.room.*
import com.example.gerenciamentodeviagem.data.model.Travel
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelDao {

    @Insert
    suspend fun insertTravel(travel: Travel)

    @Update
    suspend fun updateTravel(travel: Travel)

    @Delete
    suspend fun deleteTravel(travel: Travel)

    @Query("SELECT * FROM travel ORDER BY startDate ASC")
    suspend fun getAllTravels(): List<Travel> // A lista de viagens
}




