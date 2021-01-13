
package com.hacernur.database;

import com.hacernur.girisler.OduncKitaplar;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OduncKitapIslemleri extends AbstractDatabase implements Isil{
   
    private int kitapNo;
    private String ad;
    private String yazar;
    private int uyeNo;
    private String uyeAd;
    private String uyeSoyad;
    private String verilisTarihi;
    private String teslimTarihi;

    public OduncKitapIslemleri() {}    
    
    public OduncKitapIslemleri(int kitapNo, String ad, String yazar, int uyeNo, String uyeAd, String uyeSoyad, String verilisTarihi, String teslimTarihi) {
        super();
        this.kitapNo = kitapNo;
        this.ad = ad;
        this.yazar = yazar;
        this.uyeNo = uyeNo;
        this.uyeAd = uyeAd;
        this.uyeSoyad = uyeSoyad;
        this.verilisTarihi = verilisTarihi;
        this.teslimTarihi = teslimTarihi;
    }
        
    @Override
    void abstractMetodYaz() {
        System.out.println("Abstract Metod");
    }

    @Override
    public void ekle() {
        String sorgu = "Insert Into odunckitaplar (kitapNo, ad, yazar, uyeNo, uyeAd, uyeSoyad, verilisTarihi, teslimTarihi) VALUES(?,?,?,?,?,?,?,?)";
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        java.sql.Date verilisTarihiSql = null;
        java.sql.Date teslimTarihiSql = null;
        try {
            java.util.Date tarihJava = format.parse(verilisTarihi);
            java.util.Date tarihJava2 = format.parse(teslimTarihi);
            verilisTarihiSql = new java.sql.Date(tarihJava.getTime());
            teslimTarihiSql = new java.sql.Date(tarihJava2.getTime());           
            
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setInt(1, kitapNo);
            preparedStatement.setString(2, ad);
            preparedStatement.setString(3, yazar);
            preparedStatement.setInt(4, uyeNo);
            preparedStatement.setString(5, uyeAd);
            preparedStatement.setString(6, uyeSoyad);
            preparedStatement.setDate(7, verilisTarihiSql);
            preparedStatement.setDate(8, teslimTarihiSql);
            preparedStatement.executeUpdate();            
        } catch (SQLException ex) {
            Logger.getLogger(OduncKitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(OduncKitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void guncelle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void sil(int id) {
        String sorgu = "Delete from odunckitaplar where kitapNo = ?";
        
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OduncKitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
   
    public ArrayList<OduncKitaplar> oduncKitaplariGetir(){
        ArrayList<OduncKitaplar> cikti = new ArrayList<OduncKitaplar>();
        
        try {
            statement = con.createStatement();
            
            String sorgu = "Select * From oduncKitaplar";
            ResultSet rs = statement.executeQuery(sorgu);
            
            while(rs.next()){
                int kitapNo = rs.getInt("kitapNo");
                String ad = rs.getString("ad");
                String yazar = rs.getString("yazar");
                int uyeNo = rs.getInt("uyeNo");
                String uyeAd = rs.getString("uyeAd");
                String uyeSoyad = rs.getString("uyeSoyad");
                Date verilisTarihi = rs.getDate("verilisTarihi");
                Date teslimTarihi = rs.getDate("teslimTarihi");
                
                cikti.add(new OduncKitaplar(kitapNo, ad, yazar, uyeNo, uyeAd, uyeSoyad, verilisTarihi, teslimTarihi));
            }
            return cikti;
        } catch (SQLException ex) {
            Logger.getLogger(OduncKitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }   
    }

    public ArrayList<OduncKitaplar> araOduncKitap(String adi){ 
        ArrayList<OduncKitaplar> cikti = new ArrayList<OduncKitaplar>();
        
        try {
            statement = con.createStatement();
            
            String sorgu = "Select * From odunckitaplar where ad = '" + adi + "'";
            ResultSet rs = statement.executeQuery(sorgu);
            
            while(rs.next()){
                int kitapNo = rs.getInt("kitapNo");
                String ad = rs.getString("ad");
                String yazar = rs.getString("yazar");
                int uyeNo = rs.getInt("uyeNo");
                String uyeAd = rs.getString("uyeAd");
                String uyeSoyad = rs.getString("uyeSoyad");
                Date verilisTarihi = rs.getDate("verilisTarihi");
                Date teslimTarihi = rs.getDate("teslimTarihi");
                
                cikti.add(new OduncKitaplar(kitapNo, ad, yazar, uyeNo, uyeAd, uyeSoyad, verilisTarihi, teslimTarihi));
            }
            return cikti;
        } catch (SQLException ex) {
            Logger.getLogger(OduncKitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } 
    }

    public static boolean teslimTarihiGectiMi(Date teslimTarihi){

        LocalDate now = LocalDate.now();
        Date dateNow = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());

        if(teslimTarihi.before(dateNow)){ return true; }
        else{ return false; }
        
    }

    public boolean araOKitap(int kitapNo){
        try {
            statement = con.createStatement();
            String sorgu = "Select * From odunckitaplar";
            ResultSet rs = statement.executeQuery(sorgu);
            
            while(rs.next()){
                int kitapNo2 = rs.getInt("kitapNo");
                if(kitapNo2 == kitapNo){
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OduncKitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    } 
 
    public int kacKitapAlmis(int no){
        int say = 0;
        try {
            statement = con.createStatement();
            String sorgu = "Select * From odunckitaplar";
            ResultSet rs = statement.executeQuery(sorgu);
        
            while(rs.next()){
                int uyeNo = rs.getInt("uyeNo");
                if(no == uyeNo){
                    say ++;
                }
            }
            return say;
        } catch (SQLException ex) {
            Logger.getLogger(OduncKitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
