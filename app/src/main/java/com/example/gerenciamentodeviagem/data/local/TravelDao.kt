package com.example.gerenciamentodeviagem.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gerenciamentodeviagem.data.models.Travel

@Dao
interface TravelDao {

    @Query("SELECT * FROM travels WHERE userId = :userId ORDER BY startDate")
    suspend fun getTravelsByUserId(userId: Int): List<Travel>

    @Query("SELECT * FROM travels ORDER BY startDate")
    fun getAllTravels(): List<Travel>

    @Query("SELECT * FROM travels WHERE id = :travelId LIMIT 1")
    fun getTravelById(travelId: Int): Travel?

    @Insert
    suspend fun addTravel(travel: Travel)

    @Update
    suspend fun updateTravel(travel: Travel)

    @Delete
    suspend fun deleteTravel(travel: Travel)
}
