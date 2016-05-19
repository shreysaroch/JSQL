package com.nit.jsql;

import javax.swing.*;
import com.nit.jsql.dbconnect.DB;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class Settings extends JFrame implements ActionListener
{
    JLabel ldb, luser, lpassword, lip, lport;
    JTextField tdb, tuser, tpassword, tip, tport;
    JButton btnSet, btnClear;
    JFrame parent;
    public Settings(JFrame parent)
    {
        this.parent = parent;
        setTitle("Setting : JSQL");
        setLocation(200, 100);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ldb = new JLabel("Database Name : ");
        tdb = new JTextField(DB.DATABASE);
        luser = new JLabel("Username : ");
        tuser = new JTextField(DB.USER);
        lpassword = new JLabel("Password");
        tpassword = new JTextField(DB.PASSWORD);
        lip = new JLabel("IP Address : ");
        tip = new JTextField(DB.IP);
        lport = new JLabel("Port No. :");
        tport = new JTextField(DB.PORT);
        btnSet = new JButton("Save Settings");
        btnClear = new JButton("Clear Old Settings");
        setLayout(new GridLayout(6, 2));
        add(lip);
        add(tip);
        add(lport);
        add(tport);
        add(ldb);
        add(tdb);
        add(luser);
        add(tuser);
        add(lpassword);
        add(tpassword);
        add(btnSet);
        add(btnClear);
        btnSet.addActionListener(this);
        btnClear.addActionListener(this);
        setVisible(true);
        pack();
    }
    public void actionPerformed(ActionEvent ae)
    {
        File f1 = new File("DPPROP.JSQLDB");
        if(ae.getSource()==btnClear)
            f1.delete();
        else{
            try{
                Vector v1 = new Vector(1,1);
                v1.add(tdb.getText());
                v1.add(tuser.getText());
                v1.add(tpassword.getText());
                v1.add(tip.getText());
                v1.add(tport.getText());
                FileOutputStream fos = new FileOutputStream(f1, false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(v1);
                oos.close();
                fos.close();
                parent.dispose();
                dispose();
                new JSql();
            }catch(Exception ee){}
        }
    }
}
