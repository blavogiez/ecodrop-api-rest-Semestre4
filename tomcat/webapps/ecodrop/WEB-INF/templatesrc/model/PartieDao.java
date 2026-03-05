package model;

import java.util.List;

public interface PartieDao {
    public Partie findById(int id);

    public List<Partie> findAll();

    public void create(Partie joueur);

    public void delete(int id);
}
