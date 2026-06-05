package com.storesystem.view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import com.storesystem.config.SetupUI;
import com.storesystem.util.TableUtil;

public class OrderPanel extends JPanel{

    JPanel productTable;
    JPanel cartTable;
    JButton addCustomerButton;
    JButton payButton;
    JButton deleteButton;
    JButton refreshButton;
    JButton addToCartButton;
    JButton editButton;
    JPanel buttonPanel1;
    JPanel buttonPanel2;
    JComboBox<String> comboBox1;
    JTextField searchField1;
    JComboBox<String> comboBox2;
    JTextField searchField2;
    JPanel searchPanel1;
    JPanel searchPanel2;
    JPanel panel1;
    JPanel panel2;
    JSplitPane splitPane;
    JPanel lablePanel;
    
    public OrderPanel() {
        initComponents();
        createButtonPanel();
        creatSearchPanel();
        setupLayout();
    }

    private void initComponents(){
    productTable = TableUtil.createTable(new Object[][] {}, 
            new String[] {"کد", "نام کالا", "قیمت", "موجودی", "دسته"},
            new int[] {60, 180, 100, 60, 100} );
    cartTable = TableUtil.createTable(new Object[][] {}, 
            new String[] {"کد", "کالا", "قیمت واحد", "تعداد", "جمع"},
            new int[] {60, 170, 100, 50, 120} );
    addCustomerButton = SetupUI.createButton(new Color(65, 115, 242), "مشتری جدید 👤");
    payButton = SetupUI.createButton(new Color(46, 204, 113), "تسویه 💳");
    deleteButton = SetupUI.createButton(new Color(248, 78, 91), "حذف قلم 🗑️");
    refreshButton = SetupUI.createButton(new Color(23, 162, 184), "بروزرسانی 🔄");
    addToCartButton = SetupUI.createButton(new Color(65, 115, 242), "افزودن به سبد +");
    addToCartButton.setHorizontalAlignment((int) CENTER_ALIGNMENT);
    editButton = SetupUI.createButton(new Color(251, 179, 22), "ویرایش تعداد ✏️");
    buttonPanel1 = new JPanel();
    buttonPanel2 = new JPanel();
    comboBox1 = new JComboBox<String>(new String[] {});
    comboBox2 = new JComboBox<String>(new String[] {});
    searchField1 = new JTextField();
    searchField2 = new JTextField();
    searchPanel1 = new JPanel();
    searchPanel2 = new JPanel();
    panel1 = new JPanel();
    panel2 = new JPanel();
    lablePanel = new JPanel();
    }

    private void createButtonPanel(){
        buttonPanel1.setBackground(Color.WHITE);
        buttonPanel1.setLayout(new GridLayout(2, 5, 10,-25));
        buttonPanel1.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttonPanel1.add(payButton);
        buttonPanel1.add(editButton);
        buttonPanel1.add(deleteButton);
        buttonPanel1.add(refreshButton);
        JPanel[] p = new JPanel[5]; 
        for (int i = 0; i < 5; i++) {
            p[i] = new JPanel();
            p[i].setBackground(Color.WHITE);
            p[i].setPreferredSize(new Dimension(15,10));
            buttonPanel1.add(p[i]);
        }

        buttonPanel2.setBackground(Color.WHITE);
        buttonPanel2.setLayout(new GridLayout(2, 1, 5, -25));
        buttonPanel2.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        buttonPanel2.add(addToCartButton);

        addActionListeners();
    }

    private void creatSearchPanel(){

        searchPanel1.setBackground(Color.WHITE);
        searchPanel1.setLayout(new GridLayout(1, 5, 10,-25));
        searchPanel1.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        searchPanel1.add(addCustomerButton);
        searchPanel1.add(searchField1);
        searchPanel1.add(comboBox1);

        searchPanel2.setBackground(Color.WHITE);
        searchPanel2.setLayout(new GridLayout(1, 3, 10,-25));
        searchPanel2.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        searchPanel2.add(comboBox2);
        searchPanel2.add(searchField2);
    }

    private void setupLayout(){
        panel1.setBackground(Color.WHITE);
        panel1.setLayout(new BorderLayout());
        panel1.add(productTable,BorderLayout.CENTER);
        panel1.add(buttonPanel2,BorderLayout.SOUTH);
        panel1.add(searchPanel2,BorderLayout.NORTH);
        panel1.setMinimumSize(new Dimension(100, 0));

        panel2.setBackground(Color.WHITE);
        panel2.setLayout(new BorderLayout());
        panel2.add(cartTable,BorderLayout.CENTER);
        panel2.add(buttonPanel1,BorderLayout.SOUTH);
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.WHITE);
        emptyPanel.setPreferredSize(new Dimension(0,21));
        panel2.add(emptyPanel,BorderLayout.NORTH);
        panel2.setMinimumSize(new Dimension(100, 0));
        

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel1, panel2);
        splitPane.setDividerLocation(0.5); 
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true); 
        javax.swing.SwingUtilities.invokeLater(() -> {
            splitPane.setDividerLocation(0.5);
        });

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(searchPanel1,BorderLayout.NORTH);
        add(lablePanel, BorderLayout.SOUTH);

    }

    private void addActionListeners() {

    }
}
