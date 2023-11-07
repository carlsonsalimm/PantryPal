package recipeList;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import java.util.Comparator;

import java.io.File;

class RecipeItem extends HBox {


    // RecipeItem Information
    private TextField RecipeName;
    private Button viewButton;
    // Task: add user information to RecipeItem()
    RecipeItem() {
        this.setPrefSize(500, 40); // sets size of Recipe
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of Recipe
        
        RecipeName = new TextField(); // create RecipeItem name text field
        RecipeName.setPrefSize(140, 20); // set size of text field
        RecipeName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
       
        RecipeName.setPadding(new Insets(20, 0, 20, 10)); // adds some padding to the text field
        this.getChildren().add(RecipeName); // add textlabel to Recipe
         
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        viewButton = new Button("View"); // text displayed on button
        viewButton.setStyle(defaultButtonStyle); // styling the button
        this.getChildren().add(viewButton);
        //viewButton.setAlignment(Pos.CENTER_RIGHT);
        viewButton.setPadding(new Insets(20,0,20,80));

        viewButton.setOnAction(e -> {

            Stage prompt = new Stage();

            // CHANGE TO THEIR STAGE
            AppFrame root = new AppFrame();

            
            // Create scene of mentioned size with the border pane
            prompt.setScene(new Scene(root, 600, 700));
            
           
            
        });
    }

    public TextField getRecipeName() {
        return this.RecipeName;
    }

}

class recipeList extends VBox {

    recipeList() {
        this.setSpacing(5); // sets spacing between Recipes
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void loadRecipes() {

        for(int i = 0; i < this.getChildren().size() ; i++){
            this.getChildren().removeIf(recipe -> recipe instanceof RecipeItem);
        }

        for(recipe : recipe.getRecipeName()){
            RecipeItem Item = new RecipeItem();
            recipe.getRecipeName().setText(recipe);
            this.getChildren().add(Item);
        }
    }

}

class Header extends VBox {

    private Button addButton;

    Header() {
        this.setPrefSize(500, 100); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Recipe List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setPadding(new Insets(20, 0, 0, 20)); 
        this.setAlignment(Pos.BASELINE_LEFT); // Align the text to the Center
    
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("+"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button
        this.getChildren().add(addButton);
        addButton.setAlignment(Pos.TOP_RIGHT);
    }

    public Button getAddButton() {
        return addButton;
    }
}


/*
 *  APP GUI
 */
class AppFrame extends BorderPane{

    private Header header;
    private recipeList recipeList;
    private Button addButton;
    private Button viewButton;

    AppFrame()
    {
        // Initialise the header Object
        header = new Header();
        

        // Create a recipeList Object to hold the Recipes
        recipeList = new recipeList();

        ScrollPane scroller = new ScrollPane(recipeList);
        scroller.setFitToHeight(isCache());
        scroller.setFitToWidth(true);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scroller);
        
        // Add footer to the bottom of the BorderPane

        // Initialise Button Variables through the getters in Footer
        addButton = header.getAddButton();
        // Call Event Listeners for the Buttons
        addListeners();

        recipeList.loadRecipes();
    }

    public void addListeners()
    {

        // Add button functionality
        addButton.setOnAction(e -> {

            Stage prompt = new Stage();

            // CHANGE TO THEIR STAGE
            AppFrame root = new AppFrame();

            // Set the title of the app
            prompt.setTitle("Prompt");
            // Create scene of mentioned size with the border pane
            prompt.setScene(new Scene(root, 600, 700));
            // Make window non-resizable
            prompt.setResizable(false);
            // Show the app
            prompt.show();
            
        });
       
    }
}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the recipeList
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("RecipeItem List");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 600, 700));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
