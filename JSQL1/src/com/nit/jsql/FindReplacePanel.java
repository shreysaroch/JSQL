package com.nit.jsql;

import java.awt.*;
import javax.swing.*;

class FindReplacePanel extends JPanel
{
    JPanel tPanel, lPanel, btnPanel;
    JButton findBtn, replaceBtn;
    JLabel lfWhat, lrWith;
    JTextField tfWhat, trWith;
    public FindReplacePanel()
    {
        lPanel = new JPanel();
        tPanel = new JPanel();
        btnPanel = new JPanel();
        findBtn = new JButton("FIND");
        replaceBtn = new JButton("REPLACE");
        lfWhat = new JLabel("Find What : ");
        lrWith = new JLabel("Replace With : ");
        tfWhat = new JTextField();
        trWith = new JTextField();
        setLayout(new BorderLayout(5, 5));
        add(tPanel);
        add(lPanel, BorderLayout.WEST);
        add(btnPanel, BorderLayout.SOUTH);
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(findBtn);
        btnPanel.add(replaceBtn);
        lPanel.setLayout(new GridLayout(2, 1));
        tPanel.setLayout(new GridLayout(2, 1));
        lPanel.add(lfWhat);
        lPanel.add(lrWith);
        tPanel.add(tfWhat);
        tPanel.add(trWith);
    }
}
