package model ;

import java.util.List;

public interface JoueurDao {
    public Joueur findById(int id);

    public List<Joueur> findAll();

    public void create(Joueur joueur);

    public void delete(int id);
}
