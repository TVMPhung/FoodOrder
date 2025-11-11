# Database Import Feature Guide

## Overview
The FoodOrder application now includes a comprehensive database import system for food products with image storage capabilities.

## Features
- Import food items with images stored directly in the database
- Support for multiple image formats (JPEG, PNG, etc.)
- Automatic image compression and optimization
- Batch import functionality for multiple items
- Comprehensive data validation and error handling

## Using DatabaseImportHelper

### Basic Setup
```java
import com.example.foodorder.utils.DatabaseImportHelper;
import com.example.foodorder.repository.FoodRepository;

// In your Activity or Fragment
FoodRepository foodRepository = new FoodRepository(getApplication());
DatabaseImportHelper importHelper = new DatabaseImportHelper(this, foodRepository);
```

### Single Food Item Import (from Drawable Resource)
```java
// Import a single food item with image
boolean success = importHelper.importFoodWithImage(
    "Margherita Pizza",                    // name
    "Classic Italian pizza with fresh mozzarella and basil",  // description
    12.99,                                  // price
    R.drawable.pizza_margherita,            // drawable resource ID
    "Pizza",                                // category
    "Tomato sauce, Mozzarella, Basil"      // ingredients
);

if (success) {
    Log.d("Import", "Food item imported successfully");
}
```

### Single Food Item Import (from InputStream)
```java
try {
    InputStream imageStream = getAssets().open("images/burger.jpg");
    boolean success = importHelper.importFoodWithImageStream(
        "Classic Cheeseburger",
        "Juicy beef patty with cheese and fresh vegetables",
        9.99,
        imageStream,
        "Burgers",
        "Beef patty, Cheese, Lettuce, Tomato, Pickles"
    );
} catch (IOException e) {
    e.printStackTrace();
}
```

### Batch Import Multiple Items
```java
import com.example.foodorder.utils.DatabaseImportHelper.FoodImportItem;

List<FoodImportItem> foodItems = new ArrayList<>();

// Add multiple items
foodItems.add(new FoodImportItem(
    "Margherita Pizza",
    "Classic pizza with tomato sauce and mozzarella",
    12.99,
    R.drawable.pizza_margherita,
    "Pizza",
    "Tomato, Mozzarella, Basil"
));

foodItems.add(new FoodImportItem(
    "Cheeseburger",
    "Juicy beef patty with cheese, lettuce, and tomato",
    9.99,
    R.drawable.burger,
    "Burgers",
    "Beef, Cheese, Lettuce, Tomato"
));

foodItems.add(new FoodImportItem(
    "Caesar Salad",
    "Fresh romaine lettuce with Caesar dressing",
    7.99,
    R.drawable.salad,
    "Salads",
    "Romaine, Parmesan, Croutons"
));

// Execute batch import
int successCount = importHelper.batchImportFoods(foodItems);
Log.d("Import", "Successfully imported " + successCount + " items");
```

### Image Storage Details
- Images are automatically compressed to JPEG format at 80% quality
- Maximum image dimension is 500px (width or height)
- Images are stored as byte arrays in the database
- Original aspect ratio is preserved during resizing

### Data Validation
The import helper validates all required fields:
- Name must not be empty
- Description must not be empty
- Price must be positive
- Category must not be empty
- Ingredients must not be empty
- Image must be loadable

### Error Handling
All import methods return boolean or int values indicating success:
- `importFoodWithImage()`: Returns `true` on success, `false` on failure
- `importFoodWithImageStream()`: Returns `true` on success, `false` on failure
- `batchImportFoods()`: Returns count of successfully imported items

Errors are logged with detailed messages for debugging.

### Best Practices
1. **Image Preparation**: Use optimized images to reduce processing time
2. **Batch Operations**: Use batch import for multiple items to improve performance
3. **Error Checking**: Always check return values to handle failures
4. **Threading**: Import operations should be done on background threads to avoid blocking UI

### Example: Complete Implementation in Activity
```java
public class FoodListActivity extends AppCompatActivity {
    private FoodRepository foodRepository;
    private DatabaseImportHelper importHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ... setup code ...
        
        foodRepository = new FoodRepository(getApplication());
        importHelper = new DatabaseImportHelper(this, foodRepository);
        
        // Check if database is empty and populate if needed
        checkAndPopulateDatabase();
    }
    
    private void checkAndPopulateDatabase() {
        foodRepository.getAllFoods().observe(this, foods -> {
            if (foods == null || foods.isEmpty()) {
                // Run import on background thread
                new Thread(() -> {
                    importSampleData();
                }).start();
            }
        });
    }
    
    private void importSampleData() {
        List<FoodImportItem> items = new ArrayList<>();
        
        items.add(new FoodImportItem(
            "Margherita Pizza",
            "Classic pizza with tomato sauce and mozzarella",
            12.99,
            R.drawable.pizza,  // Make sure this drawable exists
            "Pizza",
            "Tomato, Mozzarella, Basil"
        ));
        
        // Add more items...
        
        int count = importHelper.batchImportFoods(items);
        
        runOnUiThread(() -> {
            Toast.makeText(this, 
                "Imported " + count + " items", 
                Toast.LENGTH_SHORT).show();
        });
    }
}
```

## Database Schema Updates
The Food entity now includes an `imageData` field:
```java
@Entity(tableName = "foods")
public class Food {
    // ... other fields ...
    private byte[] imageData; // New field for image storage
    
    public byte[] getImageData() {
        return imageData;
    }
    
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
```

## Migration Notes
- Database version updated from 1 to 2
- Using `fallbackToDestructiveMigration()` - existing data will be cleared
- For production apps, implement proper Room migrations to preserve data

## Displaying Images
To display images stored in the database:
```java
// In your adapter or activity
byte[] imageData = food.getImageData();
if (imageData != null && imageData.length > 0) {
    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    imageView.setImageBitmap(bitmap);
} else {
    // Use placeholder image
    imageView.setImageResource(R.drawable.placeholder);
}
```

## Performance Considerations
- Image compression reduces database size
- Batch operations are more efficient than individual inserts
- Consider using WorkManager for large import operations
- Monitor database size and implement cleanup if needed

## Troubleshooting
- **Images not importing**: Check drawable resources exist
- **Out of memory**: Reduce MAX_IMAGE_SIZE in DatabaseImportHelper
- **Slow imports**: Use batch operations and background threads
- **Validation failures**: Check all required fields are provided
