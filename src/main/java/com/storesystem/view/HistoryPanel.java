package com.storesystem.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.*;

import com.storesystem.config.SetupUI;
import com.storesystem.controller.HistoryController;
import com.storesystem.util.JalaliDatePickerDialog;
import com.storesystem.util.TableUtil;
import java.awt.Window;
import java.util.List;

public class HistoryPanel extends JPanel {
    private final HistoryController controller;
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
    
    JButton btnRefresh;
    private JButton btnDeleteSelected;
    private JButton btnDeleteAll;
    private JButton btnPrintReceipt;
    private JButton btnExportPDF;

    public HistoryPanel() {
        this.controller = new HistoryController(this);
        initComponents();
        createSearchPanel();
        createButtonPanel();
        setupLayout();
        setupTableSelection();
    }

    private void initComponents() {
        topTablePanel = TableUtil.createTable(new ArrayList<>(), 
                new String[] {"شماره سفارش", "کد مشتری", "تاریخ جلالی", "جمع جزء", "تخفیف", "مالیات", "جمع کل"},
                new int[] {100, 100, 150, 100, 100, 100, 120} ); 

        bottomTablePanel = TableUtil.createTable(new ArrayList<>(), 
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
        searchPanel.setBackground(Color.WHITE);
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
        topTablePanel.setMinimumSize(new Dimension(0, 50));
        bottomTablePanel.setMinimumSize(new Dimension(0, 50));

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
        btnDeleteAll.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "آیا از حذف تمامی سفارشات مطمئن هستید؟",
                    "حذف همه",
                    JOptionPane.YES_NO_OPTION
                    );
            if (confirm == JOptionPane.YES_OPTION) {
                String result = controller.deleteAllOrders();
                JOptionPane.showMessageDialog(this, result);
                controller.refreshTopTable();
                controller.clearBottomTable();
            }
        });
        btnDeleteSelected.addActionListener(e -> {
            JTable topTable = TableUtil.getTableFromPanel(topTablePanel);
            if (topTable != null && topTable.getSelectedRow() >= 0) {
                long orderId = Long.parseLong(topTable.getValueAt(topTable.getSelectedRow(), 0).toString());
                int confirm = JOptionPane.showConfirmDialog(this,
                        "آیا از حذف این سفارش اطمینان دارید؟",
                        "حذف سفارش",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String result = controller.deleteOrder((int)orderId);
                    JOptionPane.showMessageDialog(this, result);
                    controller.refreshTopTable();
                    controller.clearBottomTable();
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "لطفا ابتدا یک سفارش را انتخاب کنید!", "خطا", JOptionPane.ERROR_MESSAGE);
            }
        
        });
        btnPrintReceipt.addActionListener(e -> {
        
        });
        btnExportPDF.addActionListener(e -> {
        
        });
        btnRefresh.addActionListener(e -> {
            controller.refreshTopTable();
            controller.clearBottomTable();
        });
        btnStartDate.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(btnStartDate);
            JalaliDatePickerDialog picker = new JalaliDatePickerDialog(window, "انتخاب تاریخ و ساعت شروع");
            picker.setVisible(true);
            if (picker.getSelectedDate() != null) {
                btnStartDate.setText(picker.getSelectedDate());
                applyDateRangeFilter();
            }
        });
        btnEndDate.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(btnStartDate);
            JalaliDatePickerDialog picker = new JalaliDatePickerDialog(window, "انتخاب تاریخ و ساعت پایان");
            picker.setVisible(true);
            if (picker.getSelectedDate() != null) {
                btnEndDate.setText(picker.getSelectedDate());
                applyDateRangeFilter();
            }
        
        });
        btnResetFilters.addActionListener(e -> {
            btnStartDate.setText("انتخاب تاریخ/ساعت شروع");
            btnEndDate.setText("انتخاب تاریخ/ساعت پایان");

            searchField.setText("");
            TableUtil.refreshTable(topTablePanel, controller.getAllOrders());
            TableUtil.refreshTable(bottomTablePanel, new ArrayList<>());
            JOptionPane.showMessageDialog(this,
                        "فیلترها با موفقیت ریست شدند",
                        "ریست فیلترها",
                        JOptionPane.INFORMATION_MESSAGE);

        });
        searchField.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            TableUtil.refreshTable(topTablePanel, controller.searchOrders(searchText));
            controller.clearBottomTable();

        });

    }
    private void setupTableSelection() {
        JTable topTable = TableUtil.getTableFromPanel(topTablePanel);
        if (topTable != null) {
            topTable.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = topTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        long orderId = Long.parseLong(topTable.getValueAt(selectedRow, 0).toString());
                        controller.showOrderItems(orderId);
                    }
                }
            });
        }
    }
    private void applyDateRangeFilter(){
        String startDate = btnStartDate.getText().trim();
        String endDate = btnEndDate.getText().trim();
        if((!startDate.equals("انتخاب تاریخ/ساعت شروع"))|| (!endDate.equals("انتخاب تاریخ/ساعت پایان"))){
            List<Object[]> list = new ArrayList<>();
            if((!startDate.equals("انتخاب تاریخ/ساعت شروع"))&& (!endDate.equals("انتخاب تاریخ/ساعت پایان"))){
                list = controller.searchByDateRange(startDate, endDate);
            } else if (!startDate.equals("انتخاب تاریخ/ساعت شروع")) {
                list = controller.searchByDateRange(startDate, null);
            } else {
                list = controller.searchByDateRange(null, endDate);
            }
            TableUtil.refreshTable(topTablePanel, list);
            controller.clearBottomTable();
        }
    }
    public void refreshTopTable(List<Object[]> data) {
        TableUtil.refreshTable(topTablePanel, data);
    }

    public void refreshBottomTable(List<Object[]> data) {
        TableUtil.refreshTable(bottomTablePanel, data);
    }

    public void clearBottomTable() {
        TableUtil.refreshTable(bottomTablePanel, new ArrayList<>());
    }

}