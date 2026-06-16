package com.storesystem.util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class JalaliDatePickerDialog extends JDialog {

    private JComboBox<String> monthCombo;
    private JComboBox<Integer> yearCombo;
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private JPanel daysPanel;
    private JLabel livePreviewLabel;
    private List<JButton> dayButtons; 
    
    private int selectedDay = 1; 
    private String selectedDate = null; 

    public JalaliDatePickerDialog(Window parent, String title) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        setResizable(false);
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        Font vazirFont = new Font("Vazir", Font.PLAIN, 12);
        setLayout(new BorderLayout(10, 10));
        applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        dayButtons = new ArrayList<>();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
        String[] months = {"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(vazirFont);
        
        Integer[] years = new Integer[61];
        for (int i = 0; i <= 60; i++) {
            years[i] = 1390 + i;
        }
        yearCombo = new JComboBox<>(years);
        yearCombo.setFont(vazirFont);

        topPanel.add(new JLabel("ماه:"));
        topPanel.add(monthCombo);
        topPanel.add(new JLabel("سال:"));
        topPanel.add(yearCombo);

        daysPanel = new JPanel(new GridLayout(0, 7, 2, 2));
        daysPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        daysPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] weekDays = {"ش", "ی", "د", "س", "چ", "پ", "ج"};
        for (String wd : weekDays) {
            JLabel lbl = new JLabel(wd, SwingConstants.CENTER);
            lbl.setFont(new Font("Vazir", Font.BOLD, 12));
            daysPanel.add(lbl);
        }

        for (int i = 1; i <= 31; i++) {
            JButton dayBtn = new JButton(String.valueOf(i));
            dayBtn.setFont(vazirFont);
            dayBtn.setMargin(new Insets(2, 2, 2, 2));
            
            final int currentDay = i;
            dayBtn.addActionListener(e -> {
                selectedDay = currentDay;
                refreshDayButtonsColor(); 
                updateLivePreview();
            });
            
            dayButtons.add(dayBtn);
            daysPanel.add(dayBtn);
        }

        JPanel bottomWrapperPanel = new JPanel(new BorderLayout());
        
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        timePanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        hourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        hourSpinner.setFont(vazirFont);
        minuteSpinner.setFont(vazirFont);

        timePanel.add(new JLabel("ساعت"));
        timePanel.add(hourSpinner);
        timePanel.add(new JLabel("دقیقه"));
        timePanel.add(minuteSpinner);

        JPanel actionPanel = new JPanel(new BorderLayout(10, 10));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        actionPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        livePreviewLabel = new JLabel("انتخاب فعلی: --", SwingConstants.RIGHT);
        livePreviewLabel.setFont(new Font("Vazir", Font.BOLD, 12));
        livePreviewLabel.setForeground(new Color(0, 102, 204));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnConfirm = new JButton("تایید");
        JButton btnCancel = new JButton("انصراف");
        btnConfirm.setFont(vazirFont);
        btnCancel.setFont(vazirFont);

        buttonsPanel.add(btnConfirm);
        buttonsPanel.add(btnCancel);

        actionPanel.add(livePreviewLabel, BorderLayout.CENTER);
        actionPanel.add(buttonsPanel, BorderLayout.WEST);

        bottomWrapperPanel.add(timePanel, BorderLayout.NORTH);
        bottomWrapperPanel.add(actionPanel, BorderLayout.SOUTH);

        setInitialTimeToNow();

        monthCombo.addActionListener(e -> updateLivePreview());
        yearCombo.addActionListener(e -> updateLivePreview());
        hourSpinner.addChangeListener(e -> updateLivePreview());
        minuteSpinner.addChangeListener(e -> updateLivePreview());

        btnConfirm.addActionListener(e -> {
            generateSelectedDateString();
            dispose();
        });
        btnCancel.addActionListener(e -> dispose());

        add(topPanel, BorderLayout.NORTH);
        add(daysPanel, BorderLayout.CENTER);
        add(bottomWrapperPanel, BorderLayout.SOUTH);
    }

    private void setInitialTimeToNow() {
        try {
            String now = JalaliDateUtil.getCurrentJalaliDateTime();
            
            int currYear = Integer.parseInt(now.substring(0, 4));
            int currMonth = Integer.parseInt(now.substring(5, 7));
            int currDay = Integer.parseInt(now.substring(8, 10));
            int currHour = Integer.parseInt(now.substring(11, 13));
            int currMinute = Integer.parseInt(now.substring(14, 16));

            yearCombo.setSelectedItem(currYear);
            monthCombo.setSelectedIndex(currMonth - 1);
            selectedDay = currDay;
            hourSpinner.setValue(currHour);
            minuteSpinner.setValue(currMinute);

            refreshDayButtonsColor();
            updateLivePreview();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshDayButtonsColor() {
        for (JButton btn : dayButtons) {
            if (Integer.parseInt(btn.getText()) == selectedDay) {
                btn.setBackground(new Color(135, 206, 250));
            } else {
                btn.setBackground(Color.WHITE);
            }
        }
    }

    private void updateLivePreview() {
        int year = (Integer) yearCombo.getSelectedItem();
        int monthIndex = monthCombo.getSelectedIndex() + 1;
        int h = (Integer) hourSpinner.getValue();
        int m = (Integer) minuteSpinner.getValue();

        String liveStr = String.format("%04d/%02d/%02d %02d:%02d", year, monthIndex, selectedDay, h, m);
        livePreviewLabel.setText("تاریخ انتخابی: " + liveStr);
    }

    private void generateSelectedDateString() {
        int year = (Integer) yearCombo.getSelectedItem();
        int monthIndex = monthCombo.getSelectedIndex() + 1;
        int h = (Integer) hourSpinner.getValue();
        int m = (Integer) minuteSpinner.getValue();
        selectedDate = String.format("%04d/%02d/%02d %02d:%02d", year, monthIndex, selectedDay, h, m);
    }

    public String getSelectedDate() {
        return selectedDate;
    }
}