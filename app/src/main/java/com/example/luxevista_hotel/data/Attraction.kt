package com.example.luxevista_hotel.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attractions")
data class Attraction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val type: String,
    val photoUrl: String
)