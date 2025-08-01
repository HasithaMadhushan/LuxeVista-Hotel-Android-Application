# LuxeVista Hotel - Android Application

A comprehensive luxury hotel booking and management Android application built with modern Android development practices. This app provides a complete hotel experience with room booking, premium services, attractions, and user management features.

## 🏨 Features

### ✨ Core Functionality
- **User Authentication**: Secure login/register system with email validation
- **Room Booking**: Browse and book 8 different room types with real-time availability
- **Hotel Services**: Access to premium services like spa treatments, yacht tours, and fine dining
- **Attractions & Offers**: Discover nearby attractions and exclusive hotel offers
- **Booking Management**: Complete booking history and status tracking
- **User Profiles**: Personalized experience with preferences and loyalty points

### 🛏️ Room Types
- **Standard Room** - $150/night
- **Garden View Room** - $180/night  
- **Deluxe Room** - $250/night
- **Ocean View Suite** - $450/night
- **Family Suite** - $550/night
- **Honeymoon Suite** - $650/night
- **Penthouse** - $950/night
- **Presidential Suite** - $1,200/night

### 🎯 Premium Services
- Deep Tissue Massage - $120
- Fine Dining Reservation - $50
- Poolside Cabana - $100
- Sunset Yacht Tour - $300
- Private Beach Dinner - $250
- Spa Day Package - $350
- Golf Course Access - $150
- Tennis Court Rental - $80

### 🎪 Attractions & Offers
- Sunset Beach Tour
- Coral Reef Diving
- Local Cuisine Tour
- Historical City Tour
- 20% Off Spa Treatments
- Free Welcome Drink

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

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24 or higher
- Kotlin 1.8+ 
- Java 11

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/LuxeVista_Hotel.git
   cd LuxeVista_Hotel
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory and select it

3. **Sync Project**
   - Wait for Gradle sync to complete
   - Ensure all dependencies are downloaded

4. **Run the Application**
   - Connect an Android device or start an emulator
   - Click the "Run" button (green play icon) in Android Studio
   - Select your target device and click "OK"

### Build Configuration

The app uses Gradle with Kotlin DSL. Key configuration in `app/build.gradle.kts`:

```kotlin
android {
    namespace = "com.example.luxevista_hotel"
    compileSdk = 36
    
    defaultConfig {
        applicationId = "com.example.luxevista_hotel"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
}
```

## 📱 Usage

### First Launch
1. The app starts with the **Login Screen**
2. Create a new account or use existing credentials
3. Upon successful login, you'll be taken to the **Main Dashboard**

### Main Dashboard
- **Rooms Section**: Browse available rooms with filtering options
- **Services Section**: Explore premium hotel services
- **Attractions Section**: Discover offers and nearby attractions
- **Navigation**: Access profile, bookings, and logout options

### Room Booking
1. Browse rooms in the horizontal scrolling list
2. Use the **Filter** button to narrow down options by:
   - Room type
   - Price range
   - Sort by price (ascending/descending)
3. Tap on a room to view details
4. Select dates and complete booking

### Service Booking
1. Browse services in the services section
2. Tap on a service to view details
3. Select date and special requests
4. Confirm booking

### User Profile
- Access via the profile icon in the top-left
- View and edit personal information
- Manage preferences and special requests
- Track loyalty points

## 🎨 Design System

### Color Palette
- **Primary**: Deep Blue (#1B3A57) - Professional luxury
- **Secondary**: Gold (#D4AF37) - Premium accent
- **Background**: Light Gray (#F9F9F9) - Clean, modern
- **Accent**: Warm Beige (#E8C5B0) - Hospitality feel
- **Status Colors**: Success, Error, Warning, Info

### UI Components
- Material Design components
- Card-based layouts
- Horizontal scrolling lists
- Responsive design with proper padding
- Professional luxury hotel aesthetic

## 🗄️ Database

### Room Database Configuration
- **Database Name**: `luxevista_database`
- **Version**: 1
- **Entities**: User, Room, Service, Booking, Attraction
- **Auto-population**: Sample data loaded on first launch

### Key Features
- **LiveData**: Reactive data updates throughout the app
- **Relationships**: Proper foreign key relationships
- **CRUD Operations**: Complete Create, Read, Update, Delete functionality
- **Filtering**: Advanced query capabilities for rooms and services

## 🔧 Development

### Key Dependencies
```kotlin
// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")

// Image Loading
implementation("com.github.bumptech.glide:glide:4.16.0")

// UI Components
implementation("androidx.activity:activity-ktx:1.9.0")
implementation("com.google.android.material:material:1.11.0")
```

### Architecture Patterns
- **MVVM**: Model-View-ViewModel for UI logic separation
- **Repository Pattern**: Single source of truth for data
- **LiveData**: Reactive programming for UI updates
- **ViewBinding**: Type-safe view access

## 🧪 Testing

### Test Structure
```
src/
├── androidTest/    # Instrumented tests
└── test/          # Unit tests
```

### Running Tests
- **Unit Tests**: `./gradlew test`
- **Instrumented Tests**: `./gradlew connectedAndroidTest`

## 📋 TODO & Future Enhancements

### Planned Features
- [ ] Network integration with hotel API
- [ ] Payment gateway integration
- [ ] Push notifications for booking updates
- [ ] Offline mode with data synchronization
- [ ] Multi-language support
- [ ] Dark theme support
- [ ] Advanced search and filtering
- [ ] Guest reviews and ratings
- [ ] Loyalty program integration

### Technical Improvements
- [ ] Enhanced image caching and optimization
- [ ] Performance optimization for large datasets
- [ ] Security enhancements (encryption, biometric auth)
- [ ] Accessibility improvements
- [ ] Comprehensive error handling
- [ ] Analytics and crash reporting

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Developer**: [Your Name]
- **Design**: Material Design Guidelines
- **Architecture**: MVVM with Repository Pattern

## 📞 Support

For support and questions:
- Create an issue in the GitHub repository
- Contact: [your-email@example.com]

---

**LuxeVista Hotel** - Where luxury meets convenience in the palm of your hand. 🏨✨ 
