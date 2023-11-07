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

class Recipe extends HBox {


    // Recipe Information
    private TextField RecipeName;

    // Task: add user information to Recipe()
    Recipe() {
        this.setPrefSize(500, 40); // sets size of Recipe
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of Recipe
        
        RecipeName = new TextField(); // create Recipe name text field
        RecipeName.setPrefSize(140, 20); // set size of text field
        RecipeName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
       
        RecipeName.setPadding(new Insets(20, 0, 20, 10)); // adds some padding to the text field
        this.getChildren().add(RecipeName); // add textlabel to Recipe
         
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
        FileReader file; // Contents of file
        try {
 
            file = new FileReader("Recipes.csv");
 
            BufferedReader buffer = new BufferedReader(file, 16384);
 
            String line = ""; // Reads line by line of file
            try{line = buffer.readLine();
            }catch(Exception e){}

    
            while(line != null){ // Sets a new task per line
                Recipe recipe = new Recipe();
                recipe.getRecipeName().setText(line);
                this.getChildren().add(recipe);
                try{line = buffer.readLine();}
                catch(Exception e){}  
            }
            
        try{
            buffer.close();
            file.close();
            } catch(Exception e){}
        }
         catch (FileNotFoundException e) {}
    }

}

class Header extends VBox {

    private Button addButton;
    private Button createRecipeButton;

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

        //create recipe button
        createRecipeButton = new Button("New Recipe"); // text displayed on add button
        createRecipeButton.setStyle(defaultButtonStyle); // styling the button
        this.getChildren().add(createRecipeButton);
        createRecipeButton.setAlignment(Pos.TOP_RIGHT);
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getCreateRecipeButton() {
        return createRecipeButton;
    }
}


/*
 *  APP GUI
 */
class AppFrame extends BorderPane{

    private Header header;
    private recipeList recipeList;
    private Button addButton;
    private Button createRecipeButton;
   

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

        // Initialise Button Variables through the getters in Footer
        createRecipeButton = header.getCreateRecipeButton();
      
        // Call Event Listeners for the Buttons
        addListeners();

        recipeList.loadRecipes();
    }

    public void addListeners()
    {

        // Add button functionality
        addButton.setOnAction(e -> {
            // Create a new Recipe
            Recipe Recipe = new Recipe();
            // Add Recipe to recipeList
            recipeList.getChildren().add(Recipe);
            // New window appears and makes new page
            // ViewFrame() should populate with Recipe object data
            
        });

        createRecipeButton.setOnAction(e -> {
            try {
                // Create a new Recipe
                new SpecifyIngredientPage().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the recipeList
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("Recipe List");
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
