package com.storesystem.view;

import javax.swing.JPanel;
import javax.swing.JTextField;
import com.storesystem.util.TableUtil;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.storesystem.config.SetupUI;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;


public class ProductPanel extends JPanel {

    JPanel productTable;
    JButton addButton;
    JButton deleteButton;
    JButton refreshButton;
    JButton importCSV;
    JButton exportCSV;
    JButton editButton;
    JButton lowStockButton;
    JPanel buttonPanel;
    JLabel categoryLabel;
    JLabel searchLabel;
    JComboBox<String> comboBox;
    JTextField searchField;
    JPanel searchPanel;


    public ProductPanel() {
        initComponents();
        createButtonPanel();
        creatSearchPanel();
        setupLayout();
    }

    private void initComponents() {
        productTable = new JPanel();
        productTable=TableUtil.createTable(new Object[][] {},
                new String[] {"کد کالا", "نام کالا", "قیمت", "موجودی", "دسته‌بندی"},
                new int[] {90, 350, 130, 90, 180});
        buttonPanel = new JPanel();
        searchPanel = new JPanel();
        searchField = new JTextField();
        categoryLabel = new JLabel("دسته‌بندی:");
        searchLabel = new JLabel("جست‌وجو:");
        comboBox = new JComboBox<String>(new String[] {"همه"});

        addButton = SetupUI.createButton(new Color(65, 115, 242),"افزودن +");
        editButton = SetupUI.createButton(new Color(251, 179, 22),"ویرایش ✏️");
        deleteButton = SetupUI.createButton(new Color(248, 78, 91),"حذف 🗑️");
        lowStockButton = SetupUI.createButton(new Color(156, 39, 176),"کمبود موجودی ⚠️");
        importCSV = SetupUI.createButton(new Color(46, 204, 113),"ورود 📥 CSV");
        exportCSV = SetupUI.createButton(new Color(52, 73, 94),"خروج 📤 CSV");
        refreshButton = SetupUI.createButton(new Color(23, 162, 184),"بروزرسانی 🔄");
        
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(productTable, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(searchPanel, BorderLayout.NORTH);
    }

    private void creatSearchPanel() {
        searchPanel.setBackground(new Color(245, 246, 248));
        searchPanel.setLayout(new java.awt.GridBagLayout());
        searchPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        searchPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(0, 5, 0, 15);
        gbc.fill = java.awt.GridBagConstraints.NONE;

        comboBox.setPreferredSize(new java.awt.Dimension(120, 30));
        searchField.setPreferredSize(new java.awt.Dimension(250, 30));

        searchPanel.add(categoryLabel, gbc);
        searchPanel.add(comboBox, gbc);
        searchPanel.add(searchLabel, gbc);
        searchPanel.add(searchField, gbc);

        gbc.weightx = 1.0;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        javax.swing.JPanel spacer = new javax.swing.JPanel();
        spacer.setOpaque(false); // نامرئی کردن فنر
        searchPanel.add(spacer, gbc);
    }

    private void createButtonPanel() {
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridLayout(2, 8, 10,-25));
        buttonPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(lowStockButton);
        buttonPanel.add(importCSV);
        buttonPanel.add(exportCSV);
        JPanel[] p = new JPanel[8]; 
        for (int i = 0; i < 8; i++) {
            p[i] = new JPanel();
            p[i].setBackground(Color.WHITE);
            p[i].setPreferredSize(new Dimension(15,10));
            buttonPanel.add(p[i]);
        }
        addActionListeners();
    }

    private void addActionListeners() {
        addButton.addActionListener(e -> {
            
        });
        editButton.addActionListener(e -> {
            
        });
        deleteButton.addActionListener(e -> {
            
        });
        refreshButton.addActionListener(e -> {
            
        });
        lowStockButton.addActionListener(e -> {
            
        });
        importCSV.addActionListener(e -> {
            
        });
        exportCSV.addActionListener(e -> {
            
        });
    }


}
