package Service;

import DAO.AccountDAO;
import Exception.DuplicateUsernameException;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) {
        if (account == null
                || account.getUsername() == null
                || account.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid account");
        }

        if (account.getPassword() == null
                || account.getPassword().length() < 4) {
            throw new IllegalArgumentException(
                    "Password must be at least 4 characters");
        }

        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            throw new DuplicateUsernameException();
        }

        Account addedAccount = accountDAO.insertAccount(account);
        if (addedAccount == null) {
            throw new IllegalArgumentException("Account could not be added");
        }

        return addedAccount;
    }

    public Account getAccount(Account requestedAccount) {
        if (requestedAccount == null
                || requestedAccount.getUsername() == null
                || requestedAccount.getPassword() == null) {
            return null;
        }

        return accountDAO.getAccountByUsernameAndPassword(
                requestedAccount.getUsername(),
                requestedAccount.getPassword());
    }
}
