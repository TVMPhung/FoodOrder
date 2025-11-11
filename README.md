# FoodOrder - Android Food Ordering Application

A complete Android application for food ordering built with Java, Room Database, and Material Design components.

## Features Implemented

### 1. **Authentication Module**
- **Sign Up Screen**: User registration with validation
  - Username, email, password, phone number, address fields
  - Input validation for all fields
  - Password strength checking
  - Duplicate account prevention
- **Login Screen**: Secure authentication
  - Email and password login
  - Remember me functionality
  - Session management with SharedPreferences
  - Auto-login for returning users

### 2. **Database Architecture**
- **Room Database** with 6 entities:
  - `User`: User account information
  - `Food`: Food items with details
  - `CartItem`: Shopping cart items
  - `Order`: Order records
  - `Review`: User reviews and ratings
  - `Message`: Chat messages
- **DAOs (Data Access Objects)**: One for each entity
- **Repository Pattern**: 6 repositories for data management
- **LiveData**: Reactive data observation throughout the app

### 3. **Food Browsing**
- **Food List Screen**:
  - RecyclerView with food items
  - Search functionality
  - Category filtering (with ChipGroup)
  - Pull-to-refresh capability
  - Sample data population
- **Food Detail Screen**:
  - Complete food information display
  - Image, name, description, price, ingredients
  - Average rating and review count
  - Quantity selector
  - Add to cart functionality
  - Availability status
  - Navigate to reviews

### 4. **Shopping Cart**
- **Cart Screen**:
  - Display all cart items with images
  - Quantity adjustment (increase/decrease)
  - Remove items functionality
  - Subtotal calculation per item
  - Total price with delivery fee
  - Proceed to checkout button
- **Cart Notification**:
  - Notification when app opens if cart has items
  - Clickable notification to navigate to cart
  - Notification channel for Android O+

### 5. **Payment Processing**
- **Billing Screen**:
  - Order summary display
  - Editable delivery address
  - Payment method selection:
    - Cash on Delivery
    - Online Payment
  - Cost breakdown:
    - Subtotal
    - Delivery fee ($2.99)
    - Tax (8%)
    - Total amount
  - Place order functionality
  - Order confirmation dialog

### 6. **Order Management**
- **Order History Screen**:
  - List of all past orders
  - Order details: ID, date, items, total, status
  - Status indicators: Pending, Confirmed, Preparing, Out for Delivery, Delivered, Cancelled
  - Chronological order display
  - Empty state handling

### 7. **Review and Rating System**
- **Review Screen**:
  - Submit reviews with 5-star rating
  - Text review input (max 500 characters)
  - Display all reviews for a food item
  - Reviewer name and timestamp
  - Sort by date

### 8. **Communication Feature**
- **Chat Screen**:
  - Real-time chat interface with store
  - Message history stored in Room database
  - Sent/received message differentiation
  - Message timestamps
  - Keyboard-aware scrolling

### 9. **Location Services**
- **Map Screen**:
  - Store information display:
    - Address
    - Phone number
    - Operating hours
  - "Get Directions" button
  - Integration with Google Maps intent

## Technical Implementation

### Architecture
- **Pattern**: MVVM-like with Repository pattern
- **Database**: Room Database with SQLite
- **UI**: Material Design Components
- **Binding**: View Binding enabled
- **Reactive**: LiveData for data observation

### Project Structure
```
app/src/main/java/com/example/foodorder/
├── model/              # Entity classes (6 entities)
├── dao/                # Data Access Objects (6 DAOs)
├── database/           # AppDatabase singleton
├── repository/         # Repository classes (6 repositories)
├── ui/
│   ├── auth/          # Login & SignUp activities
│   ├── food/          # Food list & detail activities
│   ├── cart/          # Cart activity
│   ├── billing/       # Billing/checkout activity
│   ├── order/         # Order history activity
│   ├── review/        # Review activity
│   ├── chat/          # Chat activity
│   └── location/      # Map activity
├── adapter/           # RecyclerView adapters (5 adapters)
└── utils/             # Utility classes (SessionManager, ValidationUtils, NotificationHelper)
```

### Dependencies
- AndroidX AppCompat
- Material Components
- Room Database (Runtime & Compiler)
- Lifecycle (ViewModel & LiveData)
- RecyclerView, CardView, SwipeRefreshLayout, CoordinatorLayout
- Google Play Services (Maps & Location)

## Key Files

### Java Files (38 total)
- **Models**: User, Food, CartItem, Order, Review, Message
- **DAOs**: UserDao, FoodDao, CartItemDao, OrderDao, ReviewDao, MessageDao
- **Repositories**: 6 repository classes
- **Activities**: 11 activities
- **Adapters**: 5 RecyclerView adapters
- **Utilities**: 3 utility classes

### Layout Files (16 total)
- Activity layouts for all screens
- RecyclerView item layouts
- Menu resources

## Permissions
- `INTERNET`: For network operations
- `ACCESS_FINE_LOCATION`: For location services
- `ACCESS_COARSE_LOCATION`: For location services
- `POST_NOTIFICATIONS`: For cart notifications (Android 13+)

## Configuration
- **Minimum SDK**: API 21 (Android 5.0)
- **Target SDK**: API 36
- **Compile SDK**: API 36
- **Java Version**: 11

## Features Highlights

### Security & Validation
- Input validation for all user inputs
- Password strength checking
- Email format validation
- Phone number validation
- Session management

### User Experience
- Material Design throughout
- Smooth animations and transitions
- Pull-to-refresh
- Empty state handling
- Loading states
- Error handling

### Data Management
- Persistent storage with Room
- LiveData for reactive updates
- Repository pattern for clean architecture
- Asynchronous database operations

## Sample Data
The app includes sample food items that are automatically populated on first run:
1. Margherita Pizza - $12.99
2. Cheeseburger - $9.99
3. Caesar Salad - $7.99
4. Spaghetti Carbonara - $11.99
5. Chicken Wings - $8.99

## Future Enhancements
- Google Maps API integration for real map view
- Real-time chat with backend integration
- Push notifications for order updates
- Payment gateway integration
- Image upload for food items
- Order tracking with live status updates
- Promo code functionality
- Multiple payment methods

## Build Instructions
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle
4. Run on emulator or device (API 21+)

## Notes
- The app uses Room Database for local storage
- All data is stored locally on the device
- Google Maps integration requires API key (placeholder included)
- Notification functionality requires Android 13+ for runtime permission

## License
This is a demonstration project for educational purposes.
