package rs.edu.matgim.zadatak;

import java.sql.*;
import java.util.ArrayList;

public class DB {

    String connectionString = "jdbc:sqlite:src\\main\\java\\KompanijaZaPrevoz.db";

    public void printFirma() {
        try (Connection conn = DriverManager.getConnection(connectionString); Statement s = conn.createStatement()) {

            ResultSet rs = s.executeQuery("SELECT * FROM Firma");
            while (rs.next()) {
                int IdFil = rs.getInt("IdFir");
                String Naziv = rs.getString("Naziv");
                String Adresa = rs.getString("Adresa");
                String Tel1 = rs.getString("Tel1");
                String Tel2 = rs.getString("Tel2");

                System.out.println(String.format("%d\t%s\t%s\t%s\t%s", IdFil, Naziv, Adresa, Tel1, Tel2));
            }

        } catch (SQLException ex) {
            System.out.println("Greska prilikom povezivanja na bazu");
            System.out.println(ex);
        }
    }
    public void printUkupnoPopravki() {
        try (Connection conn = DriverManager.getConnection(connectionString); Statement s = conn.createStatement()) {

            ResultSet rs = s.executeQuery("SELECT Marka, SUM(BrPopravljanja) AS BR FROM Kamion GROUP BY Marka ORDER BY BR DESC");
                    
            
            while (rs.next()) {
                int BrPopravljanja = rs.getInt("BR");
                String Marka = rs.getString("Marka");
                

                System.out.println(String.format("%d\t%s", BrPopravljanja, Marka));
            }

        } catch (SQLException ex) {
            System.out.println("Greska prilikom povezivanja na bazu");
            System.out.println(ex);
        }
    }
    public boolean zadatak(int IdKam) {
        try (Connection conn = DriverManager.getConnection(connectionString); Statement s = conn.createStatement()) {
            conn.setAutoCommit(false);
            int br1 =1;
            PreparedStatement ss = conn.prepareStatement("UPDATE Kamion Set BrPopravljanja = BrPopravljanja + ? WHERE IDKam = ?") ;  
            ss.setInt(1,br1);
            ss.setInt(2, IdKam);
            ss.execute();
            ArrayList<Integer> radnici = new ArrayList<Integer>();
            ArrayList<Integer> kamioni = new ArrayList<Integer>();
            ResultSet rs = s.executeQuery("SELECT IDZap FROM popravlja WHERE IDKam="+IdKam);
            while (rs.next()) {
                int k =rs.getInt("IDZap");
                radnici.add(k);
                
            }
            for(int i=0;i<radnici.size();i++)
            {
                PreparedStatement sss=conn.prepareStatement("DELETE FROM popravlja WHERE IDZap=?");
                
                sss.setInt(1, radnici.get(i));
                sss.execute();
            }
            ResultSet rss = s.executeQuery("SELECT DISTINCT IDKam FROM popravlja");
            int sum = 0;
            while(rss.next()){
                kamioni.add(rss.getInt("IDKam"));
            }
            int k=0;
            if(kamioni.size()>radnici.size())
            {
                sum = radnici.size();
                System.out.println("nema");
            }
            else
            {
                sum = kamioni.size();
                k =1;
                
            }
            
            for (int i=0;i<sum;i++)
            {
                
                PreparedStatement fin = conn.prepareStatement("INSERT INTO popravlja (Dana, IDZap,IDKam) VALUES (?,?,?)");
                fin.setInt(1,br1);
                fin.setInt(2,radnici.get(i));
                fin.setInt(3,kamioni.get(i));
                fin.execute();
            }
            if(k==1)
            {
                for(int i=sum+1;i<radnici.size();i++)
                {
                    System.out.println(String.format("%d", radnici.get(i)));
                }
            }
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            System.out.println("Greska prilikom povezivanja na bazu");
            System.out.println(ex);
            return false;
            
        }
    }

}
