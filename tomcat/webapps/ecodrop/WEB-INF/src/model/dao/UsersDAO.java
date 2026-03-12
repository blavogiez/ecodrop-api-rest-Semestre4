package model.dao;

import java.util.List;

import model.dto.Users;

public interface UsersDAO {
    List<Users> findAll();

    Users findById(int id);

    Users update(int id, Users users);
}
