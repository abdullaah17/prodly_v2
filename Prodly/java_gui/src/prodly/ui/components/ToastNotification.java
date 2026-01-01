package prodly.ui.components;

import prodly.ui.ModernTheme;
import javax.swing.*;
import java.awt.*;

/**
 * Modern Toast Notification Component
 * Displays temporary success, error, warning, or info messages
 */
public class ToastNotification {
    private static final int TOAST_DURATION = 3000; // 3 seconds
    private static final int ANIMATION_DURATION = 300; // Animation duration in ms
    private static final int TOAST_HEIGHT = 60;
    private static final int TOAST_WIDTH = 350;
    
    public enum ToastType {
        SUCCESS, ERROR, WARNING, INFO
    }
    
    /**
     * Show a toast notification
     * @param parent The parent component (usually a JFrame)
     * @param message The message to display
     * @param type The type of toast (SUCCESS, ERROR, WARNING, INFO)
     */
    public static void show(Component parent, String message, ToastType type) {
        JFrame frame = getFrame(parent);
        if (frame == null) return;
        
        // Create toast panel
        JPanel toastPanel = createToastPanel(message, type);
        
        // Get frame dimensions
        Dimension frameSize = frame.getSize();
        Point frameLocation = frame.getLocation();
        
        // Calculate toast position (top-right corner with padding)
        int x = frameLocation.x + frameSize.width - TOAST_WIDTH - 20;
        int y = frameLocation.y + 80;
        
        // Create overlay window
        JWindow toastWindow = new JWindow(frame);
        toastWindow.setSize(TOAST_WIDTH, TOAST_HEIGHT);
        toastWindow.setLocation(x, y);
        toastWindow.setLayout(new BorderLayout());
        toastWindow.add(toastPanel, BorderLayout.CENTER);
        toastWindow.setBackground(new Color(0, 0, 0, 0));
        
        // Animate in
        animateIn(toastWindow, () -> {
            // After animation, wait for duration, then animate out
            Timer hideTimer = new Timer(TOAST_DURATION, e -> {
                animateOut(toastWindow, () -> {
                    toastWindow.dispose();
                });
            });
            hideTimer.setRepeats(false);
            hideTimer.start();
        });
    }
    
    private static JPanel createToastPanel(String message, ToastType type) {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Set background color based on type
        Color bgColor;
        Color iconColor;
        String icon;
        
        switch (type) {
            case SUCCESS:
                bgColor = ModernTheme.SUCCESS;
                iconColor = Color.WHITE;
                icon = "✓";
                break;
            case ERROR:
                bgColor = ModernTheme.ERROR;
                iconColor = Color.WHITE;
                icon = "✕";
                break;
            case WARNING:
                bgColor = ModernTheme.WARNING;
                iconColor = Color.WHITE;
                icon = "⚠";
                break;
            case INFO:
            default:
                bgColor = ModernTheme.INFO;
                iconColor = Color.WHITE;
                icon = "ℹ";
                break;
        }
        
        panel.setBackground(bgColor);
        panel.setOpaque(true);
        
        // Icon label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        iconLabel.setForeground(iconColor);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(30, 30));
        
        // Message label
        JLabel messageLabel = new JLabel("<html><body style='width: " + (TOAST_WIDTH - 80) + "px'>" + 
                                         escapeHtml(message) + "</body></html>");
        messageLabel.setFont(ModernTheme.FONT_BODY);
        messageLabel.setForeground(Color.WHITE);
        
        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(messageLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private static void animateIn(JWindow window, Runnable onComplete) {
        window.setOpacity(0f);
        window.setVisible(true);
        
        Timer animTimer = new Timer(10, null);
        final long startTime = System.currentTimeMillis();
        
        animTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1.0f, (float) elapsed / ANIMATION_DURATION);
            
            // Ease-out animation
            float eased = 1.0f - (float) Math.pow(1.0 - progress, 3);
            window.setOpacity(eased);
            
            // Slide in from right
            Point location = window.getLocation();
            int targetX = location.x;
            int startX = targetX + 50;
            int currentX = (int) (startX + (targetX - startX) * eased);
            window.setLocation(currentX, location.y);
            
            if (progress >= 1.0f) {
                animTimer.stop();
                if (onComplete != null) onComplete.run();
            }
        });
        
        animTimer.start();
    }
    
    private static void animateOut(JWindow window, Runnable onComplete) {
        Timer animTimer = new Timer(10, null);
        final long startTime = System.currentTimeMillis();
        final Point startLocation = window.getLocation();
        
        animTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1.0f, (float) elapsed / ANIMATION_DURATION);
            
            // Ease-in animation
            float eased = (float) Math.pow(progress, 3);
            window.setOpacity(1.0f - eased);
            
            // Slide out to right
            int targetX = startLocation.x + 50;
            int currentX = (int) (startLocation.x + (targetX - startLocation.x) * eased);
            window.setLocation(currentX, startLocation.y);
            
            if (progress >= 1.0f) {
                animTimer.stop();
                if (onComplete != null) onComplete.run();
            }
        });
        
        animTimer.start();
    }
    
    private static JFrame getFrame(Component component) {
        if (component instanceof JFrame) {
            return (JFrame) component;
        }
        Component parent = component.getParent();
        while (parent != null && !(parent instanceof JFrame)) {
            parent = parent.getParent();
        }
        return (JFrame) parent;
    }
    
    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
    
    // Convenience methods
    public static void showSuccess(Component parent, String message) {
        show(parent, message, ToastType.SUCCESS);
    }
    
    public static void showError(Component parent, String message) {
        show(parent, message, ToastType.ERROR);
    }
    
    public static void showWarning(Component parent, String message) {
        show(parent, message, ToastType.WARNING);
    }
    
    public static void showInfo(Component parent, String message) {
        show(parent, message, ToastType.INFO);
    }
}

