package project;
import java.io.IOException;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;

/*
 * Header for RecipeListPage
 * Has add button UI that redirects to SpecifyIngredientsPage
 */
class RecipeListHeader extends HBox {
    private Label title;
    private Button addButton;
    private BorderPane pane;
    private StackPane titleContainer;
    private StackPane addContainer;

    RecipeListHeader() {
        this.setPrefSize(600, 70); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;");
        pane = new BorderPane();
        pane.setPrefSize(565, 40); // sets size of Recipe

        title = new Label("My Recipes"); // Text of the Header
        
        title.setStyle("-fx-font-size: 20;-fx-font-weight: bold;");
        title.setPrefSize(475, 40); // sets size of Recipe
        title.setTextAlignment(TextAlignment.CENTER);

        String defaultButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
        addButton = new Button("+"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button

        titleContainer = new StackPane(title);
        StackPane.setMargin(title, new Insets(0, 10, 0, 0));

        addContainer = new StackPane(addButton);
        StackPane.setMargin(addButton, new Insets(0, 33, 0, 0));

        pane.setLeft(titleContainer);
        pane.setRight(addContainer);
        this.getChildren().add(pane);
        this.setAlignment(Pos.CENTER);
    }

    public Button getAddButton() {
        return addButton;
    }
}

class RecipeItem extends HBox {
    public Recipe recipe;
    private Label recipeName;
    private Button detailedViewButton;
    private BorderPane pane;
    private StackPane viewContainer;

    RecipeItem(Recipe recipe) {
        this.recipe = recipe;
        pane = new BorderPane();
        pane.setPrefSize(550, 40); // sets size of Recipe
        pane.setStyle(" -fx-background-color:E1EAF3; -fx-background-radius: 5; -fx-font-weight: bold;"); // sets background


        recipeName = new Label(this.recipe.getTitle()); // create RecipeItem name text field
        recipeName.setPrefSize(475, 40); // sets size of Recipe
        recipeName.setPadding(new Insets(0,10,0,20));
        recipeName.setTextAlignment(TextAlignment.CENTER);
        

        String defaultButtonStyle = "-fx-background-radius: 5; -fx-font-style: italic; -fx-background-color: #D9D9D9; -fx-font-weight: bold; -fx-font: 11 arial; -fx-top-spacing: 5;";
        detailedViewButton = new Button("View"); // text displayed on button
        detailedViewButton.setStyle(defaultButtonStyle); // styling the button
        detailedViewButton.setPrefSize(50, 30); // sets size of Recipe
        detailedViewButton.setTextAlignment(TextAlignment.CENTER);
       
        viewContainer = new StackPane(detailedViewButton);
        StackPane.setMargin(detailedViewButton, new Insets(0, 10, 0, 0));

        pane.setLeft(recipeName);
        pane.setRight(viewContainer);
        this.getChildren().add(pane);
        this.setAlignment(Pos.CENTER);


        detailedViewButton.setOnAction(e -> {
            Main.setPage(new DetailedRecipePage(recipe));
        });
    }
}

class RecipeList extends VBox {
    public List<Recipe> recipes;

    RecipeList() throws IOException {
        loadRecipe();
    }

    private void loadRecipe() throws IOException{
        recipes = CSVHandler.readRecipes();

        this.setSpacing(7);
        this.setPadding(new Insets(10,0,30,0));
        //this.setPrefSize(600, 560);
        this.setStyle("-fx-background-color: #FFFFFF;");

        for (Recipe recipe : recipes) {
            RecipeItem Item = new RecipeItem(recipe);
            this.getChildren().add(Item);
        }
    }

}

public class RecipeListPage extends BorderPane {
    private RecipeListHeader header;
    private Button addButton;
    private RecipeList recipeList;

    RecipeListPage() throws IOException {
        header = new RecipeListHeader();
        addButton = header.getAddButton();
        recipeList = new RecipeList();
        // Initialise the header Object
        header = new RecipeListHeader();

        // Create a RecipeList Object to hold the Recipes
        recipeList = new RecipeList();

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
    }

    public void addListeners() {
        addButton.setOnAction(e -> {
            Main.setPage(new SpecifyIngredientPage());
        });
    }

    public RecipeList getRecipeList() {
        return recipeList;
    }
}
