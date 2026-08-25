package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message addMessage(Message message) {
        if (message == null
                || message.getMessage_text() == null
                || message.getMessage_text().trim().isEmpty()
                || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Invalid message text");
        }

        if (accountDAO.getAccountById(message.getPosted_by()) == null) {
            throw new IllegalArgumentException("Account does not exist");
        }

        Message addedMessage = messageDAO.insertMessage(message);
        if (addedMessage == null) {
            throw new IllegalArgumentException("Message could not be added");
        }

        return addedMessage;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessageText(int messageId, Message message) {
        if (message == null
                || message.getMessage_text() == null
                || message.getMessage_text().trim().isEmpty()
                || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Invalid message text");
        }

        Message updatedMessage = messageDAO.updateMessageText(
                messageId,
                message.getMessage_text());

        if (updatedMessage == null) {
            throw new IllegalArgumentException("Message does not exist");
        }

        return updatedMessage;
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }
}
