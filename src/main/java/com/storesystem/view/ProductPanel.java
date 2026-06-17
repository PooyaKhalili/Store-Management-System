package com.storesystem.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.storesystem.util.TableUtil;

import com.storesystem.config.SetupUI;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import com.storesystem.controller.ProductController;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.io.File;


public class ProductPanel extends JPanel {

    public JPanel productTable;

    private JButton addButton;
    private JButton deleteButton;
    JButton refreshButton;
    private JButton importCSV;
    private JButton exportCSV;
    private JButton editButton;
    private JButton lowStockButton;
    private JPanel buttonPanel;

    private JLabel categoryLabel;
    private JLabel searchLabel;
    public JComboBox<String> comboBox;
    private JTextField searchField;
    private JPanel searchPanel;

    private final ProductController controller;


    public ProductPanel() {
        controller = new ProductController(this);
        initComponents();
        createButtonPanel();
        creatSearchPanel();
        setupLayout();
    }

    private void initComponents() {
        productTable = new JPanel();
        productTable=TableUtil.createTable(controller.getAllProducts(),
                new String[] {"کد کالا", "نام کالا", "قیمت", "موجودی", "دسته‌بندی"},
                new int[] {90, 350, 130, 90, 180});
        buttonPanel = new JPanel();
        searchPanel = new JPanel();
        searchField = new JTextField();
        searchField.setFont(new Font("Vazir", Font.BOLD, 14));
        categoryLabel = new JLabel("دسته‌بندی:");
        categoryLabel.setFont(new Font("Vazir", Font.BOLD, 14));
        searchLabel = new JLabel("جست‌وجو:");
        searchLabel.setFont(new Font("Vazir", Font.BOLD, 14));
        comboBox = new JComboBox<>();
        controller.loadCategoriesIntoComboBox();
        comboBox.setFont(new Font("Vazir", Font.BOLD, 14));


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
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 5, 0, 15);
        gbc.fill = GridBagConstraints.NONE;

        comboBox.setPreferredSize(new Dimension(120, 30));
        comboBox.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Vazir", Font.BOLD, 14));
        searchField.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        searchPanel.add(categoryLabel, gbc);
        searchPanel.add(comboBox, gbc);
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
        Font vazirFont = new Font("Vazir", Font.PLAIN, 14);

        addButton.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField priceField = new JTextField();
            
            JComboBox<String> categoryCombo = new JComboBox<>();
            for (int i = 1; i < comboBox.getItemCount(); i++) {
                categoryCombo.addItem(comboBox.getItemAt(i));
            }
            if (categoryCombo.getItemCount() == 0) {
                categoryCombo.addItem("نامشخص");
            }

            JSpinner StokSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
            StokSpinner.setFont(vazirFont);
            ((JSpinner.DefaultEditor) StokSpinner.getEditor()).getTextField().setFont(vazirFont);
            ((JSpinner.DefaultEditor) StokSpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.RIGHT);

            nameField.setFont(vazirFont); priceField.setFont(vazirFont); 
            StokSpinner.setFont(vazirFont); categoryCombo.setFont(vazirFont);
            
            nameField.setHorizontalAlignment(JTextField.RIGHT);
            priceField.setHorizontalAlignment(JTextField.RIGHT);


            JLabel nameLabel = new JLabel("نام کالا:");
            JLabel priceLabel = new JLabel("قیمت (ریال):");
            JLabel stockLabel = new JLabel("موجودی:");
            JLabel catLabel = new JLabel("دسته‌بندی:");
            
