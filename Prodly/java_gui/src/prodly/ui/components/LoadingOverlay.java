package prodly.ui.components;

import prodly.ui.ModernTheme;
import javax.swing.*;
import java.awt.*;

/**
 * Loading Overlay Component
 * Displays a semi-transparent overlay with a loading spinner
 */
public class LoadingOverlay extends JPanel {
    private JLabel messageLabel;
    private SpinnerPanel spinner;
    private boolean isVisible = false;
    
    public LoadingOverlay() {
        setOpaque(false);
        setLayout(new BorderLayout());
        
        // Create spinner panel
        spinner = new SpinnerPanel();
        
        // Message label
        messageLabel = new JLabel("Loading...");
        messageLabel.setFont(ModernTheme.FONT_BODY);
        messageLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);
        centerPanel.add(spinner, BorderLayout.CENTER);
        centerPanel.add(messageLabel, BorderLayout.SOUTH);
        
        // Card container
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(ModernTheme.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        card.add(centerPanel, BorderLayout.CENTER);
        
        add(card, BorderLayout.CENTER);
        setVisible(false);
    }
    
    public void show(String message) {
        if (message != null) {
            messageLabel.setText(message);
        }
        isVisible = true;
        setVisible(true);
        spinner.start();
        revalidate();
        repaint();
    }
    
    public void hide() {
        isVisible = false;
        setVisible(false);
        spinner.stop();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if (isVisible) {
            // Draw semi-transparent background
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2d.setColor(ModernTheme.BACKGROUND);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
        super.paintComponent(g);
    }
    
    /**
     * Animated spinner panel
     */
    private static class SpinnerPanel extends JPanel {
        private Timer animationTimer;
        private float rotation = 0f;
        private static final int SPINNER_SIZE = 40;
        private static final int STROKE_WIDTH = 4;
        
        public SpinnerPanel() {
            setPreferredSize(new Dimension(SPINNER_SIZE, SPINNER_SIZE));
            setOpaque(false);
        }
        
        public void start() {
            if (animationTimer != null && animationTimer.isRunning()) {
                return;
            }
            
            animationTimer = new Timer(16, e -> {
                rotation += 0.1f;
                if (rotation > 2 * Math.PI) {
                    rotation = 0;
                }
                repaint();
            });
            animationTimer.start();
        }
        
        public void stop() {
            if (animationTimer != null) {
                animationTimer.stop();
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = SPINNER_SIZE / 2 - STROKE_WIDTH;
            
            // Draw arc
            g2d.setStroke(new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(ModernTheme.PRIMARY);
            
            // Create arc with rotation
            int startAngle = (int) Math.toDegrees(rotation);
            int arcAngle = 270; // 3/4 circle
            
            g2d.drawArc(
                centerX - radius,
                centerY - radius,
                radius * 2,
                radius * 2,
                startAngle,
                arcAngle
            );
            
            g2d.dispose();
        }
    }
}

