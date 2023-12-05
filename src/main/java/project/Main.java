package project;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
        Recipe mock = new Recipe("title test", "instruction test", "test", "123");
        List<Recipe> recipes = new ArrayList<Recipe>();
        Main.root = new RecipeListPage(recipes);
        
        Main.controller = new RecipeListPageController((RecipeListPage) root, model);

        // Main.root = new DetailedRecipePage(mock);
        // Main.controller = new DetailedRecipePageController((DetailedRecipePage) root, model);
       
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

    // public static void callLoadMethod() {
    //     temp.getRecipeList();
    // }

    public static void main(String[] args) {
        launch(args);
    }
}