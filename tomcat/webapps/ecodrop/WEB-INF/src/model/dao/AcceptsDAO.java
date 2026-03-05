package model.dao;

import model.dto.Accepts;
import java.util.List;

public interface AcceptsDAO {
    public List<Accepts> findAll();

    public Accepts findById(int id);

    public boolean add(Accepts accepts);

    public boolean delete(Accepts accepts);
}
