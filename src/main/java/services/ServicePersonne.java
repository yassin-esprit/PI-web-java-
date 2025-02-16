////package services;
////
////import models.Personne;
////import tools.MyDB;
//
//import java.sql.*;
//import java.util.ArrayList;
////import java.util.List;
////
////public class ServicePersonne
////        implements IServices<Personne>{
////    private Connection cnx;
////    public ServicePersonne(){
////        cnx= MyDB.getInstance().getConnection();
////    }
////    @Override
////    public void ajouter(Personne p) throws SQLException {
//////        String sql="insert into personne(nom,prenom,age)" +
//////                "values ('"+p.getNom()+"','"+p.getPrenom()+
//////                "',"+p.getAge()+")";
//////        Statement ste = cnx.createStatement();
//////        ste.executeUpdate(sql);
////        String sql ="insert into personne (nom,prenom,age)" +
////                "values (?,?,?)";
////        PreparedStatement ste = cnx.prepareStatement(sql);
////        ste.setString(1,p.getNom());
////        ste.setString(2,p.getPrenom());
////        ste.setInt(3,p.getAge());
////        ste.executeUpdate();
////        System.out.println("personne ajoutée");
////    }
////
////    @Override
////    public void supprimer(int id) throws SQLException {
////        String sql="delete from personne where id=?";
////        PreparedStatement ste = cnx.prepareStatement(sql);
////        ste.setInt(1,id);
////        ste.executeUpdate();
////        System.out.println("personne supprimée");
////
////
////    }
////
////    @Override
////    public void modifier(int id,String nom,int age) throws SQLException {
////        String sql ="update personne set nom=? , age =? " +
////                "where id=?  ";
////        PreparedStatement ste = cnx.prepareStatement(sql);
////        ste.setString(1,nom);
////        ste.setInt(2,age);
////        ste.setInt(3,id);
////        ste.executeUpdate();
////        System.out.println("Personne modifiée");
////
////
////    }
////
////    @Override
////    public List<Personne> recuperer() throws SQLException {
////        String sql ="select * from personne";
////        Statement ste = cnx.createStatement();
////        ResultSet rs= ste.executeQuery(sql);
////        List<Personne> personnes = new ArrayList<>();
////        while(rs.next()){
////            Personne p = new Personne();
////            p.setId(rs.getInt(1));
////            p.setNom(rs.getString("nom"));
////            p.setPrenom(rs.getString("prenom"));
////            p.setAge(rs.getInt("age"));
////            personnes.add(p);
////        }
////        return personnes;
////    }
////}
