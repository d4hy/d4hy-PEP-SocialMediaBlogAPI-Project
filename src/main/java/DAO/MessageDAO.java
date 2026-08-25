package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, message.getPosted_by());
            statement.setString(2, message.getMessage_text());
            statement.setLong(3, message.getTime_posted_epoch());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0) {
                return null;
            }

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return new Message(
                        generatedKeys.getInt(1),
                        message.getPosted_by(),
                        message.getMessage_text(),
                        message.getTime_posted_epoch());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch " +
                "FROM message ORDER BY message_id";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                messages.add(mapMessage(resultSet));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public Message getMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch " +
                "FROM message WHERE message_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, messageId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapMessage(resultSet);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message deleteMessageById(int messageId) {
        Message message = getMessageById(messageId);

        if (message == null) {
            return null;
        }

        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, messageId);

            if (statement.executeUpdate() > 0) {
                return message;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message updateMessageText(int messageId, String messageText) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, messageText);
            statement.setInt(2, messageId);

            if (statement.executeUpdate() > 0) {
                return getMessageById(messageId);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch " +
                "FROM message WHERE posted_by = ? ORDER BY message_id";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, accountId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                messages.add(mapMessage(resultSet));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    private Message mapMessage(ResultSet resultSet) throws SQLException {
        return new Message(
                resultSet.getInt("message_id"),
                resultSet.getInt("posted_by"),
                resultSet.getString("message_text"),
                resultSet.getLong("time_posted_epoch"));
    }
}
