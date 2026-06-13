package com.storesystem.view;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HeadPanel extends JPanel {

    JLabel label;

    public HeadPanel(){
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));
        setPreferredSize(new Dimension(100,60));
        applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        label=new JLabel("سیستم مدیریت فروشگاه");
        label.setFont(new Font("Vazir",Font.PLAIN , 30));
        setBackground(new Color(41, 128, 185));
        label.setForeground(Color.WHITE);
        add(label, BorderLayout.EAST);
    }

}
