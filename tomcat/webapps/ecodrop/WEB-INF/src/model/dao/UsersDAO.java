package model.dao;

import model.dto.Users;

import java.util.List;

public interface UsersDAO {
    List<Users> findAll();
    Users findById(int id);
    Users update(Users users);
}
