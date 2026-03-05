package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PartieDaoJdbc implements PartieDao {
    static final DS DS = new DS();

    public Partie findById(int id) {
        Partie partie = null;

        try (Connection con = DS.getConnection()) {
            String query = "select * from partie where pno=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int pno = rs.getInt("pno");
                int jno1 = rs.getInt("jno1");
                int jno2 = rs.getInt("jno2");
                String date = rs.getString("date");
                String statut = rs.getString("statut");
                int temps = rs.getInt("temps");
                int gagnant = rs.getInt("gagnant");

                partie = new Partie(pno, jno1, jno2, date, statut, temps, gagnant);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return partie;
    }

    public void create(Partie partie) {
        int pno = partie.getPno();
        int jno1 = partie.getJno1();
        int jno2 = partie.getJno2();
        String date = partie.getDate();
        String statut = partie.getStatut();
        int temps = partie.getTemps();
        int gagnant = partie.getGagnant();

        try (Connection con = DS.getConnection()) {
            String query = "insert into partie values(?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, pno);
            ps.setInt(2, jno1);
            ps.setInt(3, jno2);
            ps.setString(4, date);
            ps.setString(5, statut);
            ps.setInt(6, temps);
            ps.setInt(7, gagnant);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<Partie> findAll() {
        List<Partie> lesParties = new ArrayList<>();
        try (Connection con = DS.getConnection()) {
            String query = "select * from partie";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int pno = rs.getInt("pno");
                int jno1 = rs.getInt("jno1");
                int jno2 = rs.getInt("jno2");
                String date = rs.getString("date");
                String statut = rs.getString("statut");
                int temps = rs.getInt("temps");
                int gagnant = rs.getInt("gagnant");

                Partie unePartie = new Partie(pno, jno1, jno2, date, statut, temps, gagnant);
                lesParties.add(unePartie);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return lesParties;
    }

    public void delete(int id) {

        try (Connection con = DS.getConnection()) {
            String query = "delete from partie where pno=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
