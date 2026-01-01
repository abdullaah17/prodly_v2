package prodly.ui.components;

import prodly.ui.ModernTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Modern Card Component
 * A reusable card panel with hover effects and optional actions
 */
public class ModernCard extends JPanel {
    private boolean hoverable = false;
    private boolean clickable = false;
    private Runnable onClickAction;
    private Color defaultBackground;
    
    public ModernCard() {
        this(false, false);
    }
    
    public ModernCard(boolean hoverable, boolean clickable) {
        this.hoverable = hoverable;
        this.clickable = clickable;
        this.defaultBackground = ModernTheme.SURFACE;
        
        setBackground(defaultBackground);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
            BorderFactory.createEmptyBorder(ModernTheme.PADDING_MEDIUM, 
                ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM)
        ));
        
        if (hoverable || clickable) {
            setupInteractions();
        }
    }
    
    private void setupInteractions() {
        if (clickable) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (hoverable || clickable) {
                    animateHover(true);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (hoverable || clickable) {
                    animateHover(false);
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickable && onClickAction != null) {
                    onClickAction.run();
                }
            }
        });
    }
    
    private void animateHover(boolean isHover) {
        Timer animTimer = new Timer(10, null);
        final long startTime = System.currentTimeMillis();
        final int duration = 150;
        final Color startColor = getBackground();
        final Color endColor = isHover ? 
            new Color(Math.min(255, defaultBackground.getRed() + 10),
                     Math.min(255, defaultBackground.getGreen() + 10),
                     Math.min(255, defaultBackground.getBlue() + 10)) : 
            defaultBackground;
        
        animTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1.0f, (float) elapsed / duration);
            
            // Ease-out animation
            float eased = 1.0f - (float) Math.pow(1.0 - progress, 3);
            
            int r = (int) (startColor.getRed() + (endColor.getRed() - startColor.getRed()) * eased);
            int g = (int) (startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * eased);
            int b = (int) (startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * eased);
            
            setBackground(new Color(r, g, b));
            
            // Update border
            if (isHover) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ModernTheme.PRIMARY, 2),
                    BorderFactory.createEmptyBorder(ModernTheme.PADDING_MEDIUM, 
                        ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM)
                ));
            } else {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ModernTheme.BORDER, 1),
                    BorderFactory.createEmptyBorder(ModernTheme.PADDING_MEDIUM, 
                        ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM, ModernTheme.PADDING_MEDIUM)
                ));
            }
            
            repaint();
            
            if (progress >= 1.0f) {
                animTimer.stop();
            }
        });
        
        animTimer.start();
    }
    
    public void setOnClick(Runnable action) {
        this.onClickAction = action;
        this.clickable = true;
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    public void setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
    }
    
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
        if (clickable) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}

