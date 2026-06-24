package com.storesystem.config;

import javax.swing.*;
import java.awt.*;

public class SetupUI {

public static JButton createButton(Color bgColor, String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(130, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.putClientProperty("FlatLaf.style", "arc: 15; borderWidth: 0; focusWidth: 0;");
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Vazir", Font.BOLD, 13));
        button.setHorizontalAlignment(SwingConstants.RIGHT);
        return button;
    }

    public static JButton createSidebarMenuButton(String text, boolean isActive) {
    JButton button = new JButton(text);
    
    button.setHorizontalAlignment(SwingConstants.RIGHT);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setFont(new Font("Vazir", Font.PLAIN, 14));
    
    if (isActive) {
        Color activeBgColor = new Color(237, 242, 252); 
        Color activeTextColor = new Color(44, 111, 183); 
        
        button.setBackground(activeBgColor);
        button.setForeground(activeTextColor);
        
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 10, activeTextColor), 
            BorderFactory.createEmptyBorder(10, 15, 10, 15) 
        ));
        
        button.putClientProperty("FlatLaf.style", "hoverBackground: " + "#edf2fc"); 
        
    } else {
        button.setBackground(Color.WHITE); 
        button.setForeground(new Color(80, 80, 80)); 
        
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 19));
    
        button.putClientProperty("FlatLaf.style", "borderWidth: 0; focusWidth: 0;");
    }
    
    return button;
}

public static void updateButtonStyle(JButton button, boolean isActive) {
        if (isActive) {
            Color activeBgColor = new Color(237, 242, 252);
            Color activeTextColor = new Color(44, 111, 183);
            button.setBackground(activeBgColor);
            button.setForeground(activeTextColor);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 0, 10, activeTextColor),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            button.putClientProperty("FlatLaf.style", "hoverBackground: #edf2fc; borderWidth: 0; focusWidth: 0;");
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(80, 80, 80));
            button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 19));
            button.putClientProperty("FlatLaf.style", "borderWidth: 0; focusWidth: 0;");
        }
    }
}
