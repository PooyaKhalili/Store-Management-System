package com.storesystem.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import com.storesystem.config.SetupUI;
import com.storesystem.util.TableUtil;

public class OrderPanel extends JPanel {

    private JPanel productTable;
    private JPanel cartTable;

    private JButton addCustomerButton;

    private JButton payButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton editButton;
    private JPanel buttonPanel1;

    private JButton addToCartButton;
    private JPanel buttonPanel2;

    private JComboBox<String> comboBox1;
    private JTextField searchField1;
    private JComboBox<String> comboBox2;
    private JTextField searchField2;
    private JLabel searchCustomerLabel;
    private JLabel selectCustomerLabel;
    private JLabel searchProductLabel;
    private JLabel categoryLabel;
    private JPanel searchPanel1;
    private JPanel searchPanel2;

    private JPanel panel1;
    private JPanel panel2;
    private JSplitPane splitPane;

    private JPanel lablePanel;
    private JLabel totalSumLabel;

    private int total=0;


    public OrderPanel() {
        initComponents();
        createButtonPanel();
        creatSearchPanel();
        setupLayout();
    }

    private void initComponents() {
        cartTable = TableUtil.createTable(new Object[][] {}, 
                new String[] {"کد کالا", "نام کالا", "قیمت", "موجودی", "دسته"},
                new int[] {60, 180, 100, 60, 100} );
                
        productTable = TableUtil.createTable(new Object[][] {}, 
                new String[] {"کد کالا", "کالا", "قیمت واحد", "تعداد", "جمع"},
                new int[] {60, 170, 100, 50, 120} );

        
        addCustomerButton = SetupUI.createButton(new Color(16, 69, 122), "مشتری جدید 👤");
        
        payButton = SetupUI.createButton(new Color(46, 204, 113), "تسویه 💳");

        deleteButton = SetupUI.createButton(new Color(248, 78, 91), "حذف قلم 🗑️");

        editButton = SetupUI.createButton(new Color(251, 179, 22), "ویرایش تعداد ✏️");

        refreshButton = SetupUI.createButton(new Color(23, 162, 184), "بروزرسانی 🔄");

        addToCartButton = SetupUI.createButton(new Color(65, 115, 242), "افزودن به سبد +"); 
        addToCartButton.setHorizontalAlignment(SwingConstants.CENTER);

        buttonPanel1 = new JPanel();
        buttonPanel2 = new JPanel();
        comboBox1 = new JComboBox<>(new String[] {"همه"});
        comboBox2 = new JComboBox<>(new String[] {"همه"});
        searchField1 = new JTextField();
        searchField2 = new JTextField();
        searchPanel1 = new JPanel();
        searchPanel2 = new JPanel();
        panel1 = new JPanel();
        panel2 = new JPanel();
        lablePanel = new JPanel();

        searchCustomerLabel = new JLabel("جست‌وجو مشتری:");
        searchCustomerLabel.setFont(new Font("Vazir", Font.BOLD, 14));
        selectCustomerLabel = new JLabel("انتخاب مشتری:");
        selectCustomerLabel.setFont(new Font("Vazir", Font.BOLD, 14));
        searchProductLabel = new JLabel("جست‌وجو کد/نام:");
        searchProductLabel.setFont(new Font("Vazir", Font.BOLD, 14));
        categoryLabel = new JLabel("دسته:");
        categoryLabel.setFont(new Font("Vazir", Font.BOLD, 14));
        
        totalSumLabel = new JLabel("جمع کل: "+total+" ریال");
        totalSumLabel.setFont(new Font("Vazir", Font.BOLD, 14));
    }

