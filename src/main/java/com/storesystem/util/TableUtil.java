package com.storesystem.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class TableUtil {

    public static JPanel createTable(List<Object[]> data, String[] columnNames, int[] columnWidths) {
        
        Object[][] dataArray = new Object[0][0];
        if (data != null) {
            dataArray = data.toArray(new Object[0][]);
        }

        DefaultTableModel model = new DefaultTableModel(dataArray, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        JTable table = new JTable(model);
        
        table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        table.setRowHeight(50); 
        table.setShowVerticalLines(false); 
        table.setShowHorizontalLines(true); 
        table.setGridColor(new Color(235, 235, 235)); 
        table.setSelectionBackground(new Color(237, 242, 252));
        table.setSelectionForeground(new Color(40, 40, 40)); 
        table.setFillsViewportHeight(true); 
        table.setFont(new Font("Vazir", Font.PLAIN, 12)); 
        table.setBackground(Color.WHITE);

        if (columnWidths != null && columnWidths.length == columnNames.length) {
            for (int i = 0; i < columnWidths.length; i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
            }
        }

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.RIGHT); 
                label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); 
                return label;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        JTableHeader header = table.getTableHeader();
        header.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); 
        header.setReorderingAllowed(false); 
        header.setPreferredSize(new Dimension(0, 50)); 
        
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(250, 250, 250)); 
                label.setForeground(new Color(80, 80, 80)); 
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                label.setFont(new Font("Vazir", Font.BOLD, 12));
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225)),
                    BorderFactory.createEmptyBorder(0, 0, 0, 15)
                ));
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.putClientProperty("FlatLaf.style", "arc: 15;");
        
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        wrapperPanel.setBorder(new EmptyBorder(20, 30, 20, 30)); 
        wrapperPanel.add(scrollPane, BorderLayout.CENTER);

        return wrapperPanel;
    }

    public static void refreshTable(JPanel wrapperPanel, List<Object[]> newData) {
        for (Component comp : wrapperPanel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JTable) {
                    JTable table = (JTable) view;
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    
                    model.setRowCount(0);
                    
                    if (newData != null) {
                        for (Object[] row : newData) {
                            model.addRow(row);
                        }
                    }
                    
                    return;
                }
            }
        }
        System.err.println("Error: JTable not found in the provided wrapper panel!");
    }

    public static JTable getTableFromPanel(JPanel wrapperPanel) {
        for (Component comp : wrapperPanel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                Component view = scrollPane.getViewport().getView();
                if (view instanceof JTable) {
                    return (JTable) view;
                }
            }
        }
        return null;
    }
}