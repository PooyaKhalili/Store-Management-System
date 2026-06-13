package com.storesystem.view;

import javax.swing.JPanel;
import javax.swing.JTextField;
import com.storesystem.util.TableUtil;
import javax.swing.JButton;
import javax.swing.JLabel;
import com.storesystem.config.SetupUI;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import java.awt.Insets;


public class CustomerPanel extends JPanel {

    private JPanel CustomerTable;

    private JButton addButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton editButton;
    private JPanel buttonPanel;

    private JLabel searchLabel;
    private JTextField searchField;
    private JPanel searchPanel;


    public CustomerPanel(){
        initComponents();
        createButtonPanel();
        creatSearchPanel();
        setupLayout();
    }

    private void initComponents() {
        CustomerTable = new JPanel();
        CustomerTable = TableUtil.createTable(new Object[][] {}, 
                new String[] {"کد مشتری", "نام و نام خانوادگی", "شماره موبایل", "تعداد خرید", "مجموع پرداختی", "تاریخ عضویت"},
                new int[] {100, 200, 150, 100, 150, 120} );

        buttonPanel = new JPanel();
        searchPanel = new JPanel();
        searchLabel = new JLabel("جستجوی مشتری:");
        searchField = new JTextField();
        searchLabel.setFont(new Font("Vazir", Font.BOLD, 14));

        addButton = SetupUI.createButton(new Color(65, 115, 242),"افزودن +");
        editButton = SetupUI.createButton(new Color(251, 179, 22),"ویرایش ✏️");
        deleteButton = SetupUI.createButton(new Color(248, 78, 91),"حذف 🗑️");
        refreshButton = SetupUI.createButton(new Color(23, 162, 184),"بروزرسانی 🔄");
        
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(CustomerTable, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(searchPanel, BorderLayout.NORTH);
    }

    private void creatSearchPanel() {
        searchPanel.setBackground(new Color(245, 246, 248));
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 5, 0, 15);
        gbc.fill = GridBagConstraints.NONE;

        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Vazir", Font.BOLD, 14));
        searchField.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        searchPanel.add(searchLabel, gbc);
        searchPanel.add(searchField, gbc);

        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        searchPanel.add(spacer, gbc);
    }

    private void createButtonPanel() {
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridLayout(2, 5, 10,-25));
        buttonPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
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

    }

}
