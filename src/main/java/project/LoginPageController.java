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

public class LoginPageController implements LoginControllerInterface {
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
        String username = view.getUsername();
        String password = view.getPassword();

        if (model.performRequest("POST", "login", username, password, null, null, null, null, null, null)
                .equals("true")) {
            model.setUsername(username);
            model.setPassword(password);
            view.goToRecipeListPage();

            if (view.getRememberMe()) {
                FileWriter file = new FileWriter("RememberMe.csv");
                try {
                    file.write(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                file.close();
            }

            return true;
        } else {
            view.showAlert("Error", "Account Not Found");
            return false;
        }

    }

    public boolean handleCreateAccountButton(ActionEvent event) throws IOException {
        String username = view.getUsername();
        String password = view.getPassword();

        // If Account Doesn Not Exist
        if (model.performRequest("POST", "signup", username, password, null, null, null, null, null, null)
                .equals("true")) {
            model.setUsername(username);
            model.setPassword(password);
            view.goToRecipeListPage();
            return true;
        }

        // If Account Exxists
        else {
            view.showAlert("Error", "Account Already Exist");
            return false;
        }
    }
    //helper method to store recipeList as a List of recipes
    public List<Recipe> extractRecipeInfo(String jsonString) {
        ArrayList<Recipe> list = new ArrayList<Recipe>();
        Document jsonRecipes = Document.parse(jsonString);
        for (int i = 0; i < jsonRecipes.size(); i++) {
            Document recipe = (Document) (jsonRecipes.get(String.valueOf(i)));
            String recipeTitle = recipe.getString("recipeTitle");
            String mealType = recipe.getString("mealType");
            String ingredients = recipe.getString("ingredients");
            String instructions = recipe.getString("instructions");
            Long creationTime = recipe.getLong("creationTime");
            // Create a Recipe object and add it to the list
            // Recipe newRecipe = new Recipe(recipeTitle, ingredients + instructions);
            Recipe newRecipe = new Recipe(recipeTitle, mealType, ingredients, instructions, String.valueOf(creationTime));
            list.add(newRecipe);
        }
        return list;  // returns list with all recipes parsed from given JSON string
       
    }

}
