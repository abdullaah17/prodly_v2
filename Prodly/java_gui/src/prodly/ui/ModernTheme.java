package prodly.ui;

import javax.swing.*;
import java.awt.*;

public class ModernTheme {
    // Modern, professional color palette (no neon)
    public static final Color PRIMARY = new Color(52, 73, 94);        // Dark blue-gray
    public static final Color PRIMARY_LIGHT = new Color(70, 90, 110);
    public static final Color SECONDARY = new Color(149, 165, 166);   // Soft gray
    public static final Color ACCENT = new Color(46, 125, 50);         // Professional green
    public static final Color ACCENT_BLUE = new Color(33, 150, 243);   // Modern blue
    public static final Color ACCENT_ORANGE = new Color(255, 152, 0); // Warm orange
    public static final Color ACCENT_PURPLE = new Color(156, 39, 176); // Deep purple
    
    public static final Color BACKGROUND = new Color(250, 250, 250);  // Light gray
    public static final Color SURFACE = Color.WHITE;
    public static final Color BORDER = new Color(224, 224, 224);
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);
    public static final Color SUCCESS = new Color(76, 175, 80);
    public static final Color WARNING = new Color(255, 152, 0);
    public static final Color ERROR = new Color(244, 67, 54);
    public static final Color INFO = new Color(33, 150, 243);
    
    // Modern fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    
    // Spacing constants
    public static final int PADDING_SMALL = 8;
    public static final int PADDING_MEDIUM = 16;
    public static final int PADDING_LARGE = 24;
    public static final int PADDING_XLARGE = 32;
    
    // Border radius (for rounded corners - using borders)
    public static final int BORDER_RADIUS = 8;
    
    // Modern button style
    public static void styleModernButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 42));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    
    // Modern text field style
    public static void styleTextField(JTextField field) {
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        field.setBackground(SURFACE);
    }
    
    // Modern password field style
    public static void stylePasswordField(JPasswordField field) {
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        field.setBackground(SURFACE);
    }
    
    // Modern panel with shadow effect
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM)
        ));
        return panel;
    }
    
    // Modern label style
    public static void styleLabel(JLabel label, boolean isHeading) {
        if (isHeading) {
            label.setFont(FONT_HEADING);
            label.setForeground(TEXT_PRIMARY);
        } else {
            label.setFont(FONT_BODY);
            label.setForeground(TEXT_SECONDARY);
        }
    }
}

