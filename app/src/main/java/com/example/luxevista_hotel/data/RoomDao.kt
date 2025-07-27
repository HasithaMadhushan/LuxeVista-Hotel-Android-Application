package com.example.luxevista_hotel.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO for the 'rooms' table.
 */
@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rooms: List<Room>)

    @Query("SELECT * FROM rooms ORDER BY pricePerNight ASC")
    fun getAllRooms(): LiveData<List<Room>>

    // ADD THIS NEW FUNCTION
    @Query("SELECT * FROM rooms WHERE id = :roomId")
    fun getRoomById(roomId: Int): LiveData<Room>
    
    // Filter by room type
    @Query("SELECT * FROM rooms WHERE type LIKE '%' || :roomType || '%' ORDER BY pricePerNight ASC")
    fun getRoomsByType(roomType: String): LiveData<List<Room>>
    
    // Filter by price range
    @Query("SELECT * FROM rooms WHERE pricePerNight BETWEEN :minPrice AND :maxPrice ORDER BY pricePerNight ASC")
    fun getRoomsByPriceRange(minPrice: Double, maxPrice: Double): LiveData<List<Room>>
    
    // Sort by price (ascending)
    @Query("SELECT * FROM rooms ORDER BY pricePerNight ASC")
    fun getRoomsSortedByPriceAsc(): LiveData<List<Room>>
    
    // Sort by price (descending)
    @Query("SELECT * FROM rooms ORDER BY pricePerNight DESC")
    fun getRoomsSortedByPriceDesc(): LiveData<List<Room>>
    
    // Combined filter by type and price range
    @Query("SELECT * FROM rooms WHERE type LIKE '%' || :roomType || '%' AND pricePerNight BETWEEN :minPrice AND :maxPrice ORDER BY pricePerNight ASC")
    fun filterRooms(roomType: String, minPrice: Double, maxPrice: Double): LiveData<List<Room>>
}