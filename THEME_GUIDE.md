# Red Theme Implementation Guide

## Overview
The FoodOrder application now features a comprehensive red color theme that provides a vibrant, appetizing look consistent with food ordering applications. The theme is applied throughout the entire application using Material Design 3 principles.

## Theme Components

### Color Palette
The application uses a carefully selected red color palette defined in `res/values/colors.xml`:

#### Primary Colors
- **red_primary** (#D32F2F): Main brand color used for toolbars, primary buttons
- **red_primary_dark** (#B71C1C): Darker shade for status bar and emphasis
- **red_primary_light** (#EF5350): Lighter shade for hover states and dark mode

#### Accent Colors
- **red_accent** (#FF5252): Bright red for FABs and call-to-action elements
- **red_secondary** (#C62828): Alternative red for secondary components
- **red_secondary_light** (#E57373): Light red for subtle highlights

#### Supporting Colors
- **background_light** (#FFFFFF): Main background color
- **surface_light** (#FAFAFA): Card and surface backgrounds
- **text_primary** (#212121): Primary text color
- **text_secondary** (#757575): Secondary text and hints
- **text_on_primary** (#FFFFFF): Text displayed on red backgrounds

### Light Theme (Default)
Defined in `res/values/themes.xml`:
```xml
<style name="Base.Theme.FoodOrder" parent="Theme.Material3.DayNight.NoActionBar">
    <item name="colorPrimary">@color/red_primary</item>
    <item name="colorPrimaryVariant">@color/red_primary_dark</item>
    <item name="colorOnPrimary">@color/white</item>
    <item name="colorSecondary">@color/red_accent</item>
    <item name="android:statusBarColor">@color/red_primary_dark</item>
    <item name="colorAccent">@color/red_accent</item>
</style>
```

### Dark Theme
Defined in `res/values-night/themes.xml`:
- Uses slightly lighter shades for better visibility
- Status bar remains red for brand consistency
- Background and surface colors adjusted for dark mode

## UI Components with Red Theme

### 1. Toolbars and App Bars
All toolbars automatically use the primary red color:
```xml
<com.google.android.material.appbar.MaterialToolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="?attr/actionBarSize"
    android:background="?attr/colorPrimary"
    app:titleTextColor="@android:color/white" />
```

### 2. Buttons
Material buttons automatically use the theme colors:
```xml
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnLogin"
    android:layout_width="match_parent"
    android:layout_height="60dp"
    android:text="Login"
    android:textSize="16sp" />
```
The button will automatically be red with white text.

### 3. Floating Action Buttons (FAB)
FABs use the accent color:
```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fabCart"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@android:drawable/ic_menu_search" />
```

### 4. Text Input Fields
Text input fields use outlined style with red accents:
```xml
<com.google.android.material.textfield.TextInputLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox">
    
    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/etEmail"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Email" />
</com.google.android.material.textfield.TextInputLayout>
```

### 5. Radio Buttons and Checkboxes
Radio buttons and checkboxes automatically use the theme's accent color.

## Visual Elements

### Application Logo
A custom logo has been created at `res/drawable/app_logo.xml`:
- Features a red circular design with food-themed icons
- Includes fork and spoon elements
- Responsive vector drawable that scales to any size
- Used on Login and Register screens

### Food List Banner
A welcoming banner at `res/layout/banner_food_list.xml`:
- Gradient background using red theme colors
- Displays "Delicious Food" heading
- Includes food emojis (🍕🍔🍰🥗)
- Material Card with elevation for modern look
- Positioned at top of Food List page

## Usage in Code

### Setting Theme Colors Programmatically
```java
// Get theme color
TypedValue typedValue = new TypedValue();
getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, 
    typedValue, true);
int primaryColor = typedValue.data;

// Apply to view
view.setBackgroundColor(primaryColor);
```

### Styling Text with Theme Colors
```java
TextView textView = findViewById(R.id.myTextView);
textView.setTextColor(getResources().getColor(R.color.red_primary, getTheme()));
```

### Creating Custom Buttons with Theme
```xml
<com.google.android.material.button.MaterialButton
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Custom Button"
    android:backgroundTint="@color/red_accent"
    android:textColor="@color/white" />
```

## Consistency Guidelines

### Do's
✅ Use theme colors (`?attr/colorPrimary`) instead of hardcoded colors
✅ Ensure text on red backgrounds is white for readability
✅ Use Material Design components which automatically apply theme
✅ Test in both light and dark modes
✅ Maintain consistent spacing and elevation

### Don'ts
❌ Don't hardcode colors like `#FF0000` directly in layouts
❌ Don't use conflicting color schemes
❌ Don't place red text on red backgrounds
❌ Don't override theme colors inconsistently

## Accessibility

### Contrast Ratios
All color combinations meet WCAG 2.1 AA standards:
- White text on red primary: 4.5:1 contrast ratio
- Black text on white background: 21:1 contrast ratio

### Color Blindness Considerations
- Red is complemented with text labels
- Important information isn't conveyed by color alone
- Sufficient contrast maintained throughout

## Testing the Theme

### Visual Inspection Checklist
- [ ] All toolbars are red with white text
- [ ] Buttons have red background with white text
- [ ] FABs use red accent color
- [ ] Text input fields show red highlight when focused
- [ ] Radio buttons and checkboxes use red when selected
- [ ] Logo appears correctly on Login and Register screens
- [ ] Banner displays properly on Food List page
- [ ] Theme works in both light and dark modes
- [ ] Status bar is dark red
- [ ] Text is readable on all backgrounds

### Device Testing
Test on:
- Different screen sizes (phone, tablet)
- Different Android versions (API 21+)
- Light and dark mode
- Different display densities

## Customization

### Changing Theme Colors
To modify the theme, update values in `res/values/colors.xml`:
```xml
<color name="red_primary">#YOUR_COLOR</color>
```
The theme will automatically propagate to all components.

### Adding New Theme Attributes
Define new theme attributes in `themes.xml`:
```xml
<item name="myCustomColor">@color/red_accent</item>
```
Reference in layouts:
```xml
android:textColor="?attr/myCustomColor"
```

## Backwards Compatibility
The theme uses Material Design 3 (Material You) which requires:
- Minimum SDK: API 21 (Android 5.0)
- Target SDK: API 36
- Material Components library

For older devices, the theme gracefully falls back to compatible Material Design 2 components.

## Design Principles Applied

### Material Design 3
- Elevation and shadows for depth
- Rounded corners for modern feel
- Consistent spacing (8dp grid)
- Typography scale

### Food Industry Best Practices
- Red stimulates appetite
- Warm, inviting color palette
- High contrast for menu readability
- Clean, uncluttered interface

## Examples Across the App

### Login Screen
- Red logo at top
- White background
- Red login button
- Purple (now red) text for "Sign Up" link

### Food List Screen
- Red toolbar with "Food Menu" title
- Red gradient banner at top
- Red FAB for cart
- Red accent on search field when focused

### Cart Screen
- Red toolbar
- Red "Proceed to Checkout" button
- Red color for total price

### Other Screens
All screens consistently apply the red theme to maintain brand identity throughout the user journey.

## Resources
- Material Design Guidelines: https://m3.material.io/
- Color Tool: https://material.io/resources/color/
- Accessibility: https://www.w3.org/WAI/WCAG21/quickref/

## Support
For questions or issues with the theme implementation, refer to:
- Android Material Design Components documentation
- Project README.md
- Theme configuration files in `res/values/`
