package com.example.luxevista_hotel.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * DAO for the 'users' table.
 */
@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)


    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): LiveData<User>
    
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): LiveData<User>
    
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserByIdNow(userId: Int): User?
    
    @Update
    suspend fun updateUser(user: User)
    
    @Query("UPDATE users SET preferredRoomType = :preferredRoomType WHERE id = :userId")
    suspend fun updatePreferredRoomType(userId: Int, preferredRoomType: String)
    
    @Query("UPDATE users SET dietaryPreferences = :dietaryPreferences WHERE id = :userId")
    suspend fun updateDietaryPreferences(userId: Int, dietaryPreferences: String)
    
    @Query("UPDATE users SET specialRequests = :specialRequests WHERE id = :userId")
    suspend fun updateSpecialRequests(userId: Int, specialRequests: String)
    
    @Query("UPDATE users SET phoneNumber = :phoneNumber WHERE id = :userId")
    suspend fun updatePhoneNumber(userId: Int, phoneNumber: String)
    
    @Query("UPDATE users SET loyaltyPoints = loyaltyPoints + :points WHERE id = :userId")
    suspend fun addLoyaltyPoints(userId: Int, points: Int)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    fun getUserByEmailAndPassword(email: String, password: String): LiveData<User>
}