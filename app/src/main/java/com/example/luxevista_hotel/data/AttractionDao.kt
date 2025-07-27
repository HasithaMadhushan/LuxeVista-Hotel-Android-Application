package com.example.luxevista_hotel.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO for the 'attractions' table.
 */
@Dao
interface AttractionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attractions: List<Attraction>)

    @Query("SELECT * FROM attractions")
    fun getAllAttractions(): LiveData<List<Attraction>>
    
    @Query("SELECT * FROM attractions WHERE id = :attractionId")
    fun getAttractionById(attractionId: Int): LiveData<Attraction>
    
    @Query("SELECT * FROM attractions WHERE type = :type")
    fun getAttractionsByType(type: String): LiveData<List<Attraction>>
    
    // Get recommended attractions based on user preferences
    // In a real app, this would be more sophisticated and consider user history
    @Query("SELECT a.* FROM attractions a " +
           "JOIN users u ON u.preferredRoomType LIKE '%' || a.name || '%' OR " +
           "a.type LIKE '%' || u.preferredRoomType || '%' " +
           "WHERE u.id = :userId " +
           "UNION " +
           "SELECT * FROM attractions " +
           "ORDER BY name ASC " +
           "LIMIT 5")
    fun getRecommendedAttractionsForUser(userId: Int): LiveData<List<Attraction>>
}