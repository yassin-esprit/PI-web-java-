//package Services;
//
//import Models.User;
//import Tools.MyDB;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class UserServices {
//    private Connection cnx;
//    public UserServices(){
//        cnx= MyDB.getInstance().getConnection();
//    }
//
//
//
//    public void add(User user) throws SQLException {
////        String sql="insert into personne(nom,prenom,age)" +
////                "values ('"+p.getNom()+"','"+p.getPrenom()+
////                "',"+p.getAge()+")";
////        Statement ste = cnx.createStatement();
////        ste.executeUpdate(sql);
//        String sql ="insert into users (first_name,last_name,phone,email,password)" +
//                "values (?,?,?,?,?)";
//        Connection cnx = MyDB.getInstance().getConnection();
//        PreparedStatement ste = cnx.prepareStatement(sql);
//        ste.setString(1,user.getFirstName());
//        ste.setString(2,user.getLastName());
//        ste.setInt(3,user.getPhone());
//        ste.setString(4,user.getEmail());
//        ste.setString(5,user.getPassword());
//
//        ste.executeUpdate();
//        System.out.println("personne ajoutée");
//    }
//
//    ////////////////////////////////////ADDED BY GHAITH TO TEST/////////////////////////////////////////////////////
//
////    public User getUser(int id) throws SQLException {
////        String query = "SELECT * FROM users WHERE id = ?";
////        PreparedStatement stmt = cnx.prepareStatement(query);
////        stmt.setInt(1, id);
////        ResultSet rs = stmt.executeQuery();
////
////        if (rs.next()) {
////            return new User(
////                    rs.getInt("id"),
////                    rs.getString("first_name"),
////                    rs.getString("last_name"),
////                    rs.getInt("phone"),
////                    rs.getString("email"),
////                    rs.getString("password") // Only include if necessary
////            );
////        }
////
////        return null; // Return null if user not found
////    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//}


package services;

import models.User;
import tools.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServices {
    private Connection cnx;

    public UserServices() {
        cnx = MyDB.getInstance().getConnection();
    }

    public void add(User user) throws SQLException {
        // Modified SQL to include role
        String sql = "INSERT INTO user (firstname, lastname, phone, email, password, role) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);

        ste.setString(1, user.getFirstName());
        ste.setString(2, user.getLastName());
        ste.setString(3, user.getPhone());
        ste.setString(4, user.getEmail());
        ste.setString(5, user.getPassword());

        // Set the role (assuming it's an Enum, convert it to string)
        ste.setString(6, user.getRole().name());  // Use the role's name (e.g., "ADMIN", "OWNER", etc.)

        ste.executeUpdate();
        System.out.println("User added");
    }

//    public User getUser(int id) throws SQLException {
//        String query = "SELECT * FROM users WHERE id = ?";
//        PreparedStatement stmt = cnx.prepareStatement(query);
//        stmt.setInt(1, id);
//        ResultSet rs = stmt.executeQuery();
//
//        if (rs.next()) {
//            return new User(
//                    rs.getInt("id"),
//                    rs.getString("first_name"),
//                    rs.getString("last_name"),
//                    rs.getInt("phone"),
//                    rs.getString("email"),
//                    rs.getString("password"),
//                    rs.getString("role"), // Ensure "role" exists in your database
//                    rs.getTimestamp("created_at") // Ensure "created_at" exists in your database
//            );
//        }
//
//        return null; // Return null if user not found
//    }

///////////////////////////////////////////////////////////
public User getUser(int id) throws SQLException {
    String query = "SELECT * FROM user WHERE user_id = ?";
    PreparedStatement stmt = cnx.prepareStatement(query);
    stmt.setInt(1, id);
    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
        return new User(
                rs.getInt("userid"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("password"),
                User.Role.valueOf(rs.getString("Role").toUpperCase()), // Convert String to Enum
                rs.getTimestamp("created_at")
        );
    }

    return null; // Return null if user not found
}

    public List<User> getAdmins() throws SQLException {
        String query = "SELECT * FROM user WHERE role = ?";
        PreparedStatement stmt = cnx.prepareStatement(query);
        stmt.setString(1, User.Role.ADMIN.name()); // Récupérer uniquement les utilisateurs avec le rôle ADMIN
        ResultSet rs = stmt.executeQuery();

        List<User> admins = new ArrayList<>();
        while (rs.next()) {
            User user = new User(
                    rs.getInt("userid"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("password"),
                    User.Role.valueOf(rs.getString("role").toUpperCase())
                    //   rs.getTimestamp("created_at")
            );
            admins.add(user);
        }
        return admins;
    }

///////////////////////////////////////



//    public User getUser(int id) throws SQLException {
//        String query = "SELECT * FROM users WHERE id = ?";
//        PreparedStatement stmt = cnx.prepareStatement(query);
//        stmt.setInt(1, id);
//        ResultSet rs = stmt.executeQuery();
//
//        if (rs.next()) {
//            return new User(
//                    rs.getInt("id"),
//                    rs.getString("first_name"),
//                    rs.getString("last_name"),
//                    rs.getInt("phone"),
//                    rs.getString("email"),
//                    rs.getString("password") // Only include if necessary
//            );
//        }
//
//        return null; // Return null if user not found
//    }
}
