package project;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.FileReader;
import java.io.IOException;
import java.lang.ModuleLayer.Controller;

public class Main extends Application {

    static Stage primaryStage;
    static VBox root;
    static RecipeListPage temp;

    @Override
    public void start(Stage primaryStage) throws Exception, IOException {

        Main.primaryStage = primaryStage;
        LoginPage root = new LoginPage();
        //RecipeListPage root = new RecipeListPage();
        

        Main.root = root;
        Model model = new Model();

        // Set Controller
        LoginPageController controller = new LoginPageController(root, model);
        
        //primaryStage.getIcons().add(new Image("./icons/recipe.png"));
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