package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {

    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = null;
            statement.setString(1, account.username);
            statement.setString(2, account.password);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0) {
                return null;
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return new Account(
                        generatedKeys.getInt(1),
                        account.username,
                        account.password);
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT account_id, username, password FROM account WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = null;

            statement.setString(1, username);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapAccount(resultSet);
            }

            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return null;
    }

    public Account getAccountByUsernameAndPassword(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT account_id, username, password " +
                    "FROM account WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = null;
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapAccount(resultSet);
            }

            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return null;
    }

    private Account mapAccount(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getInt("account_id"),
                resultSet.getString("username"),
                resultSet.getString("password"));
    }
    public Account getAccountById(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
    
        String sql =
                "SELECT account_id, username, password " +
                "FROM account WHERE account_id = ?";
    
        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
    
            statement.setInt(1, accountId);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapAccount(resultSet);
                }
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    
        return null;
    }
}
