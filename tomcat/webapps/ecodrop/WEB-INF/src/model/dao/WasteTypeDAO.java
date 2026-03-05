package model.dao;

import java.util.List;

import model.dto.WasteType;

public interface WasteTypeDAO {
    public List<WasteType> findAll();

    public WasteType findById(int id);

    public boolean add(WasteType wasteType);

    public boolean delete(int id);

    public WasteType update(int targetId, WasteType wasteType);
}
