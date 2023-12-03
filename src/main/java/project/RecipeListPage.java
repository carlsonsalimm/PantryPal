package project;

import java.io.IOException;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/*
 * Header for RecipeListPage
 * Has add button UI that redirects to SpecifyIngredientsPage
 */
class RecipeListHeader extends VBox {
    private Label title;
    private Button addButton;
    private Button signOutButton;

    private BorderPane topPane;
    private StackPane titleContainer;
    private StackPane addContainer;
    private StackPane signOutContainer;

    private ObservableList<String> sortOptions;
    private final ComboBox<String> sortBox;
    private ObservableList<String> filterOptions;
    private final ComboBox<String> filterBox;

//     ObservableList<String> options = 
//     FXCollections.observableArrayList(
//         "Option 1",
//         "Option 2",
//         "Option 3"
//     );
// final ComboBox comboBox = new ComboBox(options);

    private BorderPane bottomPane;
    private StackPane sortContainer;
    private StackPane filterContainer;

    RecipeListHeader() {
        this.setPrefSize(600, 70); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;");
        topPane = new BorderPane();
        topPane.setPrefSize(565, 40); 

        title = new Label("My Recipes"); // Text of the Header
        title.setStyle("-fx-font-size: 24;-fx-font-weight: bold;");
        title.setPrefSize(350, 40); // sets size of Recipe
        title.setTextAlignment(TextAlignment.CENTER);

        String signOutButtonStyle = "-fx-background-radius: 10; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
        signOutButton = new Button("Sign Out"); // text displayed on add button
        signOutButton.setStyle(signOutButtonStyle); // styling the button

        String defaultButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
        addButton = new Button("+"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button

        titleContainer = new StackPane(title);
        StackPane.setMargin(title, new Insets(10, 0, 10, 22));

        signOutContainer = new StackPane(signOutButton);
        StackPane.setMargin(signOutButton, new Insets(10, 15, 10, 0));

        addContainer = new StackPane(addButton);
        StackPane.setMargin(addButton, new Insets(10, 33, 10, 0));

        topPane.setLeft(titleContainer);
        topPane.setCenter(signOutContainer);
        topPane.setRight(addContainer);

        // Bottom Pane Set up
        bottomPane = new BorderPane();
        bottomPane.setPrefSize(565, 40);
    
        sortOptions = FXCollections.observableArrayList("A-Z","Z-A","Newest first","Oldest first");
        sortBox = new ComboBox<String>(sortOptions);
        sortBox.setPromptText("Sort By");
        sortBox.setPrefWidth(85);
        sortBox.setStyle("-fx-background-color: #FFFFFF");
        filterOptions = FXCollections.observableArrayList("All","Breakfast","Lunch","Dinner");
        filterBox = new ComboBox<String>(filterOptions);
        filterBox.setPromptText("Filter By");
        filterBox.setPrefWidth(85);
        filterBox.setStyle("-fx-background-color: #FFFFFF");

        sortContainer = new StackPane(sortBox);
        StackPane.setMargin(sortBox, new Insets(10, 0, 10, 22));
        filterContainer = new StackPane(filterBox);
        StackPane.setMargin(filterBox, new Insets(10, 300, 10, 0));


        bottomPane.setLeft(sortContainer);
        bottomPane.setCenter(filterContainer);

        this.getChildren().addAll(topPane,bottomPane);
        this.setAlignment(Pos.CENTER);
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getSignOutButton() {
        return signOutButton;
    }

    public Label getTitle() {
        return title;
    }
    
}

class RecipeItem extends HBox {
    public Recipe recipe;
    private Label recipeName;
    private Button detailedViewButton;
    private BorderPane pane;
    private StackPane viewContainer;

    RecipeItem() {
        pane = new BorderPane();
        pane.setPrefSize(550, 40); // sets size of Recipe
        pane.setStyle(" -fx-background-color:E1EAF3; -fx-background-radius: 5; -fx-font-weight: bold;"); // sets
    }

    public void setRecipe(Recipe recipe){

        this.recipe = recipe;
        recipeName = new Label(this.recipe.getTitle()); // create RecipeItem name text field
        recipeName.setPrefSize(475, 40); // sets size of Recipe
        recipeName.setPadding(new Insets(0, 10, 0, 20));
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

        detailedViewButton.setOnAction(event -> {
            // Send recipe data to RecipeListPage
        });
    }

    public Button getDetailedViewButton() {
        return detailedViewButton;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}


class RecipeList extends VBox {
    public List<Recipe> recipes;

    RecipeList() throws IOException {
        loadRecipe();
    }

    public void loadRecipe() throws IOException {
        // TO-DO: Replace with GET request
        this.recipes = CSVHandler.readRecipes();

        this.setSpacing(7);
        this.setPadding(new Insets(10, 0, 30, 0));
        // this.setPrefSize(600, 560);
        this.setStyle("-fx-background-color: #FFFFFF;");

        for (Recipe recipe : recipes) {
            RecipeItem Item = new RecipeItem();
            Item.setRecipe(recipe);
            this.getChildren().add(Item);
        }
    }

}

public class RecipeListPage extends BorderPane {
    private RecipeListHeader header;

    private Button detailedViewButton;
    private Button addButton;
    private Button signOutButton;

    private RecipeItem recipeItem;
    private Recipe recipe;

    private RecipeList recipeList;

    RecipeListPage() throws IOException {
        header = new RecipeListHeader();
        addButton = header.getAddButton();
        signOutButton = header.getSignOutButton();

        recipeItem = new RecipeItem();
        detailedViewButton = recipeItem.getDetailedViewButton();

        // Create a RecipeList Object to hold the Recipes
        recipeList = new RecipeList();

        ScrollPane scroller = new ScrollPane(recipeList);
        scroller.setFitToHeight(isCache());
        scroller.setFitToWidth(true);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(scroller);

    }

    public void setRecipe(Recipe recipe){
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public RecipeList getRecipeList() {
        return this.recipeList;
    }

    public void setAddButtonAction(EventHandler<ActionEvent> eventHandler){
        addButton.setOnAction(eventHandler);
    }

    public void setSignOutButtonAction(EventHandler<ActionEvent> eventHandler){
        signOutButton.setOnAction(eventHandler);
    }

    public void setDetailedViewButtonAction(EventHandler<ActionEvent> eventHandler){
        detailedViewButton.setOnAction(eventHandler);
    }
    
}
