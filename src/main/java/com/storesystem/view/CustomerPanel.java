package com.storesystem.view;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.storesystem.util.TableUtil;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.storesystem.config.SetupUI;
import com.storesystem.controller.CustomerController;

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

    public JPanel CustomerTable;

    private JButton addButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton editButton;
    private JPanel buttonPanel;

    private JLabel searchLabel;
    private JTextField searchField;
    private JPanel searchPanel;

    private final CustomerController controller;


    public CustomerPanel(){
        controller = new CustomerController(this);
        initComponents();
        createButtonPanel();
        creatSearchPanel();
        setupLayout();
    }

    private void initComponents() {
        CustomerTable = new JPanel();
        CustomerTable = TableUtil.createTable(controller.getAllCustomers(), 
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
            Font vazirFont = new Font("Vazir", Font.PLAIN, 14);

            JTextField firstNameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField phoneField = new JTextField();

            firstNameField.setFont(vazirFont);
            lastNameField.setFont(vazirFont);
            phoneField.setFont(vazirFont);

            firstNameField.setHorizontalAlignment(JTextField.RIGHT);
            lastNameField.setHorizontalAlignment(JTextField.RIGHT);
            phoneField.setHorizontalAlignment(JTextField.RIGHT);

            JLabel firstNameLabel = new JLabel("نام:");
            JLabel lastNameLabel = new JLabel("نام خانوادگی:");
            JLabel phoneLabel = new JLabel("شماره تلفن:");
            
            firstNameLabel.setFont(vazirFont);
            lastNameLabel.setFont(vazirFont);
            phoneLabel.setFont(vazirFont);

            firstNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            lastNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            phoneLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(3, 2, 10, 10)); 
            inputPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            inputPanel.add(firstNameLabel);
            inputPanel.add(firstNameField);
            inputPanel.add(lastNameLabel);
            inputPanel.add(lastNameField);
            inputPanel.add(phoneLabel);
            inputPanel.add(phoneField);

            UIManager.put("Button.font", vazirFont);
            Object[] customButtons = {"ثبت مشتری", "انصراف"};

            JOptionPane optionPane = new JOptionPane(
                    inputPanel, 
                    JOptionPane.PLAIN_MESSAGE, 
                    JOptionPane.OK_CANCEL_OPTION, 
                    null, 
                    customButtons, 
                    customButtons[0]
            );
            
            optionPane.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            JDialog dialog = optionPane.createDialog(this, "افزودن مشتری جدید 👤");
            dialog.setVisible(true);

            Object result = optionPane.getValue();
            if (result != null && result.equals("ثبت مشتری")) {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String phone = phoneField.getText().trim();
                String message ="";

                if (controller != null) {
                    message =controller.addCustomer(firstName, lastName, phone); 
                }

                if (message != "مشتری با موفقیت اضافه شد") {
                    UIManager.put("OptionPane.messageFont", vazirFont);
                    JOptionPane.showMessageDialog(this, 
                            message, 
                            "خطا", 
                            JOptionPane.ERROR_MESSAGE);
                    return; 
                }else{
                    UIManager.put("OptionPane.messageFont", vazirFont);
                    JOptionPane.showMessageDialog(this, 
                            message, 
                            "", 
                            JOptionPane.INFORMATION_MESSAGE);
                    return; 

                }

            }
            
        });
editButton.addActionListener(e -> {
            JTable table = TableUtil.getTableFromPanel(CustomerTable);
            if (table == null) return;

            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                UIManager.put("OptionPane.messageFont", new Font("Vazir", Font.PLAIN, 14));
                JOptionPane.showMessageDialog(this, 
                        "لطفاً ابتدا یک مشتری را از جدول انتخاب کنید!", 
                        "خطا", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String customerId = table.getValueAt(selectedRow, 0).toString();

            Font vazirFont = new Font("Vazir", Font.PLAIN, 14);

            JTextField firstNameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField phoneField = new JTextField();

            firstNameField.setFont(vazirFont); lastNameField.setFont(vazirFont); phoneField.setFont(vazirFont);
            firstNameField.setHorizontalAlignment(JTextField.RIGHT);
            lastNameField.setHorizontalAlignment(JTextField.RIGHT);
            phoneField.setHorizontalAlignment(JTextField.RIGHT);

            JLabel firstNameLabel = new JLabel("نام جدید:");
            JLabel lastNameLabel = new JLabel("نام خانوادگی جدید:");
            JLabel phoneLabel = new JLabel("شماره تلفن جدید:");
            
            firstNameLabel.setFont(vazirFont); lastNameLabel.setFont(vazirFont); phoneLabel.setFont(vazirFont);
            firstNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            lastNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            phoneLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(3, 2, 10, 10)); 
            inputPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            inputPanel.add(firstNameLabel); inputPanel.add(firstNameField);
            inputPanel.add(lastNameLabel);  inputPanel.add(lastNameField);
            inputPanel.add(phoneLabel);     inputPanel.add(phoneField);

            UIManager.put("Button.font", vazirFont);
            Object[] customButtons = {"ویرایش مشتری", "انصراف"};

            JOptionPane optionPane = new JOptionPane(
                    inputPanel, 
                    JOptionPane.PLAIN_MESSAGE, 
                    JOptionPane.OK_CANCEL_OPTION, 
                    null, 
                    customButtons, 
                    customButtons[0]
            );
            
            optionPane.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            JDialog dialog = optionPane.createDialog(this, "ویرایش مشتری 👤");
            dialog.setVisible(true);

            Object result = optionPane.getValue();
            if (result != null && result.equals("ویرایش مشتری")) {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String phone = phoneField.getText().trim();
                String message = "";
                
                if (controller != null) {
                    message = controller.editCustomer(Integer.parseInt(customerId), firstName, lastName, phone); 
                }

                UIManager.put("OptionPane.messageFont", vazirFont);
                if (!message.equals("مشتری با موفقیت ویرایش شد")) {
                    JOptionPane.showMessageDialog(this, message, "خطا", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, message, "عملیات موفق", JOptionPane.INFORMATION_MESSAGE);
                    TableUtil.refreshTable(CustomerTable, controller.getAllCustomers());
                }
            }
        });
        deleteButton.addActionListener(e -> {
            JTable table = TableUtil.getTableFromPanel(CustomerTable);
            if (table != null) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow == -1) {
                    UIManager.put("OptionPane.messageFont", new Font("Vazir", Font.PLAIN, 14));
                    JOptionPane.showMessageDialog(this, 
                            "لطفاً ابتدا یک مشتری را از جدول انتخاب کنید!", 
                            "خطا", 
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String customerId = table.getValueAt(selectedRow, 0).toString();
                if (JOptionPane.showConfirmDialog(this, "آیا از حذف مشتری اطمینان دارید؟", "حذف مشتری", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                controller.deleteCustomer(Integer.parseInt(customerId));
                }
            }   
        });
        refreshButton.addActionListener(e -> {
            TableUtil.refreshTable(CustomerTable, controller.getAllCustomers());
        });
        searchField.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            controller.searchCustomer(searchText);
        });

    }

}
