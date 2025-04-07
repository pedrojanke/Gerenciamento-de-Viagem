package com.example.gerenciamentodeviagem.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "travels")
data class Travel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val destination: String,
    val type: TravelType,
    val startDate: Date,
    val endDate: Date?,
    val budget: Double
)

enum class TravelType {
    BUSINESS, LEISURE
}
