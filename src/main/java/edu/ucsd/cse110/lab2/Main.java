package edu.ucsd.cse110.lab2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

    static Stage primaryStage;
    static BorderPane root;
    static RecipeListPage temp;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Main.primaryStage = primaryStage;
        // Setting the Layout of the Window- Should contain a Header, Footer and the
        // RecipeList
        // AppFrame root = new AppFrame();
        Recipe testRecipe = new Recipe("blah", "daowoawdo1");
        RecipeListPage root = new RecipeListPage();
        DetailedRecipePage temp = new DetailedRecipePage(new Recipe("a", "b"));

        

        Main.root = root;
        Main.temp = root;
        // Set the title of the app
        primaryStage.setTitle("Recipe List");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 600, 700));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void setPage(BorderPane page) {
        primaryStage.getScene().setRoot(page);
    }

    public static void callLoadMethod() {
        temp.getRecipeList();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
