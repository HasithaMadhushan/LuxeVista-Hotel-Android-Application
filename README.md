# LuxeVista Hotel - Android Application

A comprehensive luxury hotel booking and management Android application built with modern Android development practices. This app provides a complete hotel experience with room booking, premium services, attractions, and user management features.

## 🏨 Features

### ✨ Core Functionality
- **User Authentication**: Secure login/register system with email validation
- **Room Booking**: Browse and book 8 different room types
- **Hotel Services**: Access to premium services like spa treatments, yacht tours, and fine dining
- **Attractions & Offers**: Discover nearby attractions and exclusive hotel offers
- **Booking Management**: Complete booking history and status tracking
- **User Profiles**: Personalized experience with preferences and loyalty points
## 🏗️ Architecture

### Technology Stack
- **Language**: Kotlin
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 14)
- **Architecture Pattern**: MVVM (Model-View-ViewModel)
- **Database**: Room Database (SQLite wrapper)
- **Image Loading**: Glide library
- **UI Framework**: Material Design with ViewBinding

### Project Structure
```
app/src/main/java/com/example/luxevista_hotel/
├── data/                    # Data layer
│   ├── AppDatabase.kt      # Room database configuration
│   ├── LuxeVistaRepository.kt # Repository pattern implementation
│   ├── User.kt             # User entity
│   ├── Room.kt             # Room entity
│   ├── Service.kt          # Service entity
│   ├── Booking.kt          # Booking entity
│   ├── Attraction.kt       # Attraction entity
│   └── *Dao.kt             # Data Access Objects
├── ui/                      # Presentation layer
│   ├── auth/               # Authentication screens
│   ├── home/               # Main dashboard
│   ├── booking/            # Booking management
│   ├── detail/             # Room/Service details
│   ├── profile/            # User profile
│   ├── service/            # Hotel services
│   └── attraction/         # Attractions and offers
├── util/                   # Utility classes
└── LuxeVistaApplication.kt # Application class
```
