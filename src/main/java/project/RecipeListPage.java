package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
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

    public ComboBox<String> getSortBox() {
        return sortBox;
    }
    
    public ComboBox<String> getFilterBox() {
        return filterBox;
    }
    
}

class RecipeItem extends VBox {
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

      

    }

    public Button getDetailedViewButton() {
        return detailedViewButton;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}

public class RecipeListPage extends BorderPane {
    private RecipeListHeader header;

    public Button detailedViewButton;
    private Button addButton;
    private Button signOutButton;

    private ComboBox<String> sortBox; 
    private ComboBox<String> filterBox; 

    public List<Recipe> recipes;

    public VBox vbox;

    public static Recipe recipe;

    RecipeListPage(List<Recipe> recipes) throws IOException {
        header = new RecipeListHeader();
        addButton = header.getAddButton();
        signOutButton = header.getSignOutButton();
        sortBox = header.getSortBox();
        filterBox = header.getFilterBox();

        this.detailedViewButton = new Button();
        
        this.recipes = recipes;
        // Add header to the top of the BorderPane
        this.setTop(header);
        // Populate the Body
        populateRecipe();

    }

    public void populateRecipe(){
        vbox = new VBox();
        vbox.setSpacing(7);
        vbox.setPadding(new Insets(10, 0, 30, 0));
        vbox.setStyle("-fx-background-color: #FFFFFF;");

        for (Recipe recipe : recipes) {
            RecipeItem Item = new RecipeItem();
            Item.setRecipe(recipe);
            vbox.getChildren().add(Item);
            EventHandler<ActionEvent> viewButton = e -> {RecipeListPage.recipe = Item.getRecipe();};
            Item.getDetailedViewButton().addEventHandler(ActionEvent.ACTION, viewButton);
            
        }


        ScrollPane scroller = new ScrollPane(vbox);
        scroller.setFitToHeight(isCache());
        scroller.setFitToWidth(true);

        this.setCenter(scroller);
    }

    public static void showDetailedView(Recipe r) {
        recipe = r;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public ComboBox<String> getSortBox() {
        return sortBox;
    }
    
    public ComboBox<String> getFilterBox() {
        return filterBox;
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

    public void setSortBoxAction(EventHandler<ActionEvent> eventHandler){
        sortBox.setOnAction(eventHandler);
    }

    public void setFilterBoxAction(EventHandler<ActionEvent> eventHandler){
        filterBox.setOnAction(eventHandler);
    }
    
}
