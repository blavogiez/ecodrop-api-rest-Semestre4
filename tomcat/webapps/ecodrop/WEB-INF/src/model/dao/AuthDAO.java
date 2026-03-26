package model.dao;

import model.Role;

public interface AuthDAO {
    // return un role ; ouverture à la modification future
    Role getRole(String username);

    boolean credentialsReferToExistingAccount(String username, String password);

    // on est tenté de faire un isUser mais en réalité un JWT valide signifie que la
    // requête est celle d'un utilisateur
    // boolean isUser(String username);
}
