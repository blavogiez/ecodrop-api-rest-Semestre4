package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JoueurDaoJdbc implements JoueurDao {
    static final DS DS = new DS();

    public Joueur findById(int id) {
        Joueur joueur = null;

        try (Connection con = DS.getConnection()) {
            String query = "select * from joueur where jno=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int jno = rs.getInt("jno");
                String pseudo = rs.getString("pseudo");
                String email = rs.getString("email");
                String pwd = rs.getString("pwd");
                int elo = rs.getInt("elo");

                joueur = new Joueur(jno, pseudo, email, pwd, elo);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return joueur;
    }

    public void create(Joueur joueur) {
        int jno = joueur.getJno();
        String pseudo = joueur.getPseudo();
        String email = joueur.getEmail();
        String pwd = joueur.getPwd();
        int elo = joueur.getElo();

        try (Connection con = DS.getConnection()) {
            String query = "insert into joueur values(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, jno);
            ps.setString(2, pseudo);
            ps.setString(3, email);
            ps.setString(4, pwd);
            ps.setInt(5, elo);

            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Joueur> findAll() {
        List<Joueur> lesJoueurs = new ArrayList<>();
        try (Connection con = DS.getConnection()) {
            String query = "select * from joueur";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int jno = rs.getInt("jno");
                String pseudo = rs.getString("pseudo");
                String email = rs.getString("email");
                String pwd = rs.getString("pwd");
                int elo = rs.getInt("elo");

                Joueur unJoueur = new Joueur(jno, pseudo, email, pwd, elo);
                lesJoueurs.add(unJoueur);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return lesJoueurs;
    }

    public void delete(int id) {

        try (Connection con = DS.getConnection()) {
            String query = "delete from joueur where jno=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
