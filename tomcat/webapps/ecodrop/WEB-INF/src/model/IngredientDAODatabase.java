package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAODatabase implements DAOIngredient {
    static final DS DS = new DS();

    public Ingredient findById(int id) {
        Ingredient ingredient = null;

        try (Connection con = DS.getConnection()) {
            String query = "select * from ingredients where id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String nom = rs.getString("nom");
                int prix = rs.getInt("prix");

                ingredient = new Ingredient(id, nom, prix);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return ingredient;
    }

    public List<Ingredient> findAll() {
        List<Ingredient> lesIngredients = new ArrayList<>();
        try (Connection con = DS.getConnection()) {
            String query = "select * from ingredients";
            PreparedStatement ps = con.prepareStatement(query);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                int prix = rs.getInt("prix");

                Ingredient ingredient = new Ingredient(id, nom, prix);
                lesIngredients.add(ingredient);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return lesIngredients;
    }

    public boolean save(Ingredient ingr) {
        boolean idNotPresent = true;

        for (Ingredient elem : findAll()) {
            if (elem.getId() == ingr.getId())
                idNotPresent = false;
        }
        if (idNotPresent) {
            try (Connection con = DS.getConnection()) {
                String query = "insert into ingredients(id,nom,prix) values(?,?,?)";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, ingr.getId());
                ps.setString(2, ingr.getNom());
                ps.setInt(3, ingr.getPrix());

                System.out.println(ps);
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return idNotPresent;
    }
}
