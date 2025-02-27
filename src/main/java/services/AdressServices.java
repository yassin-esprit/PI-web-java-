package services;

import Models.Adress;
import Tools.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class AdressServices {
    private Connection cnx;

    public AdressServices() {
        cnx = MyDB.getInstance().getConnection();
    }

    public void addaddres(Adress adress) throws SQLException {
//        String sql="insert into personne(nom,prenom,age)" +
//                "values ('"+p.getNom()+"','"+p.getPrenom()+
//                "',"+p.getAge()+")";
//        Statement ste = cnx.createStatement();
//        ste.executeUpdate(sql);

        String sql ="insert into address (user_id,address_line,city,state,postal_code,country)" +
                "values (?,?,?,?,?,?)";
        Connection cnx = MyDB.getInstance().getConnection();
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1,adress.getUserId());
        ste.setString(2,adress.getAddressLine());
        ste.setString(3,adress.getCity());
        ste.setString(4,adress.getState());
        ste.setString(5,adress.getState());
        ste.setString(6,adress.getCountry());

        ste.executeUpdate();
        System.out.println("adress ajoutée");
    }

    public Adress getAdress(int id) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = ?";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Adress(
                    rs.getString("addressLine"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("postalcode"),
                    rs.getString("country")
                         );
        }

        return null; // Return null if user not found
    }

}
