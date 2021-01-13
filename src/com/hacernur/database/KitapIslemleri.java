
package com.hacernur.database;

import com.hacernur.girisler.Kitaplar;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KitapIslemleri extends AbstractDatabase implements Isil{
    
    private int id;
    private String ad;
    private String yazar;
    private String tur;
    private String yayinevi;
    private int sayi;
    private String tarih;

    public KitapIslemleri() {}
    
    public KitapIslemleri(int id, String ad, String yazar, String tur, String yayinevi, int sayi, String tarih) {
        super();
        this.id = id;
        this.ad = ad;
        this.yazar = yazar;
        this.tur = tur;
        this.yayinevi = yayinevi;
        this.sayi = sayi;
        this.tarih = tarih;
    }
    
    @Override
    void abstractMetodYaz() {
        System.out.println("Abstract Metod");
    }
    
    @Override
    public void ekle() {
        String sorgu = "Insert Into kitaplar (ad, yazar, tur, yayinevi, sayfaSayisi, basimTarihi) VALUES(?,?,?,?,?,?)";
        
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, yazar);
            preparedStatement.setString(3, tur);
            preparedStatement.setString(4, yayinevi);
            preparedStatement.setInt(5, sayi);
            preparedStatement.setString(6, tarih);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(KitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void guncelle() {
        String sorgu = "Update kitaplar set ad = ? , yazar = ? , tur = ? , yayinevi = ? , sayfaSayisi = ? , basimTarihi = ? where kitapNo = ?";
        
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, yazar);
            preparedStatement.setString(3, tur);
            preparedStatement.setString(4, yayinevi);
            preparedStatement.setInt(5, sayi);
            preparedStatement.setString(6, tarih);
            preparedStatement.setInt(7, id);
            
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(KitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
     
    @Override
    public void sil(int id) {
        String sorgu = "Delete from kitaplar where kitapNo = ?";
        
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(KitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Kitaplar> kitaplariGetir(){
        ArrayList<Kitaplar> cikti = new ArrayList<Kitaplar>();
        
        try {
            statement = con.createStatement();
            
            String sorgu = "Select * From kitaplar";
            ResultSet rs = statement.executeQuery(sorgu);
            
            while(rs.next()){
                int kitapNumarasi = rs.getInt("kitapNo");
                String ad = rs.getString("ad");
                String yazar = rs.getString("yazar");
                String tur = rs.getString("tur");
                String yayınevi = rs.getString("yayınevi");
                int sayfa = rs.getInt("sayfaSayisi");
                String baskiTarihi = rs.getString("basimTarihi");
                
                cikti.add(new Kitaplar(kitapNumarasi, ad, yazar, tur, yayınevi, sayfa, baskiTarihi));
            }
            return cikti;
        } catch (SQLException ex) {
            Logger.getLogger(KitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }   
    }
    
    public ArrayList<Kitaplar> araKitap(String adi){
        ArrayList<Kitaplar> cikti = new ArrayList<Kitaplar>();
        
        try {
            statement = con.createStatement();
            
            String sorgu = "Select * From kitaplar where ad = '" + adi + "'";
            ResultSet rs = statement.executeQuery(sorgu);
            
            while(rs.next()){
                int kitapNumarasi = rs.getInt("kitapNo");
                String ad = rs.getString("ad");
                String yazar = rs.getString("yazar");
                String tur = rs.getString("tur");
                String yayınevi = rs.getString("yayinevi");
                int sayfa = rs.getInt("sayfaSayisi");
                String baskiTarihi = rs.getString("basimTarihi");
                
                cikti.add(new Kitaplar(kitapNumarasi, ad, yazar, tur, yayınevi, sayfa, baskiTarihi));
            }
            return cikti;
        } catch (SQLException ex) {
            Logger.getLogger(KitapIslemleri.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }  
    }    
    
}
