
package com.nit.jsql.dbconnect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class DB 
{
    public static String DATABASE, USER, PASSWORD, IP, PORT;
    static
    {
        DATABASE = "mysql";
        USER = "root";
        PASSWORD = "";
        IP = "127.0.0.1";
        PORT = "3306";
    }
    
    public static void load()throws Exception
    {
                File f1 = new File("DPPROP.JSQLDB");
                FileInputStream fos = new FileInputStream(f1);
                ObjectInputStream oos = new ObjectInputStream(fos);
                Vector v1 = (Vector)oos.readObject();
                DATABASE = v1.elementAt(0).toString();
                USER = v1.elementAt(1).toString();
                PASSWORD = v1.elementAt(2).toString();
                IP = v1.elementAt(3).toString();
                PORT= v1.elementAt(4).toString();
                oos.close();
                fos.close();
    }
}
