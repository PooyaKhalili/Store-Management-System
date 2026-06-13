package com.storesystem.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import com.storesystem.config.SetupUI;
import com.storesystem.util.TableUtil;

public class ReportPanel extends JPanel {

    JPanel topTable;
    JPanel bottomTable;
    JButton refreshButton;
    JLabel totalRevenueLabel;
    JLabel topTitleLabel;
    JLabel bottomTitleLabel;
    JPanel footerPanel;
    JPanel topPanel;
    JPanel bottomPanel;
    JSplitPane splitPane;
    int total=0;

    public ReportPanel() {
        initComponents();
        createFooterPanel();
        setupLayout();
    }

    private void initComponents() {
        topTable = TableUtil.createTable(new Object[][] {}, 
        new String[] {"نام کالا", "تعداد فروش"},
        new int[] {400, 200});

        bottomTable = TableUtil.createTable(new Object[][] {}, 
        new String[] {"مشتری", "تعداد سفارش", "مجموع خرید"},
        new int[] {250, 150, 200});

        refreshButton = SetupUI.createButton(new Color(23, 162, 184), "بروزرسانی 🔄");
        
        totalRevenueLabel = new JLabel("درآمد کل: "+total+ " ریال");
        totalRevenueLabel.setFont(new Font("Vazir", Font.BOLD, 14));
        
        topTitleLabel = new JLabel("پرفروش‌ترین کالاها");
        topTitleLabel.setFont(new Font("Vazir", Font.BOLD, 13));
        topTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        topTitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

        bottomTitleLabel = new JLabel("خلاصه خرید مشتریان");
        bottomTitleLabel.setFont(new Font("Vazir", Font.BOLD, 13));
        bottomTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        bottomTitleLabel.setBorder(BorderFactory.createEmptyBorder(15, 5, 10, 5));

        footerPanel = new JPanel();
        topPanel = new JPanel();
        bottomPanel = new JPanel();
    }

    private void createFooterPanel() {
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setLayout(new BorderLayout());
        footerPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        footerPanel.add(totalRevenueLabel, BorderLayout.LINE_START);
        footerPanel.add(refreshButton, BorderLayout.LINE_END);
    }

    private void setupLayout() {
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(topTitleLabel, BorderLayout.NORTH);
        topPanel.add(topTable, BorderLayout.CENTER);
        topPanel.setMinimumSize(new Dimension(0, 100));

        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(bottomTitleLabel, BorderLayout.NORTH);
        bottomPanel.add(bottomTable, BorderLayout.CENTER);
        bottomPanel.setMinimumSize(new Dimension(0, 100));

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        SwingUtilities.invokeLater(() -> {
            splitPane.setDividerLocation(0.5);
        });

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // حاشیه کل صفحه
        
        add(splitPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
}