            nameLabel.setFont(vazirFont); priceLabel.setFont(vazirFont); 
            stockLabel.setFont(vazirFont); catLabel.setFont(vazirFont);
            categoryCombo.setFont(vazirFont);
            
            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            stockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            catLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(4, 2, 10, 10)); 
            inputPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            inputPanel.add(nameLabel); inputPanel.add(nameField);
            inputPanel.add(priceLabel); inputPanel.add(priceField);
            inputPanel.add(stockLabel); inputPanel.add(StokSpinner);
            inputPanel.add(catLabel); inputPanel.add(categoryCombo);

            UIManager.put("Button.font", vazirFont);
            Object[] customButtons = {"ثبت کالا", "انصراف"};

            JOptionPane optionPane = new JOptionPane(
                    inputPanel, 
                    JOptionPane.PLAIN_MESSAGE, 
                    JOptionPane.OK_CANCEL_OPTION, 
                    null, 
                    customButtons, 
                    customButtons[0]
            );
            
            optionPane.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            StokSpinner.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            JDialog dialog = optionPane.createDialog(this, "افزودن کالای جدید 📦");
            dialog.setVisible(true);

            Object result = optionPane.getValue();
            if (result != null && result.equals("ثبت کالا")) {
                String name = nameField.getText().trim();
                String price = priceField.getText().trim();
                String stock = StokSpinner.getValue().toString().trim();
                String category = categoryCombo.getSelectedItem().toString();
                String message = "";

                if (controller != null) {
                    message = controller.addProduct(name, price, stock, category); 
                }

                UIManager.put("OptionPane.messageFont", vazirFont);
                if (!message.equals("کالا با موفقیت اضافه شد")) {
                    JOptionPane.showMessageDialog(this, message, "خطا", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, message, "عملیات موفق", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        editButton.addActionListener(e -> {
            JTable tableInstance = TableUtil.getTableFromPanel(productTable);
            if (tableInstance == null) return;

            int selectedRow = tableInstance.getSelectedRow();
            if (selectedRow == -1) {
                UIManager.put("OptionPane.messageFont", vazirFont);
                JOptionPane.showMessageDialog(this, 
                        "لطفاً ابتدا یک کالا را از جدول انتخاب کنید!", 
                        "خطا", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String codeStr = tableInstance.getValueAt(selectedRow, 0).toString();
            String oldName = tableInstance.getValueAt(selectedRow, 1).toString();
            String oldPrice = tableInstance.getValueAt(selectedRow, 2).toString();
            String oldStock = tableInstance.getValueAt(selectedRow, 3).toString();
            String oldCategory = tableInstance.getValueAt(selectedRow, 4).toString();

            JTextField nameField = new JTextField(oldName);
            JTextField priceField = new JTextField(oldPrice);

            JSpinner StokSpinner = new JSpinner(new SpinnerNumberModel(Integer.parseInt(oldStock), 0, 100000, 1));
            StokSpinner.setFont(vazirFont);
            ((JSpinner.DefaultEditor) StokSpinner.getEditor()).getTextField().setFont(vazirFont);
            ((JSpinner.DefaultEditor) StokSpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.RIGHT);
            
            JComboBox<String> categoryCombo = new JComboBox<>();
            for (int i = 1; i < comboBox.getItemCount(); i++) {
                categoryCombo.addItem(comboBox.getItemAt(i));
            }
            if (categoryCombo.getItemCount() == 0) categoryCombo.addItem(oldCategory);
            categoryCombo.setSelectedItem(oldCategory);

            nameField.setFont(vazirFont); priceField.setFont(vazirFont); 
            StokSpinner.setFont(vazirFont); categoryCombo.setFont(vazirFont);
            
            nameField.setHorizontalAlignment(JTextField.RIGHT);
            priceField.setHorizontalAlignment(JTextField.RIGHT);
            
            JLabel nameLabel = new JLabel("نام جدید کالا:");
            JLabel priceLabel = new JLabel("قیمت جدید (ریال):");
            JLabel stockLabel = new JLabel("موجودی جدید:");
            JLabel catLabel = new JLabel("دسته‌بندی جدید:");

            nameLabel.setFont(vazirFont); priceLabel.setFont(vazirFont); 
            stockLabel.setFont(vazirFont); catLabel.setFont(vazirFont);
            
            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            stockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            catLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(4, 2, 10, 10)); 
            inputPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

            inputPanel.add(nameLabel); inputPanel.add(nameField);
            inputPanel.add(priceLabel); inputPanel.add(priceField);
            inputPanel.add(stockLabel); inputPanel.add(StokSpinner);
            inputPanel.add(catLabel); inputPanel.add(categoryCombo);

            UIManager.put("Button.font", vazirFont);
            Object[] customButtons = {"ویرایش کالا", "انصراف"};

            JOptionPane optionPane = new JOptionPane(
                    inputPanel, 
                    JOptionPane.PLAIN_MESSAGE, 
                    JOptionPane.OK_CANCEL_OPTION, 
                    null, 
                    customButtons, 
                    customButtons[0]
            );
            
            optionPane.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            StokSpinner.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            JDialog dialog = optionPane.createDialog(this, "ویرایش کالا ✏️");
            dialog.setVisible(true);

            Object result = optionPane.getValue();
            if (result != null && result.equals("ویرایش کالا")) {
                String name = nameField.getText().trim();
                String price = priceField.getText().trim();
                String stock = StokSpinner.getValue().toString().trim();
                String category = categoryCombo.getSelectedItem().toString();
                String message = "";
                
                if (controller != null) {
                    message = controller.editProduct(Long.parseLong(codeStr), name, price, stock, category); 
                }

                UIManager.put("OptionPane.messageFont", vazirFont);
                if (!message.equals("کالا با موفقیت ویرایش شد")) {
                    JOptionPane.showMessageDialog(this, message, "خطا", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, message, "عملیات موفق", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            JTable tableInstance = TableUtil.getTableFromPanel(productTable);
            if (tableInstance != null) {
                int selectedRow = tableInstance.getSelectedRow();

                if (selectedRow == -1) {
                    UIManager.put("OptionPane.messageFont", vazirFont);
                    JOptionPane.showMessageDialog(this, 
                            "لطفاً ابتدا یک کالا را از جدول انتخاب کنید!", 
                            "خطا", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String codeStr = tableInstance.getValueAt(selectedRow, 0).toString();
                String name = tableInstance.getValueAt(selectedRow, 1).toString();
                
                UIManager.put("OptionPane.messageFont", vazirFont);
                UIManager.put("Button.font", vazirFont);
                if (JOptionPane.showConfirmDialog(this, "آیا از حذف کالای «" + name + "» اطمینان دارید؟", "حذف کالا", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (controller != null) {
                        controller.deleteProduct(Long.parseLong(codeStr));
                    }
                }
            }   
        });

        refreshButton.addActionListener(e -> {
            if (controller != null) {
                searchField.setText("");
                controller.loadCategoriesIntoComboBox();
                TableUtil.refreshTable(productTable, controller.getAllProducts());
            }
        });

        lowStockButton.addActionListener(e -> {
            if (controller != null) {
                controller.showLowStockProducts();
            }
        });

        searchField.addActionListener(e -> {
            if (controller != null) {
                String text = searchField.getText().trim();
                String category = comboBox.getSelectedItem().toString();
                controller.searchProduct(text, category);
            }
        });

        comboBox.addActionListener(e -> {
            if (controller != null) {
                String text = searchField.getText().trim();
                String category = comboBox.getSelectedItem().toString();
                controller.searchProduct(text, category);
            }
        });

        importCSV.addActionListener(e -> {
            UIManager.put("OptionPane.messageFont", vazirFont);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("انتخاب فایل CSV برای ورود");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
            int result = fileChooser.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChooser.getSelectedFile();
                String response = controller.importFromCsv(selectedFile.getAbsolutePath());
                if (response.contains("خطا")) {
                    JOptionPane.showMessageDialog(this, response, "خطا", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, response, "موفق", JOptionPane.INFORMATION_MESSAGE);
                    controller.loadCategoriesIntoComboBox();
                }
            }
        });

        exportCSV.addActionListener(e -> {
            UIManager.put("OptionPane.messageFont", vazirFont);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("ذخیره فایل CSV");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
            fileChooser.setSelectedFile(new File("productsExport.csv"));

            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();

                if (!filePath.toLowerCase().endsWith(".csv")) {
                    filePath += ".csv";
                }

                String response = controller.exportToCsv(filePath);

                UIManager.put("OptionPane.messageFont", vazirFont);
                if (response.contains("خطا")) {
                    JOptionPane.showMessageDialog(this, response, "خطا", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, response, "موفق", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }


}
