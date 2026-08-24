package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {

    public Account insertAccount(Account account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        PreparedStatement statement =
                connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ResultSet generatedKeys = null;

        try {
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
                        account.password
                );
            }

            return null;
        } finally {
            if (generatedKeys != null) {
                generatedKeys.close();
            }
            statement.close();
        }
    }

    public Account getAccountByUsername(String username) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql =
                "SELECT account_id, username, password FROM account WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = null;

        try {
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapAccount(resultSet);
            }

            return null;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            statement.close();
        }
    }

    public Account getAccountByUsernameAndPassword(String username, String password)
            throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT account_id, username, password " +
                "FROM account WHERE username = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = null;

        try {
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapAccount(resultSet);
            }

            return null;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            statement.close();
        }
    }

    private Account mapAccount(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getInt("account_id"),
                resultSet.getString("username"),
                resultSet.getString("password")
        );
    }
}
