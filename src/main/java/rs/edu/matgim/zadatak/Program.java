package rs.edu.matgim.zadatak;

public class Program {

    public static void main(String[] args) {

        DB _db = new DB();
        _db.printFirma();
        _db.printUkupnoPopravki();
         System.out.println(_db.zadatak(9));
    }
}
