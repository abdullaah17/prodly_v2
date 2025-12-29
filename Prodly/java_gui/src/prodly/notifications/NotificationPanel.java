package prodly.notifications;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationPanel extends JPanel {
    private List<Notification> notifications;
    private JPanel notificationsContainer;
    
    public NotificationPanel() {
        notifications = new ArrayList<>();
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        notificationsContainer = new JPanel();
        notificationsContainer.setLayout(new BoxLayout(notificationsContainer, BoxLayout.Y_AXIS));
        notificationsContainer.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(notificationsContainer);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void addNotification(String title, String message, NotificationType type) {
        Notification notification = new Notification(title, message, type);
        notifications.add(notification);
        notificationsContainer.add(notification);
        notificationsContainer.revalidate();
        notificationsContainer.repaint();
    }
    
    public void clearNotifications() {
        notifications.clear();
        notificationsContainer.removeAll();
        notificationsContainer.revalidate();
        notificationsContainer.repaint();
    }
    
    class Notification extends JPanel {
        private String title;
        private String message;
        private NotificationType type;
        
        public Notification(String title, String message, NotificationType type) {
            this.title = title;
            this.message = message;
            this.type = type;
            
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getTypeColor(), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            setBackground(Color.WHITE);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            titleLabel.setForeground(getTypeColor());
            
            JLabel messageLabel = new JLabel(message);
            messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            
            JButton closeButton = new JButton("×");
            closeButton.setFont(new Font("Arial", Font.BOLD, 16));
            closeButton.setBorder(null);
            closeButton.setBackground(Color.WHITE);
            closeButton.setForeground(Color.GRAY);
            closeButton.setFocusPainted(false);
            closeButton.addActionListener(e -> {
                notifications.remove(this);
                notificationsContainer.remove(this);
                notificationsContainer.revalidate();
                notificationsContainer.repaint();
            });
            
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(Color.WHITE);
            contentPanel.add(titleLabel, BorderLayout.NORTH);
            contentPanel.add(messageLabel, BorderLayout.CENTER);
            
            add(contentPanel, BorderLayout.CENTER);
            add(closeButton, BorderLayout.EAST);
            
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        
        private Color getTypeColor() {
            switch (type) {
                case INFO: return new Color(70, 130, 180);
                case SUCCESS: return new Color(60, 179, 113);
                case WARNING: return new Color(255, 140, 0);
                case ERROR: return new Color(220, 20, 60);
                default: return Color.GRAY;
            }
        }
    }
    
    enum NotificationType {
        INFO, SUCCESS, WARNING, ERROR
    }
}

