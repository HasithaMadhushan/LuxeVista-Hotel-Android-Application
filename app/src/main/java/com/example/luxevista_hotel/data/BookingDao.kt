package com.example.luxevista_hotel.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * DAO for the 'bookings' table.
 */
@Dao
interface BookingDao {
    @Insert
    suspend fun insertBooking(booking: Booking): Long

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY checkInDate DESC")
    fun getBookingsForUser(userId: Int): LiveData<List<Booking>>
    
    @Query("SELECT * FROM bookings WHERE id = :bookingId")
    fun getBookingById(bookingId: Int): LiveData<Booking>
    
    @Update
    suspend fun updateBooking(booking: Booking)
    
    @Query("UPDATE bookings SET bookingStatus = :status WHERE id = :bookingId")
    suspend fun updateBookingStatus(bookingId: Int, status: String)
    
    // Check if a room is available for the given date range
    @Query("SELECT COUNT(*) FROM bookings WHERE roomId = :roomId AND bookingStatus != 'Cancelled' AND ((checkInDate BETWEEN :checkIn AND :checkOut) OR (checkOutDate BETWEEN :checkIn AND :checkOut) OR (:checkIn BETWEEN checkInDate AND checkOutDate))")
    suspend fun isRoomBooked(roomId: Int, checkIn: Long, checkOut: Long): Int
    
    // Get all bookings for a specific room
    @Query("SELECT * FROM bookings WHERE roomId = :roomId AND bookingStatus != 'Cancelled' ORDER BY checkInDate ASC")
    fun getBookingsForRoom(roomId: Int): LiveData<List<Booking>>
    
    // Get upcoming bookings for a user
    @Query("SELECT * FROM bookings WHERE userId = :userId AND checkInDate > :currentTime AND bookingStatus = 'Confirmed' ORDER BY checkInDate ASC")
    fun getUpcomingBookingsForUser(userId: Int, currentTime: Long = System.currentTimeMillis()): LiveData<List<Booking>>
    
    // Get past bookings for a user
    @Query("SELECT * FROM bookings WHERE userId = :userId AND checkOutDate < :currentTime ORDER BY checkInDate DESC")
    fun getPastBookingsForUser(userId: Int, currentTime: Long = System.currentTimeMillis()): LiveData<List<Booking>>
}