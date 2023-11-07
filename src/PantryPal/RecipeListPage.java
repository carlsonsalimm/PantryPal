package PantryPal;

import java.io.IOException;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;

/*
 * Header for RecipeListPage
 * Has add button UI that redirects to SpecifyIngredientsPage
 */
class RecipeListHeader extends VBox {
    private Button addButton;

    RecipeListHeader() {
        this.setPrefSize(500, 100); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;");

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

class RecipeItem extends HBox {
    public Recipe recipe;
    private TextField recipeName;
    private Button detailedViewButton;

    RecipeItem(Recipe recipe) {
        this.recipe = recipe;
        this.setPrefSize(500, 40); // sets size of Recipe
        this.setStyle("-fx-background-color: #BCBCBC; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background
                                                                                                     // color of Recipe

        recipeName = new TextField(); // create RecipeItem name text field
        recipeName.setPrefSize(140, 20); // set size of text field
        recipeName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        recipeName.setText(this.recipe.getTitle());
        recipeName.setPadding(new Insets(20, 0, 20, 10)); // adds some padding to the text field
        this.getChildren().add(recipeName); // add textlabel to Recipe

        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        detailedViewButton = new Button("View"); // text displayed on button
        detailedViewButton.setStyle(defaultButtonStyle); // styling the button
        this.getChildren().add(detailedViewButton);
        // viewButton.setAlignment(Pos.CENTER_RIGHT);
        detailedViewButton.setPadding(new Insets(20, 0, 20, 80));

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

        this.setSpacing(5);
        this.setPrefSize(500, 560);
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
