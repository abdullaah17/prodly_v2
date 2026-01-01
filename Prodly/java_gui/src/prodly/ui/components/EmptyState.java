package prodly.ui.components;

import prodly.ui.ModernTheme;
import javax.swing.*;
import java.awt.*;

/**
 * Empty State Component
 * Displays a message when there's no data to show
 */
public class EmptyState extends JPanel {
    private JLabel iconLabel;
    private JLabel titleLabel;
    private JLabel messageLabel;
    private JButton actionButton;
    
    public EmptyState(String title, String message) {
        this(title, message, null, null);
    }
    
    public EmptyState(String title, String message, String actionText, Runnable action) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Icon (using emoji or text)
        iconLabel = new JLabel("📭");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_XLARGE, 0, 
            ModernTheme.PADDING_MEDIUM, 0));
        add(iconLabel);
        
        // Title
        titleLabel = new JLabel(title);
        titleLabel.setFont(ModernTheme.FONT_HEADING);
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, ModernTheme.PADDING_SMALL, 0));
        add(titleLabel);
        
        // Message
        messageLabel = new JLabel("<html><div style='text-align: center; width: 300px;'>" + 
                                 message + "</div></html>");
        messageLabel.setFont(ModernTheme.FONT_BODY);
        messageLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, ModernTheme.PADDING_MEDIUM, 0));
        add(messageLabel);
        
        // Action button (optional)
        if (actionText != null && action != null) {
            actionButton = ModernButton.primary(actionText);
            actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            actionButton.addActionListener(e -> action.run());
            add(actionButton);
        }
        
        add(Box.createVerticalGlue());
    }
    
    public void setIcon(String icon) {
        iconLabel.setText(icon);
    }
    
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    
    public void setMessage(String message) {
        messageLabel.setText("<html><div style='text-align: center; width: 300px;'>" + 
                            message + "</div></html>");
    }
    
    public void setAction(String text, Runnable action) {
        if (actionButton != null) {
            remove(actionButton);
        }
        if (text != null && action != null) {
            actionButton = ModernButton.primary(text);
            actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            actionButton.addActionListener(e -> action.run());
            add(actionButton);
        }
        revalidate();
        repaint();
    }
}

