package model.dao;

import model.dto.Deposit;
import model.dto.DepositView;

import java.util.List;

public interface DepositDAO {
    List<Deposit> findAll();
    List<DepositView> findAllEnriched();
    Deposit findById(int id);
    boolean add(Deposit deposit);
    Deposit update(int id, Deposit deposit);
}