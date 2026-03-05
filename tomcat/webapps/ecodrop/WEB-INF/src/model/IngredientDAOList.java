package model;

import java.util.ArrayList;
import java.util.List;

public class IngredientDAOList implements DAOIngredient {
    private Ingredient ingr1 = new Ingredient(1, "poivons", 2);
    private Ingredient ingr2 = new Ingredient(2, "artichaut", 2);
    private Ingredient ingr3 = new Ingredient(3, "tomate", 2);

    private List<Ingredient> ingredients = new ArrayList<>(List.of(ingr1, ingr2, ingr3));

    public List<Ingredient> findAll() {
        return ingredients;
    }

    public Ingredient findById(int id) {
        int trueId = id % ingredients.size();
        return ingredients.get(trueId);
    }

    public boolean save(Ingredient ingr) {
        boolean idNotPresent = true;
        for (Ingredient elem : ingredients) {
            if (elem.getId() == ingr.getId()) {
                idNotPresent = false;
            }
        }
        if (idNotPresent)
            ingredients.add(ingr);
        return idNotPresent;
    }
}
