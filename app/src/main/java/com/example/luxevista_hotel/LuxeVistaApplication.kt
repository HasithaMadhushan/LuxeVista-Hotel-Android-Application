package com.example.luxevista_hotel

import android.app.Application
import com.example.luxevista_hotel.data.AppDatabase
import com.example.luxevista_hotel.data.LuxeVistaRepository

/**
 * The Application class is the entry point of the app.
 * We use it to initialize the database and repository lazily,
 * meaning they are only created when they are first needed.
 */
class LuxeVistaApplication : Application() {
    // Using 'lazy' ensures the database is created only once, when it's first accessed.
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    // The repository is also created lazily.
    // The entire LuxeVistaRepository(...) constructor call happens inside the 'lazy' block.
    val repository: LuxeVistaRepository by lazy {
        LuxeVistaRepository(
            database.userDao(),
            database.roomDao(),
            database.serviceDao(),
            database.bookingDao(),
            database.attractionDao()
        )
    }
}