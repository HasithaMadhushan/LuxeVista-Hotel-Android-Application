package com.example.luxevista_hotel.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        User::class,
        Room::class, // This must be com.example.luxevista_hotel.data.Room
        Service::class,
        Booking::class,
        Attraction::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun roomDao(): RoomDao
    abstract fun serviceDao(): ServiceDao
    abstract fun bookingDao(): BookingDao
    abstract fun attractionDao(): AttractionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "luxevista_database"
                )
                    .addCallback(AppDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(database: AppDatabase) {
            // Add sample rooms
            val roomDao = database.roomDao()
            roomDao.insertAll(
                listOf(
                    Room(type = "Ocean View Suite", description = "A luxurious suite with a stunning ocean view.", pricePerNight = 450.0, photoUrl = "https://example.com/ocean_view.jpg"),
                    Room(type = "Deluxe Room", description = "A spacious and comfortable room.", pricePerNight = 250.0, photoUrl = "https://example.com/deluxe.jpg"),
                    Room(type = "Garden View Room", description = "A peaceful room overlooking our beautiful gardens.", pricePerNight = 180.0, photoUrl = "https://example.com/garden_view.jpg"),
                    Room(type = "Presidential Suite", description = "Our most luxurious accommodation with panoramic ocean views, a private terrace, and a personal butler service.", pricePerNight = 1200.0, photoUrl = "https://example.com/presidential.jpg"),
                    Room(type = "Family Suite", description = "Spacious suite with two bedrooms, perfect for families with children.", pricePerNight = 550.0, photoUrl = "https://example.com/family.jpg"),
                    Room(type = "Honeymoon Suite", description = "Romantic suite with a king-size bed, jacuzzi, and champagne service.", pricePerNight = 650.0, photoUrl = "https://example.com/honeymoon.jpg"),
                    Room(type = "Standard Room", description = "Comfortable room with all essential amenities.", pricePerNight = 150.0, photoUrl = "https://example.com/standard.jpg"),
                    Room(type = "Penthouse", description = "Exclusive top-floor accommodation with 360-degree views and luxury furnishings.", pricePerNight = 950.0, photoUrl = "https://example.com/penthouse.jpg")
                )
            )

            // Add sample services
            val serviceDao = database.serviceDao()
            serviceDao.insertAll(
                listOf(
                    Service(name = "Deep Tissue Massage", description = "A 60-minute relaxing massage.", price = 120.0),
                    Service(name = "Fine Dining Reservation", description = "A guaranteed table at our 5-star restaurant.", price = 50.0),
                    Service(name = "Poolside Cabana", description = "A private cabana for a full day.", price = 100.0),
                    Service(name = "Sunset Yacht Tour", description = "A 2-hour yacht tour during sunset.", price = 300.0),
                    Service(name = "Private Beach Dinner", description = "Romantic dinner setup on the private beach.", price = 250.0),
                    Service(name = "Spa Day Package", description = "Full day of spa treatments including massage, facial, and body scrub.", price = 350.0),
                    Service(name = "Golf Course Access", description = "Full day access to our 18-hole championship golf course.", price = 150.0),
                    Service(name = "Tennis Court Rental", description = "2-hour private tennis court session with equipment.", price = 80.0)
                )
            )

            // Add sample attractions
            val attractionDao = database.attractionDao()
            attractionDao.insertAll(
                listOf(
                    Attraction(name = "Sunset Beach Tour", description = "A guided tour of the most beautiful sunset spots.", type = "Nearby Attraction", photoUrl = "https://example.com/sunset_beach.jpg"),
                    Attraction(name = "20% Off Spa Treatments", description = "Enjoy a discount on all spa services this weekend.", type = "Hotel Offer", photoUrl = "https://example.com/spa_offer.jpg"),
                    Attraction(name = "Coral Reef Diving", description = "Explore the vibrant coral reefs with professional diving instructors.", type = "Nearby Attraction", photoUrl = "https://example.com/diving.jpg"),
                    Attraction(name = "Local Cuisine Tour", description = "Taste the best local dishes with our culinary tour.", type = "Nearby Attraction", photoUrl = "https://example.com/cuisine.jpg"),
                    Attraction(name = "Free Welcome Drink", description = "Complimentary signature cocktail upon arrival.", type = "Hotel Offer", photoUrl = "https://example.com/welcome_drink.jpg"),
                    Attraction(name = "Historical City Tour", description = "Discover the rich history of the surrounding area.", type = "Nearby Attraction", photoUrl = "https://example.com/history_tour.jpg")
                )
            )
        }
    }
}