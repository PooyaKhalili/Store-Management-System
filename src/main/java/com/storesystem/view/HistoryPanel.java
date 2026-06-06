package com.storesystem.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.storesystem.config.SetupUI;
import com.storesystem.util.TableUtil;

public class HistoryPanel extends JPanel {

    private JPanel topTablePanel;
    private JPanel bottomTablePanel;
    private JPanel searchPanel;
    private JPanel buttonPanel;
    private JSplitPane splitPane;
    
    private JLabel advancedSearchLabel;
    private JTextField searchField;
    private JButton btnStartDate;
    private JButton btnEndDate;
    private JButton btnResetFilters;
    
    private JButton btnRefresh;
    private JButton btnDeleteSelected;
    private JButton btnDeleteAll;
    private JButton btnPrintReceipt;
    private JButton btnExportPDF;

    public HistoryPanel() {
        initComponents();
        createSearchPanel();
        createButtonPanel();
        setupLayout();
    }

    private void initComponents() {
        topTablePanel = TableUtil.createTable(new Object[][] {}, 
                new String[] {"شماره سفارش", "شناسه مشتری", "تاریخ جلالی", "جمع جزء", "تخفیف", "مالیات", "جمع کل"},
                new int[] {100, 100, 150, 100, 100, 100, 120} ); 

        bottomTablePanel = TableUtil.createTable(new Object[][] {}, 
                new String[] {"کالا", "قیمت واحد", "تعداد", "جمع"},
                new int[] {300, 150, 80, 150} );

        advancedSearchLabel = new JLabel("جست‌وجوی پیشرفته:");
        advancedSearchLabel.setFont(new Font("Vazir", Font.BOLD, 14));
        searchField = new JTextField();
        
        btnStartDate = SetupUI.createButton(new Color(100, 116, 139), "انتخاب تاریخ/ساعت شروع");

        btnEndDate = SetupUI.createButton(new Color(100, 116, 139), "انتخاب تاریخ/ساعت پایان");

        btnResetFilters = SetupUI.createButton(new Color(16, 69, 122), "ریست فیلترها ↺"); 

        btnRefresh = SetupUI.createButton(new Color(23, 162, 184), "بروزرسانی 🔄️");
        
        btnDeleteSelected = SetupUI.createButton(new Color(248, 78, 91), "حذف انتخابی 🗑️");
        
        btnDeleteAll = SetupUI.createButton(new Color(192, 57, 43), "حذف همه 🛑");
        
        btnPrintReceipt = SetupUI.createButton(new Color(46, 204, 113), "رسید چاپی 🖨️");
        
        btnExportPDF = SetupUI.createButton(new Color(251, 179, 22), "خروجی PDF 📄⤓");

        searchPanel = new JPanel();
        buttonPanel = new JPanel();
    }

    private void createSearchPanel() {
        searchPanel.setBackground(new Color(245, 246, 248));
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0; 
        gbc.insets = new Insets(0, 5, 0, 10); 
        gbc.fill = GridBagConstraints.NONE;

        searchField.setPreferredSize(new Dimension(200, 32));
        searchField.setFont(new Font("Vazir", Font.BOLD, 14));
        searchField.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        btnStartDate.setPreferredSize(new Dimension(170, 32));
        btnEndDate.setPreferredSize(new Dimension(170, 32));
        btnResetFilters.setPreferredSize(new Dimension(130, 32));

        searchPanel.add(btnResetFilters, gbc);
        searchPanel.add(btnStartDate, gbc);
        searchPanel.add(btnEndDate, gbc);
        searchPanel.add(advancedSearchLabel, gbc);
        searchPanel.add(searchField, gbc);
    
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        searchPanel.add(spacer, gbc);
    }

    private void createButtonPanel() {
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.fill = GridBagConstraints.NONE;

        btnRefresh.setPreferredSize(new Dimension(110, 35));
        btnDeleteSelected.setPreferredSize(new Dimension(150, 35));
        btnDeleteAll.setPreferredSize(new Dimension(140, 35));
        btnPrintReceipt.setPreferredSize(new Dimension(140, 35));
        btnExportPDF.setPreferredSize(new Dimension(130, 35));

        buttonPanel.add(btnDeleteSelected, gbc);
        buttonPanel.add(btnDeleteAll, gbc);
        buttonPanel.add(btnPrintReceipt, gbc);
        buttonPanel.add(btnExportPDF, gbc);
        buttonPanel.add(btnRefresh, gbc);
        
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        buttonPanel.add(spacer, gbc);

        addActionListeners();
    }

    private void setupLayout() {
        topTablePanel.setMinimumSize(new Dimension(0, 150));
        bottomTablePanel.setMinimumSize(new Dimension(0, 150));

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topTablePanel, bottomTablePanel);
        splitPane.setResizeWeight(0.6);
        splitPane.setDividerSize(10);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true); 

        SwingUtilities.invokeLater(() -> splitPane.setDividerLocation(0.5));

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // حاشیه کل صفحه
        
        add(searchPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addActionListeners() {
        
    }
}