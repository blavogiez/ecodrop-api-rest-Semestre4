package model.dao;

public interface TokenDAO {
    String getOrCreateToken(String login, String password);
}
