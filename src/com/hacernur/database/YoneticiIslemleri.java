
package com.hacernur.database;

import com.hacernur.girisler.Yonetici;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YoneticiIslemleri extends AbstractDatabase implements Isil{
    
    private int id;
    private String ad;
    private String sifre;

    public YoneticiIslemleri() {}    
    
    public YoneticiIslemleri(int id, String ad, String sifre) {
        super();
        this.id = id;
        this.ad = ad;
        this.sifre = sifre;
    }
    
    @Override
    void abstractMetodYaz() {
        System.out.println("Abstract Metod");
    }

    @Override
    public void ekle() {
        String sorgu = "Insert Into kullanicilar (kullaniciAdi,sifre) VALUES(?,?)";
       
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, sifre);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(YoneticiIslemleri.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void guncelle() {
        String sorgu = "Update kullanicilar set kullaniciAdi = ? , sifre = ? where id = ?";
   
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, sifre);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(YoneticiIslemleri.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sil(int id) {
        String sorgu = "Delete from kullanicilar where id = ?";
        
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(YoneticiIslemleri.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Yonetici> yoneticiGetir(){
        ArrayList<Yonetici> cikti = new ArrayList<Yonetici>();
        
        try {
            statement = con.createStatement();
            
            String sorgu = "Select * From kullanicilar";
            ResultSet rs = statement.executeQuery(sorgu);
            
            while(rs.next()){
                int id = rs.getInt("id");
                String ad = rs.getString("kullaniciAdi");
                String sifre = rs.getString("sifre");
                
                cikti.add(new Yonetici(id,ad, sifre));
            }
            return cikti;
        } catch (SQLException ex) {
            Logger.getLogger(YoneticiIslemleri.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }   
    }
    
    public boolean girisYap(String kullaniciAdi, String parola) {
        String sorgu = "Select * From kullanicilar where kullaniciAdi = ? and sifre = ?";
        
        try {
            preparedStatement = con.prepareStatement(sorgu);
            
            preparedStatement.setString(1, kullaniciAdi);
            preparedStatement.setString(2, parola);
            
            ResultSet rs = preparedStatement.executeQuery();
            
            return rs.next();
           
        } catch (SQLException ex) {
            Logger.getLogger(YoneticiIslemleri.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void borcOlustur(){

        try {
            statement = con.createStatement();
            
            String sorgu = "Select * From oduncKitaplar";
            ResultSet rs = statement.executeQuery(sorgu);
            
            while(rs.next()){
                
                int uyeNo = rs.getInt("uyeNo");
                Date teslimTarihi = rs.getDate("teslimTarihi");
                
                if(OduncKitapIslemleri.teslimTarihiGectiMi(teslimTarihi)){
                    String sorgu2 = "Update uyeler set uyeBorc = ? where uyeNo = '" + uyeNo +"'";
                    preparedStatement = con.prepareStatement(sorgu2);
                    int kacKitap = kacKitapAlmis(uyeNo);
                    if(kacKitap == 1){               
                        preparedStatement.setDouble(1, borcDon(teslimTarihi));
                    
                    }
                    else if (kacKitap > 1){
                        preparedStatement.setDouble(1, borcOlustur2(uyeNo,kacKitap));
                    }
                    preparedStatement.executeUpdate();
                }
            }
         
        } catch (SQLException ex) {
            Logger.getLogger(YoneticiIslemleri.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 
    
    public double borcOlustur2(int no, int kacKitap){
        double toplamBorc = 0;
        try {
            statement = con.createStatement();
            String sorgu = "Select * From odunckitaplar where uyeNo = '" + no + "'";
            ResultSet rs = statement.executeQuery(sorgu);
            while(rs.next()){
                Date teslimTarihi = rs.getDate("teslimTarihi");
                toplamBorc += borcDon(teslimTarihi);
            }
            return toplamBorc;
        } catch (SQLException ex) {
            Logger.getLogger(YoneticiIslemleri.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        
    }
    
    public double borcDon(Date teslimTarihi){  
        LocalDate now = LocalDate.now();
        Date dateNow = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
        long fark = TimeUnit.DAYS.convert((dateNow.getTime() - teslimTarihi.getTime()), TimeUnit.MILLISECONDS);
        return (double) fark*2;
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
                   say++; 
                }
            }
            return say;
        } catch (SQLException ex) {
            Logger.getLogger(YoneticiIslemleri.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
