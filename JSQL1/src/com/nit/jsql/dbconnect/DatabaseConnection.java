
package com.nit.jsql.dbconnect;

import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class DatabaseConnection 
{
    static Connection CONNECTION;
    static Statement STATEMENT;
    static ResultSet RESULTSET;
    static JFrame parent;
    static
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(parent, "Unable to Start Application, due to Invalid Library of MySQL.", "MySQL Librrary Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static boolean setUpDatabase()
    {
        try {
            DB.load();
            CONNECTION = DriverManager.getConnection("jdbc:mysql://"+DB.IP+":"+DB.PORT+"/"+DB.DATABASE, DB.USER, DB.PASSWORD);
            STATEMENT = CONNECTION.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    public static boolean setUpDatabase(String db)
    {
        try {
            CONNECTION = DriverManager.getConnection("jdbc:mysql://"+DB.IP+":"+DB.PORT+"/"+db, DB.USER, DB.PASSWORD);
            STATEMENT = CONNECTION.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
    public static int executeUpdate(String SQL, JTextArea rArea)
    {
        try {
            return STATEMENT.executeUpdate(SQL);
        }
        catch(SQLException ex) {
            rArea.append(ex.toString() + "\n");
            return 0;
        }
    }
    public static ResultSet executeQuery(String SQL)
    {
        try {
            return STATEMENT.executeQuery(SQL);
        }
        catch(SQLException ex) {
            return null;
        }
    }
    public static void changeDatabase(String db)
    {
        try
        {
        DB.DATABASE = db;
        CONNECTION = DriverManager.getConnection("jdbc:mysql://"+DB.IP+":"+DB.PORT+"/"+DB.DATABASE, DB.USER, DB.PASSWORD);
        STATEMENT = CONNECTION.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }
        catch(Exception e){e.printStackTrace();}
    }
    public static boolean execute(String SQL)
    {
        try {
            return STATEMENT.execute(SQL);
        }
        catch(SQLException ex) {
            return false;
        }
    }
    public static Vector getDatabaseNames()
    {
        try {
            RESULTSET = STATEMENT.executeQuery("show databases");
            Vector data = new Vector(1,1);
            while(RESULTSET.next())
            {
                data.add(RESULTSET.getString(1));
            }
            return data;
        }
        catch(SQLException ex) {
            return new Vector(1,1);
        }
    }
    public static Vector getTableNames()
    {
        try {
            RESULTSET = STATEMENT.executeQuery("show tables");
            Vector data = new Vector(1,1);
            while(RESULTSET.next())
            {
                data.add(RESULTSET.getString(1));
            }
            return data;
        }
        catch(SQLException ex) {
            return new Vector(1,1);
        }
    }    
}
