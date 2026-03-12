package model.dao;

import model.dto.Deposit;

import java.util.List;

public interface DepositDAO {
    List<Deposit> findAll();
    Deposit findById(int id);
    boolean add(Deposit deposit);
    Deposit update(int id, Deposit deposit);
}