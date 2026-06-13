package com.storesystem;

import javax.swing.SwingUtilities;
import com.storesystem.view.MainFrame;

public class Main {
    public static void main(String[] args) {

        try {
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatIntelliJLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = MainFrame.getframe();
            frame.setVisible(true);
        });
    }
}
