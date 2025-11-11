# FoodOrder - Quick Start Guide

## New Features Overview

This guide provides a quick introduction to the newly implemented features in the FoodOrder Android application.

## 🎨 Red Theme

### What's New
The entire application now features a vibrant red color theme that:
- Makes the app more visually appealing
- Creates a consistent brand identity
- Enhances the food ordering experience

### Where to See It
- **Toolbars**: Red background with white text
- **Buttons**: Red background with white text
- **FABs**: Red floating action buttons
- **Status Bar**: Dark red
- **Text Fields**: Red accent when focused
- **Radio/Checkboxes**: Red when selected

### How It Works
The theme is automatically applied via Material Design 3:
- Colors defined in `res/values/colors.xml`
- Theme configured in `res/values/themes.xml`
- All Material components inherit the colors

### Customization
To change colors, edit `colors.xml`:
```xml
<color name="red_primary">#D32F2F</color>  <!-- Change this -->
```

## 🖼️ Application Logo

### What's New
A custom logo with food-themed design:
- Red circular background
- White inner circle
- Fork and spoon elements
- Scalable vector format

### Where to See It
- **Login Page**: Top center (120dp size)
- **Register Page**: Top center (100dp size)

### File Location
- `res/drawable/app_logo.xml`

### Usage in Other Layouts
```xml
<ImageView
    android:layout_width="120dp"
    android:layout_height="120dp"
    android:src="@drawable/app_logo" />
```

## 📢 Food List Banner

### What's New
Eye-catching banner at the top of Food List page:
- Gradient red background
- "Delicious Food" heading
- Subtitle text
- Food emojis (🍕🍔🍰🥗)

### Where to See It
- **Food List Page**: At the very top, above search

### Files
- `res/drawable/food_list_banner.xml` - Background
- `res/layout/banner_food_list.xml` - Complete layout

### Customization
Edit `banner_food_list.xml` to change:
- Text content
- Size (currently 180dp height)
- Emojis or icons
- Colors

## 📦 Database Import System

### What's New
Powerful system to import food items with images:
- Import from drawable resources
- Import from files/streams
- Batch import multiple items
- Automatic image compression
- Data validation

### Quick Usage

#### Import Single Item
```java
// Setup
FoodRepository repository = new FoodRepository(getApplication());
DatabaseImportHelper helper = new DatabaseImportHelper(this, repository);

// Import
boolean success = helper.importFoodWithImage(
    "Pizza Margherita",           // name
    "Classic Italian pizza",      // description
    12.99,                        // price
    R.drawable.pizza,             // image resource
    "Pizza",                      // category
    "Tomato, Cheese, Basil"      // ingredients
);
```

#### Batch Import
```java
List<FoodImportItem> items = new ArrayList<>();

items.add(new FoodImportItem(
    "Pizza", "Classic pizza", 12.99,
    R.drawable.pizza, "Pizza", "Tomato, Cheese"
));

items.add(new FoodImportItem(
    "Burger", "Juicy burger", 9.99,
    R.drawable.burger, "Burgers", "Beef, Cheese"
));

int count = helper.batchImportFoods(items);
Log.d("Import", "Imported " + count + " items");
```

### Key Features
- **Validation**: Checks all required fields
- **Compression**: Optimizes images (500px max, 80% quality)
- **Formats**: Supports JPEG, PNG, and all Bitmap-compatible formats
- **Storage**: Images saved as byte arrays in database
- **Error Handling**: Returns success/failure with logging

### Displaying Imported Images
```java
byte[] imageData = food.getImageData();
if (imageData != null && imageData.length > 0) {
    Bitmap bitmap = BitmapFactory.decodeByteArray(
        imageData, 0, imageData.length
    );
    imageView.setImageBitmap(bitmap);
}
```

## 📚 Full Documentation

For detailed information, see:

1. **DATABASE_IMPORT_GUIDE.md**
   - Complete API documentation
   - Advanced usage examples
   - Best practices
   - Troubleshooting

