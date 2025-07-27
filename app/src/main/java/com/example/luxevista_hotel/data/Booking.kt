package com.example.luxevista_hotel.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val roomId: Int?,
    val serviceId: Int?,
    val checkInDate: Long,
    val checkOutDate: Long,
    val numberOfGuests: Int = 1,
    val specialRequests: String = "",
    val totalPrice: Double = 0.0,
    val bookingStatus: String = "Confirmed",
    val createdAt: Long = System.currentTimeMillis()
)