
package com.hacernur.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


 class DatabaseBaglanti {
    
    public static final String kullanici_adi = "root";
    public static final String parola = "";
    public static final String db_ismi = "kütüphane otomasyonu";
    public static final String host =  "localhost";
    public static final int port = 3306;
    
    public Connection con = null;  
    public Statement statement = null;
    public PreparedStatement preparedStatement = null;
    
    public DatabaseBaglanti(){
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db_ismi+ "?useUnicode=true&characterEncoding=utf8";
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, DatabaseBaglanti.kullanici_adi, DatabaseBaglanti.parola);
            System.out.println("Bağlantı Başarılı...");            
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver Bulunamadı....");
        } catch (SQLException ex) {
            System.out.println("Bağlantı Başarısız...");
        }   
    }   
    
    public void polymorphismMetodu(){
        System.out.println("Polymorphism Metodu");
    }
}

abstract class AbstractDatabase extends DatabaseBaglanti{
    abstract void abstractMetodYaz();
}

class MiniDatabase1 extends DatabaseBaglanti{

    @Override
    public void polymorphismMetodu() {
        System.out.println("Override Edilmiş Polymorphism Metodu");
    }  
}

class MiniDatabase2 extends DatabaseBaglanti{

    @Override
    public void polymorphismMetodu() {
        System.out.println("Override Edilmiş Polymorphism Metodu 2");
    }
}

public class Database{
    public static void main(String[] args) {
        DatabaseBaglanti baglanti = new DatabaseBaglanti();
        MiniDatabase1 mDatabase1 = new MiniDatabase1();
        MiniDatabase2 mDatabase2 = new MiniDatabase2();
        metod(baglanti);
        metod(mDatabase1);
        metod(mDatabase2);       
    }
    public static void metod(DatabaseBaglanti baglanti){
        baglanti.polymorphismMetodu();
    }
}
