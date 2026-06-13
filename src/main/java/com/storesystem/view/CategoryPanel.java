package com.storesystem.view;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import com.storesystem.config.SetupUI;
import com.storesystem.controller.CategoryController;
import com.storesystem.util.TableUtil;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;


public class CategoryPanel extends JPanel {

    public JPanel table;
    private JPanel buttonPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;

    private final CategoryController controller;

    public CategoryPanel() {
        controller = new CategoryController(this);
        initComponents();
        createButtonPanel();
        setupLayout();
    }

    private void initComponents() {
        table = TableUtil.createTable(controller.getAllCategories() , 
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
        // ==========================================
        // ۱. دکمه افزودن دسته‌بندی
        // ==========================================
        addButton.addActionListener(e -> {
            Font vazirFont = new Font("Vazir", Font.PLAIN, 14);

            JTextField nameField = new JTextField();
            nameField.setFont(vazirFont);
            nameField.setHorizontalAlignment(JTextField.RIGHT);

            JLabel nameLabel = new JLabel("نام دسته‌بندی:");
            nameLabel.setFont(vazirFont);
            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(1, 2, 10, 10)); 
            inputPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            inputPanel.add(nameLabel);
            inputPanel.add(nameField);

            UIManager.put("Button.font", vazirFont);
            Object[] customButtons = {"ثبت دسته‌بندی", "انصراف"};

            JOptionPane optionPane = new JOptionPane(
                    inputPanel, 
                    JOptionPane.PLAIN_MESSAGE, 
                    JOptionPane.OK_CANCEL_OPTION, 
                    null, 
                    customButtons, 
                    customButtons[0]
            );
            
            optionPane.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            JDialog dialog = optionPane.createDialog(this, "افزودن دسته‌بندی جدید 📁");
            dialog.setVisible(true);

            Object result = optionPane.getValue();
            if (result != null && result.equals("ثبت دسته‌بندی")) {
                String name = nameField.getText().trim();
                String message = "";

                if (controller != null) {
                    message = controller.addCategory(name); 
                }

                if (!message.equals("دسته‌بندی با موفقیت اضافه شد")) {
                    UIManager.put("OptionPane.messageFont", vazirFont);
                    JOptionPane.showMessageDialog(this, 
                            message, 
                            "خطا", 
                            JOptionPane.ERROR_MESSAGE);
                    return; 
                } else {
                    UIManager.put("OptionPane.messageFont", vazirFont);
                    JOptionPane.showMessageDialog(this, 
                            message, 
                            "", 
                            JOptionPane.INFORMATION_MESSAGE);
                    return; 
                }
            }
        });

        // ==========================================
        // ۲. دکمه ویرایش دسته‌بندی
        // ==========================================
        editButton.addActionListener(e -> {
            JTable tableInstance = TableUtil.getTableFromPanel(table);
            if (tableInstance == null) return;

            int selectedRow = tableInstance.getSelectedRow();
            if (selectedRow == -1) {
                UIManager.put("OptionPane.messageFont", new Font("Vazir", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, 
                        "لطفاً ابتدا یک دسته‌بندی را از جدول انتخاب کنید!", 
                        "خطا", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String categoryId = tableInstance.getValueAt(selectedRow, 0).toString();

            Font vazirFont = new Font("Vazir", Font.PLAIN, 14);

            JTextField nameField = new JTextField();
            nameField.setFont(vazirFont);
            nameField.setHorizontalAlignment(JTextField.RIGHT);

            JLabel nameLabel = new JLabel("نام جدید دسته‌بندی:");
            nameLabel.setFont(vazirFont);
            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(1, 2, 10, 10)); 
            inputPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            inputPanel.add(nameLabel); 
            inputPanel.add(nameField);

            UIManager.put("Button.font", vazirFont);
            Object[] customButtons = {"ویرایش دسته‌بندی", "انصراف"};

            JOptionPane optionPane = new JOptionPane(
                    inputPanel, 
                    JOptionPane.PLAIN_MESSAGE, 
                    JOptionPane.OK_CANCEL_OPTION, 
                    null, 
                    customButtons, 
                    customButtons[0]
            );
            
            optionPane.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            JDialog dialog = optionPane.createDialog(this, "ویرایش دسته‌بندی ✏️");
            dialog.setVisible(true);

            Object result = optionPane.getValue();
            if (result != null && result.equals("ویرایش دسته‌بندی")) {
                String name = nameField.getText().trim();
                String message = "";
                
                if (controller != null) {
                    message = controller.editCategory(Integer.parseInt(categoryId), name); 
                }

                UIManager.put("OptionPane.messageFont", vazirFont);
                if (!message.equals("دسته‌بندی با موفقیت ویرایش شد")) {
                    JOptionPane.showMessageDialog(this, message, "خطا", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, message, "عملیات موفق", JOptionPane.INFORMATION_MESSAGE);
                    TableUtil.refreshTable(table, controller.getAllCategories());
                }
            }
        });

        // ==========================================
        // ۳. دکمه حذف دسته‌بندی
        // ==========================================
        deleteButton.addActionListener(e -> {
            JTable tableInstance = TableUtil.getTableFromPanel(table);
            if (tableInstance != null) {
                int selectedRow = tableInstance.getSelectedRow();

                if (selectedRow == -1) {
                    UIManager.put("OptionPane.messageFont", new Font("Vazir", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(this, 
                            "لطفاً ابتدا یک دسته‌بندی را از جدول انتخاب کنید!", 
                            "خطا", 
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String categoryId = tableInstance.getValueAt(selectedRow, 0).toString();
                
                if (JOptionPane.showConfirmDialog(this, "آیا از حذف دسته‌بندی اطمینان دارید؟", "حذف دسته‌بندی", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    controller.deleteCategory(Integer.parseInt(categoryId));
                }
            }   
        });

        // ==========================================
        // ۴. دکمه بروزرسانی
        // ==========================================
        refreshButton.addActionListener(e -> {
            TableUtil.refreshTable(table, controller.getAllCategories());
        });
    }
    

}
