package Controller;

import Exception.DuplicateUsernameException;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);

        return app;
    }

    private void postAccountHandler(Context context) {
        try {
            Account requestedAccount = context.bodyAsClass(Account.class);
            Account registeredAccount = accountService.addAccount(requestedAccount);

            context.status(200);
            context.json(registeredAccount);

        } catch (DuplicateUsernameException e) {
            context.status(400);

        } catch (IllegalArgumentException e) {
            context.status(400);
        }
    }

    private void postLoginHandler(Context context) {
        Account requestedAccount = context.bodyAsClass(Account.class);
        Account account = accountService.getAccount(requestedAccount);

        if (account != null) {
            context.status(200);
            context.json(account);
        } else {
            context.status(401);
        }
    }

    private void postMessageHandler(Context context) {
        try {
            Message requestedMessage = context.bodyAsClass(Message.class);
            Message addedMessage = messageService.addMessage(requestedMessage);

            context.status(200);
            context.json(addedMessage);

        } catch (IllegalArgumentException e) {
            context.status(400);
        }
    }

    private void getAllMessagesHandler(Context context) {
        context.status(200);
        context.json(messageService.getAllMessages());
    }

    private void getMessageByIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        context.status(200);
        if (message != null) {
            context.json(message);
        }
    }

    private void deleteMessageByIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);

        context.status(200);
        if (deletedMessage != null) {
            context.json(deletedMessage);
        }
    }

    private void updateMessageHandler(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message requestedMessage = context.bodyAsClass(Message.class);
            Message updatedMessage = messageService.updateMessageText(
                    messageId,
                    requestedMessage);

            context.status(200);
            context.json(updatedMessage);

        } catch (IllegalArgumentException e) {
            context.status(400);
        }
    }

    private void getMessagesByAccountIdHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));

        context.status(200);
        context.json(messageService.getMessagesByAccountId(accountId));
    }
}
