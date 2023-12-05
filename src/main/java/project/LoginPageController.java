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
    private List<Recipe> fullRecipe;

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

        if(model.performRequest("POST", "login", username, password, null, null, null, null, null, null, null).equals("true")){
            model.setUsername(username);
            model.setPassword(password);

            
            String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null, null);
            List<Recipe> recipes = extractRecipeInfo(convertStringToRecipeList(JSON));
            RecipeListPage view = new RecipeListPage(recipes);
            Main.setPage(view);
            Main.setController(new RecipeListPageController(view, model));
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
    }
    //helper method to store recipeList as a List of recipes
    public List<Recipe> extractRecipeInfo(List<Document> recipeList) {
        for(Document recipe: recipeList){
            String recipeTitle = recipe.getString("recipeTitle");
            String mealType = recipe.getString("mealType");
            String ingredients = recipe.getString("ingredients");
            String instructions = recipe.getString("instructions");
            Long creationTime = recipe.getLong("creationTime");
            // Create a Recipe object and add it to the fullRecipe list
            Recipe recipe1 = new Recipe(recipeTitle, mealType, ingredients, instructions, String.valueOf(creationTime));
            fullRecipe.add(recipe1);
        }
        return fullRecipe;  //fullRecipe will now contain all the recipeList  
    }

    public List<Document> convertStringToRecipeList(String jsonString) {
        List<Document> recipeList = new ArrayList<>();

        // Parse the JSON string and convert it to a list of documents
        List<Document> documents = (List<Document>) Document.parse(jsonString);

        // Add each document to the recipe list
        for (Document document : documents) {
            recipeList.add(document);
        }

        return recipeList;
    }

}
