package com.example.luxevista_hotel.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String = "",
    val preferredRoomType: String = "",
    val dietaryPreferences: String = "",
    val specialRequests: String = "",
    val loyaltyPoints: Int = 0,
    val profileImageUrl: String = "",
    val password: String
)