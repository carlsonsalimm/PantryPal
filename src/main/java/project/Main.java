package project;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
        Main.model = new Model();
        Main.primaryStage = primaryStage;
        //Recipe mock = new Recipe("title test", "instruction test", "test", "123");
        //List<Recipe> recipes = new ArrayList<Recipe>();
        //Main.root = new RecipeListPage(recipes);
        Main.root = new LoginPage();
        Main.controller = new LoginPageController((LoginPage) root, model);
        //Main.controller = new RecipeListPageController((RecipeListPage) root, model);
        
        FileReader file = new FileReader("RememberMe.csv");
        BufferedReader br =new BufferedReader(file);
        boolean result;
        if(result = br.readLine().equals("1")){
            System.out.println(result);
            model.setUsername(br.readLine());
            model.setPassword(br.readLine());
            String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null);
            //List<Recipe> recipes = Main.extractRecipeInfo(Main.convertStringToRecipeList(JSON));
            List<Recipe> recipes = new ArrayList<>();
            String a = "test";
            recipes.add(new Recipe(a, a, a, a));
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
    }

    public static void setPage(Parent page) {
        primaryStage.getScene().setRoot(page);
    }

    public static void setController(Controller controller){
        Main.controller = controller;
    }

    public static List<Recipe> extractRecipeInfo(List<Document> recipeList) {
        List<Recipe> fullRecipe = new ArrayList<Recipe>();
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

    public static List<Document> convertStringToRecipeList(String jsonString) {
        List<Document> recipeList = new ArrayList<>();

        // Parse the JSON string and convert it to a list of documents
        List<Document> documents = (List<Document>) Document.parse(jsonString);

        // Add each document to the recipe list
        for (Document document : documents) {
            recipeList.add(document);
        }

        return recipeList;
    }

    public static void main(String[] args) {
        launch(args);
    }
}