package model.dao;

public interface AuthDAO {
    boolean isAdmin(String username);

    boolean credentialsReferToExistingAccount(String username, String password);
}
