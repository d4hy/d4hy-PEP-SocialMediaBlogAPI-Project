package Service;

import DAO.AccountDAO;
import Exception.DuplicateUsernameException;
import Model.Account;

import java.sql.SQLException;

public class AccountService {
  private AccountDAO accountDAO;

  public AccountService() {
    accountDAO = new AccountDAO();
  }

  public AccountService(AccountDAO accountDAO) {
    this.accountDAO = accountDAO;
  }

  public Account addAccount(Account account)  {
    if (account == null
        || account.getUsername() == null
        || account.getUsername().trim().isEmpty()) {
      throw new IllegalArgumentException("Invalid account");
    }
    if (account.password == null || account.password.length() < 4) {
      throw new IllegalArgumentException(
          "Password must be at least 4 characters");
    }

    if (accountDAO.getAccountByUsername(account.username) != null) {
      throw new DuplicateUsernameException();
    }

    return accountDAO.insertAccount(account);
  }
  public Account getAccount(Account requestedAccount) {
    if (requestedAccount == null
            || requestedAccount.getUsername() == null
            || requestedAccount.getPassword() == null) {
        return null;
    }

    return accountDAO.getAccountByUsernameAndPassword(
            requestedAccount.getUsername(),
            requestedAccount.getPassword()
    );
}
  

}
