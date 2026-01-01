# Modern UI Components Guide

## Overview

This document describes the modern, reusable UI component library built for the Prodly application. All components follow modern design principles with smooth animations, proper validation, and accessibility features.

## Component Structure

```
prodly/ui/components/
├── ToastNotification.java      - Toast notifications for success/error/warning/info
├── LoadingOverlay.java         - Loading spinner with overlay
├── ModernModal.java            - Modal dialogs with animations
├── ModernCard.java             - Card components with hover effects
├── ModernButton.java           - Enhanced buttons with animations
├── ValidatedTextField.java     - Text fields with validation
├── ValidatedPasswordField.java - Password fields with show/hide toggle
├── ModernTable.java            - Enhanced tables with search/filter
└── EmptyState.java             - Empty state messages
```

## Component Usage

### 1. Toast Notifications

Display temporary notifications for user actions:

```java
// Success notification
ToastNotification.showSuccess(parentComponent, "Operation completed successfully!");

// Error notification
ToastNotification.showError(parentComponent, "Something went wrong");

// Warning notification
ToastNotification.showWarning(parentComponent, "Please review your input");

// Info notification
ToastNotification.showInfo(parentComponent, "New features available");
```

**Features:**
- Auto-dismiss after 3 seconds
- Smooth slide-in/out animations
- Color-coded by type (success, error, warning, info)
- Non-blocking UI

### 2. Loading Overlay

Show loading state during async operations:

```java
LoadingOverlay loadingOverlay = new LoadingOverlay();

// Show loading
loadingOverlay.show("Loading data...");

// Hide loading
loadingOverlay.hide();
```

**Features:**
- Semi-transparent backdrop
- Animated spinner
- Customizable message
- Non-blocking (use with background threads)

### 3. Modern Modal

Create modal dialogs:

```java
ModernModal modal = new ModernModal(parentFrame, "Dialog Title");

// Add content
JPanel content = new JPanel();
// ... add components to content
modal.setContent(content);

// Add buttons
ModernButton saveButton = ModernButton.primary("Save");
saveButton.addActionListener(e -> {
    // Handle save
    modal.close();
});
modal.addButton(saveButton);

// Show modal
modal.showModal();
```

**Features:**
- Smooth fade-in/out animations
- Backdrop click to close (optional)
- Close button in header
- Centered on parent window

### 4. Modern Card

Reusable card component:

```java
ModernCard card = new ModernCard(true, true); // hoverable, clickable

card.add(new JLabel("Card Content"));

// Optional: Add click action
card.setOnClick(() -> {
    // Handle click
});

// Add to parent
parentPanel.add(card);
```

**Features:**
- Hover effects
- Click interactions
- Smooth color transitions
- Border highlight on hover

### 5. Modern Button

Enhanced buttons with animations:

```java
// Primary button
ModernButton primaryBtn = ModernButton.primary("Click Me");

// Success button
ModernButton successBtn = ModernButton.success("Save");

// Danger button
ModernButton dangerBtn = ModernButton.danger("Delete");

// Custom color
ModernButton customBtn = new ModernButton("Custom", customColor);
```

**Features:**
- Smooth hover animations
- Press state feedback
- Color-coded variants
- Consistent sizing

### 6. Validated Text Field

Text fields with validation:

```java
// Required field
ValidatedTextField username = new ValidatedTextField(
    "Username",
    ValidatedTextField.combine(
        ValidatedTextField.required(),
        ValidatedTextField.minLength(3)
    )
);

// Email validation
ValidatedTextField email = new ValidatedTextField(
    "Email",
    ValidatedTextField.email()
);

// Validate on focus loss
boolean isValid = username.validateField();
```

**Features:**
- Inline error messages
- Visual error states (red border)
- Focus states (blue border)
- Multiple validators can be combined
- Real-time validation

### 7. Validated Password Field

Password fields with show/hide toggle:

