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


public class Main extends Application {

    static Stage primaryStage;
    static Parent root;
    static RecipeListPage temp;
    static Controller controller;
    static Model model;

    @Override
    public void start(Stage primaryStage) throws Exception, IOException {
        Main.model = new Model();
        Main.primaryStage = primaryStage;
        Main.root = new LoginPage();
        
        Main.controller = new LoginPageController((LoginPage) root, model);
       
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

    public static void callLoadMethod() {
        temp.getRecipeList();
    }

    public static void main(String[] args) {
        launch(args);
    }
}