package com.example.luxevista_hotel.data

import androidx.lifecycle.LiveData

/**
 * The Repository is the single source of truth for all app data.
 * It abstracts the data sources (database, network) from the rest of the app.
 * The UI and ViewModels will only interact with the Repository, not the DAOs directly.
 * We pass all our DAOs into its constructor.
 */
class LuxeVistaRepository(
    private val userDao: UserDao,
    private val roomDao: RoomDao,
    private val serviceDao: ServiceDao,
    private val bookingDao: BookingDao,
    private val attractionDao: AttractionDao
) {

    // --- Room Functions ---
    // This function gets all rooms from the database via the RoomDao.
    // The result is LiveData, so the UI can observe it for changes.
    val allRooms: LiveData<List<Room>> = roomDao.getAllRooms()
    
    // Filter rooms by type
    fun getRoomsByType(roomType: String): LiveData<List<Room>> {
        return roomDao.getRoomsByType(roomType)
    }
    
    // Filter rooms by price range
    fun getRoomsByPriceRange(minPrice: Double, maxPrice: Double): LiveData<List<Room>> {
        return roomDao.getRoomsByPriceRange(minPrice, maxPrice)
    }
    
    // Sort rooms by price (ascending)
    fun getRoomsSortedByPriceAsc(): LiveData<List<Room>> {
        return roomDao.getRoomsSortedByPriceAsc()
    }
    
    // Sort rooms by price (descending)
    fun getRoomsSortedByPriceDesc(): LiveData<List<Room>> {
        return roomDao.getRoomsSortedByPriceDesc()
    }
    
    // Combined filter
    fun filterRooms(roomType: String, minPrice: Double, maxPrice: Double): LiveData<List<Room>> {
        return roomDao.filterRooms(roomType, minPrice, maxPrice)
    }

    // --- Service Functions ---
    val allServices: LiveData<List<Service>> = serviceDao.getAllServices()
    
    fun getServiceById(serviceId: Int): LiveData<Service> {
        return serviceDao.getServiceById(serviceId)
    }
    
    fun searchServices(query: String): LiveData<List<Service>> {
        return serviceDao.searchServices(query)
    }
    
    fun getServicesSortedByPriceAsc(): LiveData<List<Service>> {
        return serviceDao.getServicesSortedByPriceAsc()
    }
    
    fun getServicesSortedByPriceDesc(): LiveData<List<Service>> {
        return serviceDao.getServicesSortedByPriceDesc()
    }
    
    suspend fun isServiceAvailable(serviceId: Int, date: Long): Boolean {
        val bookingCount = serviceDao.isServiceBooked(serviceId, date)
        return bookingCount == 0
    }
    
    suspend fun bookService(userId: Int, serviceId: Int, date: Long, specialRequests: String, servicePrice: Double): Long {
        val booking = Booking(
            userId = userId,
            roomId = null,
            serviceId = serviceId,
            checkInDate = date,
            checkOutDate = date, // For services, check-in and check-out are the same day
            specialRequests = specialRequests,
            totalPrice = servicePrice
        )
        return bookingDao.insertBooking(booking)
    }

    // --- Attraction Functions ---
    val allAttractions: LiveData<List<Attraction>> = attractionDao.getAllAttractions()
    
    fun getAttractionById(attractionId: Int): LiveData<Attraction> {
        return attractionDao.getAttractionById(attractionId)
    }
    
    fun getAttractionsByType(type: String): LiveData<List<Attraction>> {
        return attractionDao.getAttractionsByType(type)
    }
    
    fun getRecommendedAttractionsForUser(userId: Int): LiveData<List<Attraction>> {
        return attractionDao.getRecommendedAttractionsForUser(userId)
    }

    // --- User Functions ---
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    fun getUserByEmail(email: String): LiveData<User> {
        return userDao.getUserByEmail(email)
    }
    
    fun getUserById(userId: Int): LiveData<User> {
        return userDao.getUserById(userId)
    }
    
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
    
    suspend fun updatePreferredRoomType(userId: Int, preferredRoomType: String) {
        userDao.updatePreferredRoomType(userId, preferredRoomType)
    }
    
    suspend fun updateDietaryPreferences(userId: Int, dietaryPreferences: String) {
        userDao.updateDietaryPreferences(userId, dietaryPreferences)
    }
    
    suspend fun updateSpecialRequests(userId: Int, specialRequests: String) {
        userDao.updateSpecialRequests(userId, specialRequests)
    }
    
    suspend fun updatePhoneNumber(userId: Int, phoneNumber: String) {
        userDao.updatePhoneNumber(userId, phoneNumber)
    }
    
    suspend fun addLoyaltyPoints(userId: Int, points: Int) {
        userDao.addLoyaltyPoints(userId, points)
    }

    suspend fun getUserByIdNow(userId: Int): User? {
        return userDao.getUserByIdNow(userId)
    }

    fun getUserByEmailAndPassword(email: String, password: String): LiveData<User> {
        return userDao.getUserByEmailAndPassword(email, password)
    }

    // --- Booking Functions ---
    suspend fun insertBooking(booking: Booking): Long {
        return bookingDao.insertBooking(booking)
    }

    fun getBookingsForUser(userId: Int): LiveData<List<Booking>> {
        return bookingDao.getBookingsForUser(userId)
    }
    
    fun getBookingById(bookingId: Int): LiveData<Booking> {
        return bookingDao.getBookingById(bookingId)
    }
    
    suspend fun updateBooking(booking: Booking) {
        bookingDao.updateBooking(booking)
    }
    
    suspend fun updateBookingStatus(bookingId: Int, status: String) {
        bookingDao.updateBookingStatus(bookingId, status)
    }
    
    suspend fun isRoomAvailable(roomId: Int, checkIn: Long, checkOut: Long): Boolean {
        val bookingCount = bookingDao.isRoomBooked(roomId, checkIn, checkOut)
        return bookingCount == 0
    }
    
    fun getBookingsForRoom(roomId: Int): LiveData<List<Booking>> {
        return bookingDao.getBookingsForRoom(roomId)
    }
    
    fun getUpcomingBookingsForUser(userId: Int): LiveData<List<Booking>> {
        return bookingDao.getUpcomingBookingsForUser(userId)
    }
    
    fun getPastBookingsForUser(userId: Int): LiveData<List<Booking>> {
        return bookingDao.getPastBookingsForUser(userId)
    }
    
    // ADD THIS NEW FUNCTION
    fun getRoomById(id: Int): LiveData<Room> {
        return roomDao.getRoomById(id)
    }
    // In a real app, you would have functions here to fetch data from a network.
    // The Repository would decide whether to return fresh data from the network
    // or cached data from the Room database.
}