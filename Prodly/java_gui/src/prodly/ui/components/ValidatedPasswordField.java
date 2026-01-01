package prodly.ui.components;

import prodly.ui.ModernTheme;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Validated Password Field Component
 * Password field with validation, error states, and show/hide password toggle
 */
public class ValidatedPasswordField extends JPanel {
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JLabel label;
    private JButton toggleButton;
    private String validationMessage = "";
    private ValidatedTextField.Validator validator;
    private Border defaultBorder;
    private Border errorBorder;
    private Border focusBorder;
    private boolean passwordVisible = false;
    
    public ValidatedPasswordField(String labelText) {
        this(labelText, null);
    }
    
    public ValidatedPasswordField(String labelText, ValidatedTextField.Validator validator) {
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
        
        // Password field container
        JPanel fieldContainer = new JPanel(new BorderLayout(0, 0));
        fieldContainer.setOpaque(false);
        
        passwordField = new JPasswordField();
        ModernTheme.stylePasswordField(passwordField);
        defaultBorder = passwordField.getBorder();
        errorBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.ERROR, 2),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        );
        focusBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.PRIMARY, 2),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        );
        
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (validationMessage.isEmpty()) {
                    passwordField.setBorder(focusBorder);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                validateField();
            }
        });
        
        // Toggle button
        toggleButton = new JButton("Show");
        toggleButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        toggleButton.setForeground(ModernTheme.TEXT_SECONDARY);
        toggleButton.setBackground(ModernTheme.SURFACE);
        toggleButton.setBorderPainted(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleButton.setPreferredSize(new Dimension(50, 0));
        toggleButton.addActionListener(e -> togglePasswordVisibility());
        
        fieldContainer.add(passwordField, BorderLayout.CENTER);
        fieldContainer.add(toggleButton, BorderLayout.EAST);
        
        add(fieldContainer);
        
        // Error label
        errorLabel = new JLabel();
        errorLabel.setFont(ModernTheme.FONT_SMALL);
        errorLabel.setForeground(ModernTheme.ERROR);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_SMALL, 0, 0, 0));
        errorLabel.setVisible(false);
        add(errorLabel);
    }
    
    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        passwordField.setEchoChar(passwordVisible ? (char) 0 : '*');
        toggleButton.setText(passwordVisible ? "Hide" : "Show");
    }
    
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    public void setPassword(String password) {
        passwordField.setText(password);
    }
    
    public void setValidator(ValidatedTextField.Validator validator) {
        this.validator = validator;
    }
    
    public boolean validateField() {
        if (validator == null) {
            clearError();
            return true;
        }
        
        ValidatedTextField.ValidationResult result = validator.validate(getPassword());
        
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
        passwordField.setBorder(errorBorder);
        revalidate();
        repaint();
    }
    
    private void clearError() {
        validationMessage = "";
        errorLabel.setVisible(false);
        passwordField.setBorder(defaultBorder);
        revalidate();
        repaint();
    }
    
    public boolean hasError() {
        return !validationMessage.isEmpty();
    }
    
    public void setEnabled(boolean enabled) {
        passwordField.setEnabled(enabled);
        toggleButton.setEnabled(enabled);
        if (label != null) {
            label.setEnabled(enabled);
        }
    }
    
    public JPasswordField getPasswordField() {
        return passwordField;
    }
}

