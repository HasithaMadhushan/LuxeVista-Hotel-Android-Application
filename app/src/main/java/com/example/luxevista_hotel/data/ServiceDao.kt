package com.example.luxevista_hotel.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO for the 'services' table.
 */
@Dao
interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(services: List<Service>)

    @Query("SELECT * FROM services ORDER BY name ASC")
    fun getAllServices(): LiveData<List<Service>>
    
    @Query("SELECT * FROM services WHERE id = :serviceId")
    fun getServiceById(serviceId: Int): LiveData<Service>
    
    @Query("SELECT * FROM services WHERE name LIKE '%' || :query || '%'")
    fun searchServices(query: String): LiveData<List<Service>>
    
    @Query("SELECT * FROM services ORDER BY price ASC")
    fun getServicesSortedByPriceAsc(): LiveData<List<Service>>
    
    @Query("SELECT * FROM services ORDER BY price DESC")
    fun getServicesSortedByPriceDesc(): LiveData<List<Service>>
    
    // Check service availability (based on bookings)
    @Query("SELECT COUNT(*) FROM bookings WHERE serviceId = :serviceId AND bookingStatus != 'Cancelled' AND checkInDate = :date")
    suspend fun isServiceBooked(serviceId: Int, date: Long): Int
}