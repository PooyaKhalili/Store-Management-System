package com.storesystem.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import com.storesystem.config.SetupUI;
import com.storesystem.util.TableUtil;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

public class CategoryPanel extends JPanel {

    private JPanel table;
    private JPanel buttonPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public CategoryPanel() {
        initComponents();
        createButtonPanel();
        setupLayout();
    }

    private void initComponents() {
        table = TableUtil.createTable(new ArrayList<>() , 
            new String[] { "کد دسته بندی", "نام دسته بندی"},
            new int[] {80,450} );     
        buttonPanel = new JPanel();
        addButton = SetupUI.createButton(new Color(65, 115, 242), "افزودن +" );
        editButton = SetupUI.createButton(new Color(251, 179, 22), "ویرایش ✏️");
        deleteButton = SetupUI.createButton(new Color(248, 78, 91), "حذف 🗑️");
        refreshButton = SetupUI.createButton(new Color(23, 162, 184), "بروزرسانی 🔄️");
    }
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(table, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createButtonPanel() {
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridLayout(2, 5, 10,-25));
        buttonPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        JPanel[] p = new JPanel[5]; 
        for (int i = 0; i < 5; i++) {
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
    }
    

}