```java
ValidatedPasswordField password = new ValidatedPasswordField(
    "Password",
    ValidatedTextField.combine(
        ValidatedTextField.required(),
        ValidatedTextField.minLength(6)
    )
);

String passwordValue = password.getPassword();
```

**Features:**
- Show/hide password toggle
- Same validation as text fields
- Eye icon for visibility toggle

### 8. Modern Table

Enhanced tables with search:

```java
String[] columns = {"Name", "Email", "Role"};
ModernTable table = new ModernTable(columns, true); // true = searchable

// Add rows
table.addRow(new Object[]{"John Doe", "john@example.com", "Employee"});

// Get selected row
int selectedRow = table.getSelectedRow();

// Clear table
table.clear();
```

**Features:**
- Built-in search/filter
- Row count display
- Modern styling
- Hover effects
- Sortable columns

### 9. Empty State

Display when no data is available:

```java
EmptyState emptyState = new EmptyState(
    "No Tasks Found",
    "You don't have any tasks assigned yet.",
    "Create Task",
    () -> {
        // Handle create action
    }
);

// Add to panel
panel.add(emptyState);
```

**Features:**
- Customizable icon
- Title and message
- Optional action button
- Centered layout

## Best Practices

### 1. Threading
Always perform long-running operations in background threads:

```java
loadingOverlay.show("Processing...");
new Thread(() -> {
    // Long operation
    String result = performOperation();
    
    SwingUtilities.invokeLater(() -> {
        loadingOverlay.hide();
        ToastNotification.showSuccess(this, "Done!");
    });
}).start();
```

### 2. Validation
Validate forms before submission:

```java
boolean isValid = true;
isValid &= usernameField.validateField();
isValid &= passwordField.validateField();

if (!isValid) {
    ToastNotification.showError(this, "Please fix errors");
    return;
}
```

### 3. Error Handling
Always handle errors gracefully:

```java
try {
    // Operation
} catch (Exception e) {
    loadingOverlay.hide();
    ToastNotification.showError(this, "Error: " + e.getMessage());
    e.printStackTrace();
}
```

### 4. Accessibility
- Use semantic labels
- Provide keyboard navigation
- Ensure proper contrast ratios
- Add tooltips where needed

## Customization

### Colors
Modify `ModernTheme.java` to change color scheme:

```java
public static final Color PRIMARY = new Color(52, 73, 94);
public static final Color ACCENT = new Color(46, 125, 50);
// ... etc
```

### Animations
Adjust animation durations in component files:

```java
private static final int ANIMATION_DURATION = 300; // milliseconds
```

### Fonts
Modify font constants in `ModernTheme.java`:

```java
public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
```

## Extending Components

### Creating Custom Validators

```java
ValidatedTextField.Validator customValidator = value -> {
    if (value.contains("forbidden")) {
        return ValidatedTextField.ValidationResult.invalid("Contains forbidden text");
    }
    return ValidatedTextField.ValidationResult.valid();
};
```

### Creating Custom Button Styles

```java
ModernButton customBtn = new ModernButton("Custom", new Color(100, 150, 200));
```

## Integration Examples

### Login Form with Validation

```java
ValidatedTextField username = new ValidatedTextField("Username", 
    ValidatedTextField.required());
ValidatedPasswordField password = new ValidatedPasswordField("Password",
    ValidatedTextField.combine(
        ValidatedTextField.required(),
        ValidatedTextField.minLength(6)
    ));

ModernButton loginBtn = ModernButton.primary("Login");
loginBtn.addActionListener(e -> {
    if (username.validateField() && password.validateField()) {
        performLogin();
    }
});
```

### Data Table with Search

```java
ModernTable table = new ModernTable(new String[]{"ID", "Name", "Status"}, true);

// Populate data
for (Item item : items) {
    table.addRow(new Object[]{
        item.getId(),
        item.getName(),
        item.getStatus()
    });
}
```

## Notes

- All components are designed to work together seamlessly
- Components follow Material Design principles
- Animations are smooth and non-intrusive
- All components are accessible and keyboard-navigable
- Components are production-ready and tested

