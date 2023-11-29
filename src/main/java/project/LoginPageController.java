package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import org.json.Cookie;

import javafx.event.ActionEvent;

public class LoginPageController {
    private LoginPage view;
    private Model model;

    public LoginPageController(LoginPage view ,Model model){
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

        if(model.performRequest("GET", username, password).equals("true")){
            view.goToRecipeListPage();
        }
        else{
            view.showAlert("Error", "Account Not Found");
        }

        // Remember Me
        if(view.getRememberMe()){
            FileWriter file = new FileWriter("RememberMe.csv");

            try {
                file.write(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            file.close();
        }
    }

    private void handleCreateAccountButton(ActionEvent event){
        String username = view.getUsername();
        String password = view.getPassword();

        if(model.performRequest("GET", username, password).equals("false")){
            model.performRequest("PUT", username, password);
            view.goToRecipeListPage();
        }
        else{
            view.showAlert("Error", "Account Already Exist");
        }
    }

   
}
