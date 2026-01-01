package prodly.ui.components;

import prodly.ui.ModernTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern Modal Dialog Component
 * Provides a reusable modal dialog with backdrop and animations
 */
public class ModernModal extends JDialog {
    private JPanel contentPanel;
    private JButton closeButton;
    private boolean backdropClickable = false;
    
    public ModernModal(JFrame parent, String title) {
        super(parent, true); // Modal dialog
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initializeComponents(title);
        layoutComponents();
        setupAnimations();
    }
    
    private void initializeComponents(String title) {
        // Main container
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ModernTheme.SURFACE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(ModernTheme.PADDING_LARGE, 
                ModernTheme.PADDING_XLARGE, ModernTheme.PADDING_LARGE, ModernTheme.PADDING_XLARGE)
        ));
        
        // Title panel
        if (title != null && !title.isEmpty()) {
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(ModernTheme.SURFACE);
            titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, ModernTheme.PADDING_MEDIUM, 0));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(ModernTheme.FONT_HEADING);
            titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
            
            closeButton = new JButton("✕");
            closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            closeButton.setForeground(ModernTheme.TEXT_SECONDARY);
            closeButton.setBackground(ModernTheme.SURFACE);
            closeButton.setBorderPainted(false);
            closeButton.setFocusPainted(false);
            closeButton.setContentAreaFilled(false);
            closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            closeButton.setPreferredSize(new Dimension(30, 30));
            
            closeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    closeButton.setForeground(ModernTheme.ERROR);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    closeButton.setForeground(ModernTheme.TEXT_SECONDARY);
                }
            });
            
            closeButton.addActionListener(e -> close());
            
            titlePanel.add(titleLabel, BorderLayout.WEST);
            titlePanel.add(closeButton, BorderLayout.EAST);
            
            contentPanel.add(titlePanel, BorderLayout.NORTH);
        }
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Backdrop panel with semi-transparent background
        JPanel backdrop = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        backdrop.setOpaque(false);
        
        // Add click listener to backdrop
        backdrop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (backdropClickable && e.getSource() == backdrop) {
                    close();
                }
            }
        });
        
        // Center the content
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(contentPanel);
        
        backdrop.add(centerPanel, BorderLayout.CENTER);
        add(backdrop, BorderLayout.CENTER);
    }
    
    private void setupAnimations() {
        setOpacity(0f);
    }
    
    public void setContent(Component component) {
        contentPanel.add(component, BorderLayout.CENTER);
    }
    
    public void addButton(JButton button) {
        // Create button panel if it doesn't exist
        Component existing = contentPanel.getComponent(1);
        JPanel buttonPanel;
        
        if (existing instanceof JPanel && existing.getName() != null && existing.getName().equals("buttonPanel")) {
            buttonPanel = (JPanel) existing;
        } else {
            buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, ModernTheme.PADDING_SMALL, 0));
            buttonPanel.setName("buttonPanel");
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(ModernTheme.PADDING_MEDIUM, 0, 0, 0));
            buttonPanel.setBackground(ModernTheme.SURFACE);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        }
        
        buttonPanel.add(button);
    }
    
    public void setBackdropClickable(boolean clickable) {
        this.backdropClickable = clickable;
    }
    
    public void showModal() {
        pack();
        centerDialog();
        animateIn();
        setVisible(true);
    }
    
    public void close() {
        animateOut(() -> {
            dispose();
        });
    }
    
    private void centerDialog() {
        Window parent = getOwner();
        
        if (parent != null) {
            Point parentLocation = parent.getLocation();
            Dimension parentSize = parent.getSize();
            Dimension dialogSize = getSize();
            
            int x = parentLocation.x + (parentSize.width - dialogSize.width) / 2;
            int y = parentLocation.y + (parentSize.height - dialogSize.height) / 2;
            
            setLocation(x, y);
        } else {
            setLocationRelativeTo(null);
        }
    }
    
    private void animateIn() {
        Timer animTimer = new Timer(10, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 200;
        
        animTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1.0f, (float) elapsed / duration);
            
            // Ease-out animation
            float eased = 1.0f - (float) Math.pow(1.0 - progress, 3);
            setOpacity(eased);
            
            // Note: Java Swing doesn't support CSS-like transforms easily
            // We'll just use opacity for smooth animation
            
            if (progress >= 1.0f) {
                animTimer.stop();
                setOpacity(1.0f);
            }
        });
        
        animTimer.start();
    }
    
    private void animateOut(Runnable onComplete) {
        Timer animTimer = new Timer(10, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 150;
        
        animTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1.0f, (float) elapsed / duration);
            
            // Ease-in animation
            float eased = (float) Math.pow(progress, 3);
            setOpacity(1.0f - eased);
            
            if (progress >= 1.0f) {
                animTimer.stop();
                if (onComplete != null) onComplete.run();
            }
        });
        
        animTimer.start();
    }
    
    // Backdrop is handled by the backdrop panel, no need for paintComponent
}

