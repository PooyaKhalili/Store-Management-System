package com.storesystem.view;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.storesystem.config.SetupUI;
import com.storesystem.view.MainFrame;


public class SideBarPanel extends JPanel {

    JButton categoryButton;
    JButton productButton;
    JButton customerButton;
    JButton shoppingCartButton;
    JButton purchaseHistoryButton;
    JButton reportButton;

    public SideBarPanel() {

        setupLayout();
        initComponents();
        addActionListeners();

    }

    private void initComponents() {
        categoryButton = SetupUI.createSidebarMenuButton("دسته بندی 🗂️", true);
        productButton = SetupUI.createSidebarMenuButton("محصولات 📦", false);
        customerButton = SetupUI.createSidebarMenuButton("مشتریان 👥", false);
        shoppingCartButton = SetupUI.createSidebarMenuButton("سبد خرید 🛒", false);
        purchaseHistoryButton = SetupUI.createSidebarMenuButton("تاریخچه خرید 🕒", false);
        reportButton = SetupUI.createSidebarMenuButton("گزارشات 📊", false);

        add(categoryButton);
        add(productButton);
        add(customerButton);
        add(shoppingCartButton);
        add(purchaseHistoryButton);
        add(reportButton);
    }

    private void setupLayout() {
        setLayout(new GridLayout(10, 1, 0, 5));
        setBackground(new Color(236, 240, 241)); 
        setBorder(new EmptyBorder(10, 10, 10, 0));
        setPreferredSize(new Dimension(150, 0)); 
        applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    private void addActionListeners() {
        categoryButton.addActionListener(e -> {
            SetupUI.updateButtonStyle(productButton, false);
            SetupUI.updateButtonStyle(customerButton, false);
            SetupUI.updateButtonStyle(shoppingCartButton, false);
            SetupUI.updateButtonStyle(purchaseHistoryButton, false);
            SetupUI.updateButtonStyle(reportButton, false);
            SetupUI.updateButtonStyle(categoryButton, true);
            MainFrame.switchPanel(new CategoryPanel());

        });
        productButton.addActionListener(e -> {
            SetupUI.updateButtonStyle(categoryButton, false);
            SetupUI.updateButtonStyle(customerButton, false);
            SetupUI.updateButtonStyle(shoppingCartButton, false);
            SetupUI.updateButtonStyle(purchaseHistoryButton, false);
            SetupUI.updateButtonStyle(reportButton, false);
            SetupUI.updateButtonStyle(productButton, true);
            MainFrame.switchPanel(new ProductPanel());
  
        });
        customerButton.addActionListener(e -> {
            
        });
        shoppingCartButton.addActionListener(e -> {
            
        });
        purchaseHistoryButton.addActionListener(e -> {
            
        });
        reportButton.addActionListener(e -> {
            
        });
    }


}
