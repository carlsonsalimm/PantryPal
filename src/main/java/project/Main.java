package project;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;


public class Main extends Application {

    static Stage primaryStage;
    static Parent root;
    static DetailedRecipePage temp;
    static Controller controller;
    static Model model;


    @Override
    public void start(Stage primaryStage) throws Exception, IOException {
        try { 
        Main.model = new Model();
        Main.primaryStage = primaryStage;
        //Recipe mock = new Recipe("title test", "instruction test", "test", "123");
        //List<Recipe> recipes = new ArrayList<Recipe>();
        //Main.root = new RecipeListPage(recipes);
        Main.root = new LoginPage();
        Main.controller = new LoginPageController((LoginPage) root, model);
        //Main.controller = new RecipeListPageController((RecipeListPage) root, model);
        // Recipe mock = new Recipe("title test", "instruction test", "test", "123");
        // Main.root = new DetailedRecipePage(mock, true);
        // Main.controller = new DetailedRecipePageController((DetailedRecipePage) root, model);

        FileReader file = new FileReader("RememberMe.csv");
        BufferedReader br =new BufferedReader(file);
        if(br.readLine().equals("1")){
            System.out.println("Login Remembered");
            model.setUsername(br.readLine());
            model.setPassword(br.readLine());
            String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null,null, null);
            List<Recipe> recipes = Main.extractRecipeInfo(JSON);
            RecipeListPage listPage = new RecipeListPage(recipes);
            Main.root = listPage;
            Main.setController(new RecipeListPageController(listPage, model));
        }
        file.close();
        br.close();
       
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 600, 700));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    } catch (Exception e) {
        Main.showAlert("Error", "Server temporarily unavailable. Please try again later."); 
    }
    }

    public static void setPage(Parent page) {
        primaryStage.getScene().setRoot(page);
    }

    public static void setController(Controller controller){
        Main.controller = controller;
    }

    //helper method to store recipeList as a List of recipes
    public static List<Recipe> extractRecipeInfo(String jsonString) {
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
            Recipe newRecipe = new Recipe(recipeTitle, instructions, ingredients, mealType, String.valueOf(creationTime));
            list.add(newRecipe);
        }
        return list;  // returns list with all recipes parsed from given JSON string
    }

    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}