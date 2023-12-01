package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import org.json.Cookie;

import javafx.event.ActionEvent;

class MockLoginPageController implements LoginControllerInterface {
    private Model model;

    String username;
    String password;

    MockLoginPageController(Model model) {
        this.model = model;
    }

    public void setUsername(String user) {
        this.username = user;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public boolean handleSignInButton(ActionEvent event) throws IOException {

        // Has Login Information
        if (model.performRequest("POST", "login", username, password, null, null, null, null, null).equals("true")) {
            model.setUsername(username);
            model.setPassword(password);
            return true;
        } else {
            return false;
        }

    }

    public boolean handleCreateAccountButton(ActionEvent event) throws IOException {

        // If Account Doesn Not Exist
        if (model.performRequest("POST", "signup", username, password, null, null, null, null, null).equals("true")) {
            model.setUsername(username);
            model.setPassword(password);
            return true;
        }

        // If Account Exxists
        else {
            return false;
        }
    }

}
