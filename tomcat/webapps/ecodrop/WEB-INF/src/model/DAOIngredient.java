package model;

import java.util.List;

public interface DAOIngredient {
    public List<Ingredient> findAll();

    public Ingredient findById(int id);

    public boolean save(Ingredient ingr);

}
