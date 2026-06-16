package com.storesystem.view;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.storesystem.config.SetupUI;

public class SideBarPanel extends JPanel {

    private JButton categoryButton;
    private JButton productButton;
    private JButton customerButton;
    private JButton shoppingCartButton;
    private JButton purchaseHistoryButton;
    private JButton reportButton;

    // ۱. تعریف پنل‌ها به صورت متغیرهای کلاس تا فقط یک بار در حافظه ساخته شوند
    private CategoryPanel categoryPanel;
    private ProductPanel productPanel;
    private CustomerPanel customerPanel;
    private OrderPanel orderPanel;
    private HistoryPanel historyPanel;
    private ReportPanel reportPanel;

    public SideBarPanel() {
        setupLayout();
        initComponents();
        initPanels();
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

    private void initPanels() {
        categoryPanel = new CategoryPanel();
        productPanel = new ProductPanel();
        customerPanel = new CustomerPanel();
        orderPanel = new OrderPanel();
        historyPanel = new HistoryPanel();
        reportPanel = new ReportPanel();

        orderPanel.onAddCustomerClick = () -> {
            if (customerPanel.addButton != null) {
                customerPanel.addButton.doClick();
            }
        };
    }

    private void addActionListeners() {
        categoryButton.addActionListener(e -> {
            updateAllButtons(categoryButton);
            MainFrame.switchPanel(categoryPanel);
        });
        
        productButton.addActionListener(e -> {
            updateAllButtons(productButton);
            MainFrame.switchPanel(productPanel);
        });
        
        customerButton.addActionListener(e -> {
            updateAllButtons(customerButton);
            MainFrame.switchPanel(customerPanel);
        });
        
        shoppingCartButton.addActionListener(e -> {
            updateAllButtons(shoppingCartButton);
            MainFrame.switchPanel(orderPanel);
        });
        
        purchaseHistoryButton.addActionListener(e -> {
            updateAllButtons(purchaseHistoryButton);
            MainFrame.switchPanel(historyPanel);
        });
        
        reportButton.addActionListener(e -> {
            updateAllButtons(reportButton);
            MainFrame.switchPanel(reportPanel);
        });
    }

    // متد کمکی برای جلوگیری از کدهای تکراری در تغییر استایل دکمه‌ها
    private void updateAllButtons(JButton activeBtn) {
        SetupUI.updateButtonStyle(categoryButton, categoryButton == activeBtn);
        SetupUI.updateButtonStyle(productButton, productButton == activeBtn);
        SetupUI.updateButtonStyle(customerButton, customerButton == activeBtn);
        SetupUI.updateButtonStyle(shoppingCartButton, shoppingCartButton == activeBtn);
        SetupUI.updateButtonStyle(purchaseHistoryButton, purchaseHistoryButton == activeBtn);
        SetupUI.updateButtonStyle(reportButton, reportButton == activeBtn);
    }
}