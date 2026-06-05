package com.storesystem.view;

import java.awt.ComponentOrientation;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.storesystem.view.HeadPanel;
import com.storesystem.view.SideBarPanel;
import com.storesystem.view.CategoryPanel;

public class MainFrame extends JFrame {

    private static HeadPanel headPanel;
    private static SideBarPanel sidebarPanel;
    private static JPanel mainPanel;
    private static MainFrame mainFrame = new MainFrame();



    private MainFrame() {
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        headPanel = new HeadPanel();
        sidebarPanel = new SideBarPanel();
        mainPanel = new CategoryPanel();
    }

    private void setupLayout() {
        applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setTitle("سیستم مدیریت فروشگاه");
        setSize(1150, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new java.awt.BorderLayout());
        add(headPanel, java.awt.BorderLayout.NORTH);
        add(sidebarPanel, java.awt.BorderLayout.EAST);
        add(mainPanel, java.awt.BorderLayout.CENTER);
    }

    public static MainFrame getframe() {
        return mainFrame;
    }

    public static void switchPanel(JPanel newPanel) {
        mainFrame.getContentPane().remove(mainPanel);
        mainPanel = newPanel;
        mainFrame.add(mainPanel, java.awt.BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

}
