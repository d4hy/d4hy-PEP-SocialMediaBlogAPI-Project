package Controller;

import Exception.DuplicateUsernameException;
import Model.Account;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    
        
    
        public SocialMediaController(){
            this.accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        return app;
    }
    private void postLoginHandler(Context context) {
        
            Account requestedAccount = context.bodyAsClass(Account.class);
            Account registeredAcc = accountService.getAccount(requestedAccount);
            if(registeredAcc != null){
                context.status(200);
                context.json(registeredAcc);
            } else{
                context.status(401);
            }
        
    }
    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context context) {
       
        try{
            Account requestedAccount = context.bodyAsClass(Account.class);
            Account registeredAcc = accountService.addAccount(requestedAccount);
            context.status(200);
            context.json(registeredAcc);

         } catch(DuplicateUsernameException e){
                context.status(409);
                context.result(e.getMessage());
            
        } catch( IllegalArgumentException e){
            context.status(400);
            context.result(e.getMessage());
        }
        
    }


}