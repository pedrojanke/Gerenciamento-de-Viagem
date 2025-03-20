package com.example.travelapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "travels")
data class Travel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val destination: String,
    val type: String,  // "business" ou "leisure"
    val startDate: String,
    val endDate: String?,
    val budget: Double
)
