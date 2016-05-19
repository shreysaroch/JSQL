package com.nit.jsql;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import static javax.swing.JFrame.*;
import javax.swing.tree.DefaultMutableTreeNode;
import com.nit.jsql.dbconnect.DatabaseConnection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;


public class JSql extends JFrame implements ActionListener, MouseListener
{
    public static void main(String args[])
    {
        SwingUtilities.invokeLater(new Runnable()
        {
         public void run()
         {
           new JSql();
         }
        }
        );
    }
    JMenuBar mb;
    JMenu file,edit,tools,help;
    JMenuItem load,save,exit,find, new_con,abt;
    JToolBar tb;
    JButton bRun, bStop, bSave, bFind,mesg,rslt;
    DefaultMutableTreeNode db;
    JDesktopPane dp;
    JEditorPane ep;
    JScrollPane sp;
    FindReplacePanel findPanel;
    JPanel msg,res,lbl,pmrl,pall, cPanel;
    JTextArea Pmesg;
    JTable dataTable;
    JScrollPane dataPane;
    CardLayout cl;
    FileDialog fd;
    public JSql()
    {
       setTitle("Database Explorer") ;
       setBounds(150,50,900,650);
       try
        {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());     
           setIconImage(ImageIO.read(this.getClass().getResource("../../../img/appicon.jpg")));
        }catch (Exception e){}
        // MENU BAR Starts
        mb=new JMenuBar();
        load=new JMenuItem("Load");
        save=new JMenuItem("Save");
        exit=new JMenuItem("Exit");
        find=new JMenuItem("Find");
        new_con=new JMenuItem("New Connection");
        abt=new JMenuItem("About");
        file=new JMenu("File");
        file.add(load);
        file.add(save);
        file.add(exit);
        edit=new JMenu("Edit");
        edit.add(find);
        tools=new JMenu("Tools");
        tools.add(new_con);
       help=new JMenu("Help");
            help.add(abt);
       mb.add(file);
       mb.add(edit);
       mb.add(tools);
       mb.add(help);
       setJMenuBar(mb); // MENU BAR ENDS
                                                                         //Desktop pane
        dp = new JDesktopPane();
        dp.setLayout(new BorderLayout());
        setContentPane(dp);
                                                                         //Toolbar Starts
        bRun= new JButton(new ImageIcon(this.getClass().getResource("../../../images/start.jpg")));     
        bStop= new JButton(new ImageIcon(this.getClass().getResource("../../../images/stop.jpg")));
        bSave= new JButton(new ImageIcon(this.getClass().getResource("../../../images/save.jpg")));
        bFind= new JButton(new ImageIcon(this.getClass().getResource("../../../images/find.jpg")));
        bRun.setToolTipText("Start Query");
        bStop.setToolTipText("Stop Query");
        bSave.setToolTipText("Save");
        bFind.setToolTipText("Find");
        tb=new JToolBar("Formatting");
        tb.add(bRun);
        tb.add(bStop);
        tb.add(bSave);
        tb.add(bFind);
        tb.setVisible(true);
        add(tb, BorderLayout.NORTH); // end of toolbar
                                                                   //editor pane starts & scroll pane
        ep=new JEditorPane();
        cPanel = new JPanel();
        findPanel = new FindReplacePanel();
        cPanel.setLayout(new BorderLayout());
        sp = new JScrollPane(ep, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        cPanel.add(sp, BorderLayout.CENTER);
        cPanel.add(findPanel, BorderLayout.SOUTH);
        add(cPanel, BorderLayout.CENTER);
                                                                       //tree & scroll pane
        db = new DefaultMutableTreeNode("Databases");
        JTree t1= new JTree(db);
        JScrollPane sp=new JScrollPane(t1,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp.setPreferredSize(new Dimension (150,650));
        add(sp,BorderLayout.WEST);
                                                                       //Message and result pane
        msg=new JPanel();
        res=new JPanel();
        lbl=new JPanel();
        pmrl=new JPanel();
        pall=new JPanel();
        cl=new CardLayout();
        mesg=new JButton("Message");
        rslt=new JButton("Result");
        Pmesg=new JTextArea();
        msg.setLayout(new BorderLayout());
        msg.add(new JScrollPane(Pmesg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        Pmesg.setEditable(false);
        lbl.add(mesg);
        lbl.add(rslt);
        lbl.setBackground(Color.LIGHT_GRAY);
        pmrl.setLayout(cl);
        pmrl.add(msg,"msg");
        pmrl.add(res,"res");
        res.setLayout(new BorderLayout());
        pmrl.setBackground(Color.magenta);
        pall.setLayout(new BorderLayout());
        pall.add(lbl,BorderLayout.NORTH);
        pall.add(pmrl,BorderLayout.CENTER);
        pall.setPreferredSize(new Dimension(900,200));
        add(pall, BorderLayout.SOUTH);
                                                // adding action listeners
        load.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);
        find.addActionListener(this);
        new_con.addActionListener(this);
        abt.addActionListener(this);
        bRun.addActionListener(this);
        bStop.addActionListener(this);
        bSave.addActionListener(this);
        bFind.addActionListener(this);
        mesg.addActionListener(this);
        rslt.addActionListener(this);
        pall.addMouseListener(this);
        ep.addMouseListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        findPanel.setVisible(false);
        if(false==DatabaseConnection.setUpDatabase())
            JOptionPane.showMessageDialog(rootPane, "MySQL is not yet confuigured, go to settings and configure mysql.", "Connection", JOptionPane.ERROR_MESSAGE);
        loadDatabases();
        t1.addTreeSelectionListener(new TreeSelectionListener() {
           public void valueChanged(TreeSelectionEvent e) {
               String p = e.getPath().toString();
               String arr[] = p.split(",");
               if(arr.length==3)
               {
                   String tblnm = (arr[2].substring(1    , arr[2].length()-1));
                   String dbnm = arr[1].substring(1  , arr[1].length());
                   showTable(dbnm, tblnm);
                   //getTable(arr[2].substring(1    , arr[2].length()-1));
               }
           }
       });
        findPanel.findBtn.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               find(findPanel.tfWhat.getText());
           }
       });
        findPanel.replaceBtn.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               rplc(findPanel.tfWhat.getText(), findPanel.trWith.getText());
           }
       });
    }
    public void find(String text)
    {
        if(ep.getText().indexOf(text)==-1)
            JOptionPane.showMessageDialog(rootPane, "Text not found in the queries.", "Find", JOptionPane.INFORMATION_MESSAGE);
        else
        {
            int bpos = ep.getText().indexOf(text);
            int epos = bpos + text.length();
            ep.setSelectionStart(bpos);
            ep.setSelectionEnd(epos);
            ep.transferFocus();        
        }
    }
    public void rplc(String what, String with)
    {
        if(ep.getText().indexOf(what)==-1)
            JOptionPane.showMessageDialog(rootPane, "Text not found in the queries.", "Find", JOptionPane.INFORMATION_MESSAGE);
        else
            ep.setText(ep.getText().replace(what, with));
        
    }
    public void showTable(String dbnm, String table)
    {
       new Thread(
       new Runnable() {

           public void run() {
               try{
                            DatabaseConnection.changeDatabase(dbnm);
                            ResultSet rs = DatabaseConnection.executeQuery("Select * from "  + table);
                            ResultSetMetaData rs1 = rs.getMetaData();
                            Vector hds = new Vector(1, 1);
                            Vector dts = new Vector(1, 1);
                            int c = rs1.getColumnCount();
                            for(int i = 1;i<=c;i++)
                                hds.add(rs1.getColumnName(i));
                            while(rs.next())
                            {
                                Vector temp = new Vector(1,1);
                                for(int i = 1;i<=c;i++)
                                    temp.add(rs.getString(i));
                                dts.add(temp);
                            }
                            System.out.println(hds);
                            try{
                            res.remove(dataPane);
                            }catch(Exception e1){}
                            dataTable = new JTable(dts, hds);
                            dataPane = new JScrollPane(dataTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                            dataTable.repaint();
                            res.add(dataPane);
                            setVisible(false);
                            setVisible(true);
                            cl.show(pmrl,"res");
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            JOptionPane.showMessageDialog(rootPane, "Unable to load table data due to server error !");
                            }

           }
       }
       ).start();

    }
    public void loadDatabases()
    {
        Vector v1 = DatabaseConnection.getDatabaseNames();
        for(int i = 0;i<v1.size();i++)
        {
            DefaultMutableTreeNode temp = new DefaultMutableTreeNode(v1.elementAt(i).toString());
            loadTables(temp, v1.elementAt(i).toString());
            db.add(temp);
        }
    }
    public void loadTables(DefaultMutableTreeNode parent, String dbn)
    {
        DatabaseConnection.setUpDatabase(dbn);
        Vector v1 = DatabaseConnection.getTableNames();
        for(int i = 0;i<v1.size();i++)
        {
            DefaultMutableTreeNode temp = new DefaultMutableTreeNode(v1.elementAt(i).toString());
            parent.add(temp);
        }
    }
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource()==load)
        {
            try{
            fd = new FileDialog(this, "Open File", FileDialog.LOAD);
            fd.show();

            String fl = fd.getDirectory() +fd.getFile();
            FileReader fr = new FileReader(fl);
            int i;
            ep.setText("");
            while((i=fr.read())!=-1)
            {
            ep.setText(ep.getText() + (char)i);
            }
            }
            catch(Exception e1){}
        }
        if((e.getSource()==save) || (e.getSource()==bSave))
        {
            try{
            fd = new FileDialog(this, "Save to HardDisk", FileDialog.SAVE);
            fd.show();
            try
            {
                String fl =null;
                fl=ep.getText();
                String file = fd.getDirectory() + fd.getFile();
                FileWriter fr= new FileWriter(file);
                fr.write(fl);
                fr.close();
            }catch(IOException ae)
                {
                    System.err.println("File error");
                }
            }catch(Exception ae){}
        }
        if(e.getSource()==exit)
        {
            System.exit(1);
        }
        if((e.getSource()==find)||(e.getSource()==bFind))
        {
            findPanel.setVisible(!findPanel.isVisible());
        }
        if(e.getSource()==new_con)
        {
            new Settings(this);
        }
        if(e.getSource()==abt)
        {
            JInternalFrame f=new JInternalFrame("About");
            f.setBounds(20,20,400,400);
            JEditorPane edt=new JEditorPane();
            edt.setText(" Database Explorer developed by CMC group of Java Core Experts :P \n Copyright not available :D");
            edt.setEnabled(false);
            f.add(edt,BorderLayout.NORTH);
            dp.add(f);
            f.setClosable(true);
            f.setVisible(true);
        }
        if(e.getSource()==bRun)
        {
            runQuery();
        }
        if(e.getSource()==bStop)
        {
            Pmesg.setText("Query executed.");
            cl.show(pmrl, "msg");
        }
        if(e.getSource()==mesg)
        {
            cl.show(pmrl,"msg");
        }
        if(e.getSource()==rslt)
        {
            cl.show(pmrl,"res");
        }
    }
    public void runQuery()
    {
        String qr = ep.getText();
        String msg = "Executing Query : \""+qr+"\"";
        Pmesg.setText(Pmesg.getText()+msg+"\n");
        while(qr.startsWith(" "))
            if(qr.startsWith(" "))
                qr = qr.substring(1);
        String query = qr.substring(0, qr.indexOf(" "));
        //query = "\""+query+"\"";
        if(query.equalsIgnoreCase("SELECT"))
        {
            try{
            ResultSet rs = DatabaseConnection.executeQuery(qr);
            System.out.println(qr);
            ResultSetMetaData rs1 = rs.getMetaData();
            Vector hds = new Vector(1, 1);
            Vector dts = new Vector(1, 1);
            int c = rs1.getColumnCount();
            for(int i = 1;i<=c;i++)
                hds.add(rs1.getColumnName(i));
            while(rs.next())
            {
                Vector temp = new Vector(1,1);
                for(int i = 1;i<=c;i++)
                    temp.add(rs.getString(i));
                dts.add(temp);
            }
            System.out.println(hds);
            try{
            res.remove(dataPane);
            }catch(Exception e1){e1.printStackTrace();}
            dataTable = new JTable(dts, hds);
            dataPane = new JScrollPane(dataTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            dataTable.repaint();
            res.add(dataPane);
            setVisible(false);
            setVisible(true);
            cl.show(pmrl,"res");
            Pmesg.setText(Pmesg.getText()+"Select Query Executed Successfully"+"\n\n");
            }catch(Exception e){e.printStackTrace();}
        }
        else
        {
            cl.show(pmrl,"msg");
            Pmesg.setText(Pmesg.getText()+"Executing Query : " + qr+"\n");
            int rs = DatabaseConnection.executeUpdate(qr, Pmesg);
            Pmesg.append("Query Executed!"+"\n\n");
            Pmesg.append("Record(s) effected : " + rs+"\n\n");
        }
        //DatabaseConnection.execute(msg)
        
    }
    public void mouseClicked(MouseEvent e) 
    {
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
}
