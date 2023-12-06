package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.json.Cookie;
import javafx.event.ActionEvent;

public class LoginPageController implements Controller{
    private LoginPage view;
    private Model model;

    LoginPageController(LoginPage view, Model model) {
        this.view = view;
        this.model = model;

        this.view.setSignInButtonAction(event -> {
            try {
                handleSignInButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        this.view.setCreateAccountButtonAction(event -> {
            try {
                handleCreateAccountButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public boolean handleSignInButton(ActionEvent event) throws IOException {
        try {
            String username = view.getUsername();
            String password = view.getPassword();

            if (model.performRequest("POST", "login", username, password, null, null, null, null, null, null, null)
                    .equals("true")) {
                model.setUsername(username);
                model.setPassword(password);
                if(view.getRememberMe()){
                    FileWriter file = new FileWriter("RememberMe.csv");
                    file.write("1\n" +username+"\n"+password);
                    file.close();
                }

                
                String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null,null);
                List<Recipe> recipes = Main.extractRecipeInfo(JSON);
                RecipeListPage listPage = new RecipeListPage(recipes);
                Main.setPage(listPage);
                Main.setController(new RecipeListPageController(listPage, model));

                return true;
            } else {
                view.showAlert("Error", "Account Not Found");
                return false;
            }                 
        } catch (Exception e) {
             Main.showAlert("Error", "Server temporarily unavailable. Please try again later."); 
             return false;
        }
    }

    public boolean handleCreateAccountButton(ActionEvent event) throws IOException {
        try {
        String username = view.getUsername();
        String password = view.getPassword();

        // If Account Doesn Not Exist
        if (model.performRequest("POST", "signup", username, password, null, null, null, null, null, null, null)
                .equals("true")) {
            model.setUsername(username);
            model.setPassword(password);

            List<Recipe> recipes = new ArrayList<Recipe>();
            RecipeListPage temp = new RecipeListPage(recipes);
            Main.setPage(temp);
            Main.setController(new RecipeListPageController(temp,model));
            return true;
        }

        // If Account Exxists
        else {
            view.showAlert("Error", "Account Already Exist");
            return false;
        }
        } catch (Exception e) {
            Main.showAlert("Error", "Server temporarily unavailable. Please try again later."); 
            return false;
        }
    }
    

}
