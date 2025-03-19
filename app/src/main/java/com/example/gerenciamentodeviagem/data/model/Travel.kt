package com.example.gerenciamentodeviagem.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "travel")
data class Travel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val destination: String,
    val type: String,
    val startDate: String,
    val endDate: String?,
    val budget: Double
)
