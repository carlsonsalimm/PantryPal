package project;

import java.io.IOException;
import java.net.CookieHandler;

import org.json.Cookie;

import javafx.event.ActionEvent;

public class LoginPageController {
    private LoginPage view;
    //private Model model;

    public LoginPageController(LoginPage view /*,Model model*/ ){
        this.view = view;
        //this.model = model;

        this.view.setSignInButtonAction(event -> {
            try {
                handleSignInButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });


        this.view.setCreateAccountButtonAction(this::handleCreateAccountButton);
    }

    private void handleSignInButton(ActionEvent event) throws IOException{
        String username = view.getUsername();
        String password = view.getPassword();

        if(/*model.performRequest("GET", username, password)*/ null != null){
            view.goToRecipeListPage();
        }
        else{
            view.showAlert("Error", "Account Not Found");
        }

        // Remember Me
        if(view.getRememberMe()){
            Cookie rememberCookie = new Cookie("remember", model.performRequest("GET"));
        }
    }

    private void handleCreateAccountButton(ActionEvent event){
        String username = view.getUsername();
        String password = view.getPassword();

        if(model.performRequest("GET", username, password) == null){
            model.performRequest("PUT", username, password);
            view.goToRecipeListPage();
        }
        else{
            view.showAlert("Error", "Account Already Exist");
        }
    }

   
}
