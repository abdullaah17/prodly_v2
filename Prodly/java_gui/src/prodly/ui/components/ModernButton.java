package prodly.ui.components;

import prodly.ui.ModernTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern Button Component
 * Enhanced button with smooth animations and hover states
 */
public class ModernButton extends JButton {
    private Color defaultBackground;
    private Color hoverBackground;
    private Color pressedBackground;
    private boolean isAnimating = false;
    
    public ModernButton(String text) {
        super(text);
        initialize();
    }
    
    public ModernButton(String text, Color backgroundColor) {
        super(text);
        this.defaultBackground = backgroundColor;
        this.hoverBackground = backgroundColor.darker();
        this.pressedBackground = backgroundColor.darker().darker();
        initialize();
    }
    
    private void initialize() {
        setFont(ModernTheme.FONT_BUTTON);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(0, 42));
        
        if (defaultBackground == null) {
            defaultBackground = ModernTheme.PRIMARY;
            hoverBackground = ModernTheme.PRIMARY_LIGHT;
            pressedBackground = ModernTheme.PRIMARY.darker();
        }
        
        setBackground(defaultBackground);
        setupHoverEffects();
    }
    
    private void setupHoverEffects() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isAnimating) {
                    animateColor(defaultBackground, hoverBackground);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isAnimating) {
                    animateColor(getBackground(), defaultBackground);
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isAnimating) {
                    setBackground(pressedBackground);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isAnimating) {
                    setBackground(hoverBackground);
                }
            }
        });
    }
    
    private void animateColor(Color from, Color to) {
        if (isAnimating) return;
        
        isAnimating = true;
        Timer animTimer = new Timer(10, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 200;
        
        animTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1.0f, (float) elapsed / duration);
            
            // Ease-out animation
            float eased = 1.0f - (float) Math.pow(1.0 - progress, 3);
            
            int r = (int) (from.getRed() + (to.getRed() - from.getRed()) * eased);
            int g = (int) (from.getGreen() + (to.getGreen() - from.getGreen()) * eased);
            int b = (int) (from.getBlue() + (to.getBlue() - from.getBlue()) * eased);
            
            setBackground(new Color(r, g, b));
            
            if (progress >= 1.0f) {
                animTimer.stop();
                isAnimating = false;
            }
        });
        
        animTimer.start();
    }
    
    // Static factory methods for common button types
    public static ModernButton primary(String text) {
        return new ModernButton(text, ModernTheme.PRIMARY);
    }
    
    public static ModernButton success(String text) {
        return new ModernButton(text, ModernTheme.SUCCESS);
    }
    
    public static ModernButton danger(String text) {
        return new ModernButton(text, ModernTheme.ERROR);
    }
    
    public static ModernButton warning(String text) {
        return new ModernButton(text, ModernTheme.WARNING);
    }
    
    public static ModernButton info(String text) {
        return new ModernButton(text, ModernTheme.INFO);
    }
}