    private void createButtonPanel() {
        buttonPanel1.setBackground(Color.WHITE);
        buttonPanel1.setLayout(new GridLayout(1, 5, 10, 0));
        buttonPanel1.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
        buttonPanel1.add(payButton);
        buttonPanel1.add(editButton);
        buttonPanel1.add(deleteButton);
        buttonPanel1.add(refreshButton);
        
        JPanel spacer = new JPanel();
        spacer.setBackground(Color.WHITE);
        buttonPanel1.add(spacer); 

        buttonPanel2.setBackground(Color.WHITE);
        buttonPanel2.setLayout(new GridLayout(1, 1, 5, 5));
        buttonPanel2.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        addToCartButton.setPreferredSize(new Dimension(0, 45));
        buttonPanel2.add(addToCartButton);

        addActionListeners();
    }

private void creatSearchPanel() {
        searchPanel1.setBackground(new Color(235, 238, 243));
        searchPanel1.setLayout(new GridBagLayout());
        searchPanel1.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridy = 0;
        gbc1.insets = new Insets(10, 5, 10, 5);
        gbc1.fill = GridBagConstraints.NONE;
        searchField1.setPreferredSize(new Dimension(150, 30));
        searchField1.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        searchField1.setFont(new Font("Vazir", Font.BOLD, 14));
        comboBox1.setPreferredSize(new Dimension(150, 30));
        comboBox1.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        addCustomerButton.setPreferredSize(new Dimension(110, 30));
 
        searchPanel1.add(addCustomerButton, gbc1);
        searchPanel1.add(searchCustomerLabel, gbc1);
        searchPanel1.add(searchField1, gbc1);
        searchPanel1.add(selectCustomerLabel, gbc1);
        searchPanel1.add(comboBox1, gbc1);
       
        gbc1.weightx = 1.0; 
        gbc1.fill = GridBagConstraints.HORIZONTAL;
        JPanel spacer1 = new JPanel();
        spacer1.setOpaque(false); 
        searchPanel1.add(spacer1, gbc1);

        searchPanel2.setBackground(Color.WHITE);
        searchPanel2.setLayout(new GridBagLayout());
        searchPanel2.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridy = 0;
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.fill = GridBagConstraints.NONE;
        
        searchField2.setPreferredSize(new Dimension(130, 30));
        searchField2.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        searchField2.setFont(new Font("Vazir", Font.BOLD, 14));
        comboBox2.setPreferredSize(new Dimension(100, 30));
        comboBox2.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        searchPanel2.add(searchProductLabel, gbc2);
        searchPanel2.add(searchField2, gbc2);
        searchPanel2.add(categoryLabel, gbc2);
        searchPanel2.add(comboBox2, gbc2);

        gbc2.weightx = 1.0;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        JPanel spacer2 = new JPanel();
        spacer2.setOpaque(false);
        searchPanel2.add(spacer2, gbc2);
    }

    private void setupLayout() {
        panel1.setBackground(Color.WHITE);
        panel1.setLayout(new BorderLayout());
        panel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel1.add(cartTable, BorderLayout.CENTER);
        panel1.add(buttonPanel2, BorderLayout.SOUTH);
        panel1.add(searchPanel2, BorderLayout.NORTH);
        panel1.setMinimumSize(new Dimension(200, 0));

        panel2.setBackground(Color.WHITE);
        panel2.setLayout(new BorderLayout());
        panel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel2.add(productTable, BorderLayout.CENTER);
        panel2.add(buttonPanel1, BorderLayout.SOUTH);
        
        JPanel emptySpace = new JPanel();
        emptySpace.setBackground(Color.WHITE);
        emptySpace.setPreferredSize(new Dimension(0, 30)); 
        panel2.add(emptySpace, BorderLayout.NORTH);
        panel2.setMinimumSize(new Dimension(200, 0));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel1, panel2);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(10);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true); 

        SwingUtilities.invokeLater(() -> splitPane.setDividerLocation(0.5));

        lablePanel.setBackground(new Color(235, 238, 243));
        lablePanel.setLayout(new BorderLayout());
        lablePanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        lablePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        lablePanel.add(totalSumLabel, BorderLayout.LINE_START);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(splitPane, BorderLayout.CENTER);
        add(searchPanel1, BorderLayout.NORTH);
        add(lablePanel, BorderLayout.SOUTH);
    }

    private void addActionListeners() {
        
    }
}