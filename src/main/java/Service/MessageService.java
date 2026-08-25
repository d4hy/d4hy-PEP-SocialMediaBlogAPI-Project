package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
  private MessageDAO messageDAO;
  private AccountDAO accountDAO;

  public MessageService() {
    this.messageDAO = new MessageDAO();
    this.accountDAO = new AccountDAO();
  }

  public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
    this.messageDAO = messageDAO;
    this.accountDAO = accountDAO;
  }

  public Message addMessage(Message message) {
    if (message == null ||
        message.getMessage_text().isEmpty() ||
        message.getMessage_text().length() > 255 ) {
          throw new IllegalArgumentException("Invalid message text");
    }
    if(accountDAO.getAccountById(message.getPosted_by()) == null){

      throw new IllegalArgumentException("Account does not exist");
    }
    return messageDAO.insertMessage(message);
  }

}
