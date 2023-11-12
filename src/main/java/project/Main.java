package project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

    static Stage primaryStage;
    static BorderPane root;
    static RecipeListPage temp;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Main.primaryStage = primaryStage;
        RecipeListPage root = new RecipeListPage();

        

        Main.root = root;
        Main.temp = root;
        // Set the title of the app
        primaryStage.initStyle(StageStyle.UNIFIED);
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
