package prodly.ui.components;

import prodly.ui.ModernTheme;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Validated Text Field Component
 * Text field with validation, error states, and inline error messages
 */
public class ValidatedTextField extends JPanel {
    private JTextField textField;
    private JLabel errorLabel;
    private JLabel label;
    private String validationMessage = "";
    private Validator validator;
    private Border defaultBorder;
    private Border errorBorder;
    private Border focusBorder;
    
    @FunctionalInterface
    public interface Validator {
        ValidationResult validate(String value);
    }
    
    public static class ValidationResult {
        public final boolean isValid;
        public final String message;
        
        public ValidationResult(boolean isValid, String message) {
            this.isValid = isValid;
            this.message = message;
        }
        
        public static ValidationResult valid() {
            return new ValidationResult(true, "");
        }
        
        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }
    }
    
    public ValidatedTextField(String labelText) {
        this(labelText, null);
    }
    
    public ValidatedTextField(String labelText, Validator validator) {
        this.validator = validator;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        
        // Label
        if (labelText != null && !labelText.isEmpty()) {
            label = new JLabel(labelText);
            ModernTheme.styleLabel(label, false);
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, ModernTheme.PADDING_SMALL, 0));
            add(label);
        }
        
        // Text field
        textField = new JTextField();
        ModernTheme.styleTextField(textField);
        defaultBorder = textField.getBorder();
        errorBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.ERROR, 2),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        );
        focusBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.PRIMARY, 2),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        );
        
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (validationMessage.isEmpty()) {
                    textField.setBorder(focusBorder);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                validateField();
            }
        });
        
        add(textField);
        
        // Error label
        errorLabel = new JLabel();
        errorLabel.setFont(ModernTheme.FONT_SMALL);
        errorLabel.setForeground(ModernTheme.ERROR);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_SMALL, 0, 0, 0));
        errorLabel.setVisible(false);
        add(errorLabel);
    }
    
    public String getText() {
        return textField.getText();
    }
    
    public void setText(String text) {
        textField.setText(text);
    }
    
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    
    public boolean validateField() {
        if (validator == null) {
            clearError();
            return true;
        }
        
        ValidationResult result = validator.validate(textField.getText());
        
        if (result.isValid) {
            clearError();
            return true;
        } else {
            showError(result.message);
            return false;
        }
    }
    
    private void showError(String message) {
        validationMessage = message;
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        textField.setBorder(errorBorder);
        revalidate();
        repaint();
    }
    
    private void clearError() {
        validationMessage = "";
        errorLabel.setVisible(false);
        textField.setBorder(defaultBorder);
        revalidate();
        repaint();
    }
    
    public boolean hasError() {
        return !validationMessage.isEmpty();
    }
    
    public void setEnabled(boolean enabled) {
        textField.setEnabled(enabled);
        if (label != null) {
            label.setEnabled(enabled);
        }
    }
    
    // Common validators
    public static Validator required() {
        return value -> {
            if (value == null || value.trim().isEmpty()) {
                return ValidationResult.invalid("This field is required");
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator minLength(int min) {
        return value -> {
            if (value == null || value.length() < min) {
                return ValidationResult.invalid("Minimum length is " + min + " characters");
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator maxLength(int max) {
        return value -> {
            if (value != null && value.length() > max) {
                return ValidationResult.invalid("Maximum length is " + max + " characters");
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator email() {
        return value -> {
            if (value == null || value.trim().isEmpty()) {
                return ValidationResult.valid(); // Optional field
            }
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!value.matches(emailRegex)) {
                return ValidationResult.invalid("Please enter a valid email address");
            }
            return ValidationResult.valid();
        };
    }
    
    public static Validator combine(Validator... validators) {
        return value -> {
            for (Validator validator : validators) {
                ValidationResult result = validator.validate(value);
                if (!result.isValid) {
                    return result;
                }
            }
            return ValidationResult.valid();
        };
    }
}

