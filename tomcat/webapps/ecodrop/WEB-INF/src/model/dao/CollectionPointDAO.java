package model.dao;

import java.util.List;

import model.dto.CollectionPoint;
import model.dto.WasteType;

public interface CollectionPointDAO {
    public List<CollectionPoint> findAll();

    public CollectionPoint findById(int id);

    public boolean add(CollectionPoint collectionPoint);

    public boolean delete(int id);

    public List<WasteType> getAcceptedWasteTypes(int collectionPointId);

    public CollectionPoint update(CollectionPoint updated);
}
