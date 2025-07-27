package com.example.luxevista_hotel.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class Room(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val description: String,
    val pricePerNight: Double,
    val photoUrl: String
)