2. **THEME_GUIDE.md**
   - Complete color palette
   - Component styling guide
   - Accessibility guidelines
   - Customization instructions

3. **IMPLEMENTATION_SUMMARY.md**
   - Complete feature overview
   - Technical details
   - Requirements tracking

## 🚀 Getting Started

### For New Developers

1. **Clone the repository**
   ```bash
   git clone https://github.com/TVMPhung/FoodOrder.git
   cd FoodOrder
   ```

2. **Open in Android Studio**
   - Import as existing Android project
   - Wait for Gradle sync

3. **Build the project**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Run on device/emulator**
   - Requires Android API 21+
   - Recommended API 30+

### Exploring the Code

**Database Import:**
- `app/src/main/java/com/example/foodorder/utils/DatabaseImportHelper.java`

**Theme:**
- `app/src/main/res/values/colors.xml`
- `app/src/main/res/values/themes.xml`
- `app/src/main/res/values-night/themes.xml`

**Logo:**
- `app/src/main/res/drawable/app_logo.xml`

**Banner:**
- `app/src/main/res/drawable/food_list_banner.xml`
- `app/src/main/res/layout/banner_food_list.xml`

**Layouts:**
- `app/src/main/res/layout/activity_login.xml` (logo)
- `app/src/main/res/layout/activity_signup.xml` (logo)
- `app/src/main/res/layout/activity_food_list.xml` (banner)

## 🎯 Common Tasks

### Change Theme Color
Edit `res/values/colors.xml`:
```xml
<color name="red_primary">#YOUR_COLOR</color>
```

### Update Logo
Replace `res/drawable/app_logo.xml` with your design.

### Modify Banner Text
Edit `res/layout/banner_food_list.xml`:
```xml
<TextView
    android:text="Your Custom Text" />
```

### Import Food Items
See usage examples above or `DATABASE_IMPORT_GUIDE.md`.

### Display Food Images
```java
// Get image data from database
byte[] imageData = food.getImageData();

// Convert to bitmap and display
if (imageData != null) {
    Bitmap bitmap = BitmapFactory.decodeByteArray(
        imageData, 0, imageData.length
    );
    imageView.setImageBitmap(bitmap);
}
```

## 🔧 Troubleshooting

### Build Issues
**Problem**: Gradle sync fails
**Solution**: Check `settings.gradle` and repository configurations

**Problem**: Cannot find R.drawable.app_logo
**Solution**: Clean and rebuild project

### Theme Not Applying
**Problem**: Components not red
**Solution**: Ensure using Material Design components (MaterialButton, not Button)

### Import Not Working
**Problem**: Images not importing
**Solution**: 
- Check drawable resources exist
- Verify all required fields provided
- Check logs for validation errors

### Banner Not Showing
**Problem**: Banner missing on Food List
**Solution**: 
- Verify `<include layout="@layout/banner_food_list" />` in activity_food_list.xml
- Check banner layout file exists

## 📱 Testing Checklist

- [ ] App builds successfully
- [ ] Red theme visible on all screens
- [ ] Logo displays on Login page
- [ ] Logo displays on Register page
- [ ] Banner displays on Food List page
- [ ] Can import food items
- [ ] Images display correctly
- [ ] Theme works in dark mode
- [ ] All buttons are red
- [ ] Toolbars are red with white text

## 🤝 Contributing

When adding new features:
1. Follow existing code style
2. Use theme colors from `colors.xml`
3. Use Material Design components
4. Add documentation
5. Test in light and dark modes

## 📞 Support

For questions or issues:
1. Check documentation files
2. Review code comments
3. Check implementation examples
4. Refer to Android documentation

## 🔗 Useful Links

- [Material Design 3](https://m3.material.io/)
- [Android Room Database](https://developer.android.com/training/data-storage/room)
- [Material Components for Android](https://github.com/material-components/material-components-android)
- [Android Best Practices](https://developer.android.com/topic/performance/vitals)

## 📝 License

This project is for educational purposes.

---

**Happy Coding! 🚀**
