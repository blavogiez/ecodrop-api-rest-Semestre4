package model.dao;

import model.dto.CollectionPoint;

import java.util.List;

public interface CollectionPointDAO {
    public List<CollectionPoint> findAll();

    public CollectionPoint findById(int id);

    public boolean add(CollectionPoint collectionPoint);

    public boolean delete(int id);
}
