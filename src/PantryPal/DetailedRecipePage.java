package PantryPal;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.scene.control.ScrollPane;

import java.io.IOException;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;

class Header extends HBox {
    private Button deleteButton;
    private Button editButton;
    private Button saveButton;

    Header() {
        this.setPrefSize(500, 50);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial";

        ImageView backIcon = new ImageView(new Image("./icons/back.png"));
        deleteButton = new Button(); // text displayed on delete contacts button
        deleteButton.setGraphic(backIcon);
        deleteButton.setStyle(defaultButtonStyle); // styling the button

        ImageView saveIcon = new ImageView(new Image("./icons/save.png"));
        saveButton = new Button(); // text displayed on save contacts button
        saveButton.setGraphic(saveIcon);
        saveButton.setStyle(defaultButtonStyle); // styling the button

        ImageView editIcon = new ImageView(new Image("./icons/edit.png"));
        editButton = new Button(); // text displayed on sort contacts button
        editButton.setGraphic(editIcon);
        editButton.setStyle(defaultButtonStyle); // styling the button

        HBox hb1 = new HBox(deleteButton);
        hb1.setAlignment(Pos.BASELINE_LEFT);
        hb1.setSpacing(10);
        hb1.setPadding(new Insets(0, 100, 0, 0));

        HBox hb2 = new HBox(editButton, saveButton);
        hb2.setAlignment(Pos.BASELINE_RIGHT);
        hb2.setSpacing(10);
        hb2.setPadding(new Insets(0, 0, 0, 150));
        Bounds bounds = this.getBoundsInParent();
        // System.out.println(bounds);

        this.getChildren().addAll(hb1, hb2); // adding buttons to footer
        // this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    public Button getdeleteButton() {
        return deleteButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getEditButton() {
        return editButton;
    }
}

public class DetailedRecipePage extends BorderPane {

    // private RecipeListPage recipeListPage;
    // private PantryPal pantryPal;
    // private CSVHandler csvHandler;
    // private SpecifyIngredientsPage specifyIngredientsPage;

    private Header header;

    private Recipe currRecipe;

    private Button deleteButton;
    private Button editButton;
    private Button saveButton;

    private TextField title = new TextField();
    private TextField instructions = new TextField(); // includes ingredients

    // private Boolean editing = false;

    // Assumes that Recipe class has at least TextFields for title, ingredients, and
    // instructions
    DetailedRecipePage(Recipe recipe) {
        header = new Header();

        deleteButton = header.getdeleteButton();
        editButton = header.getEditButton();
        saveButton = header.getSaveButton();

        title.setText(recipe.getTitle());
        instructions.setText(recipe.getInstructions());

        title.setEditable(false);
        instructions.setEditable(false);

        createUI();
        addListeners(recipe);
    }

    private void createUI() {
        VBox titleContainer = new VBox(title);
        VBox bodyText = new VBox(instructions);
        ScrollPane sp = new ScrollPane(bodyText);
        sp.setFitToWidth(true);
        sp.setFitToHeight(false);

        titleContainer.setPadding(new Insets(0, 20, 0, 20));
        bodyText.setPadding(new Insets(0, 20, 0, 20));

        VBox container = new VBox(titleContainer, bodyText);
        this.setTop(header);
        this.setCenter(container);
    }

    public void addListeners(Recipe recipe) {
        // Delete recipe and go back to RecipeListPage
        deleteButton.setOnAction(e -> {
            deleteRecipe();
        });

        // Toggle editing of ingredients & instructions
        editButton.setOnAction(e -> {
            // editRecipe();
        });

        // Save recipe and go back to RecipeListPage
        saveButton.setOnAction(e -> {
            try {
                saveRecipe(recipe);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void exitWindow() {
        // Go back to RecipeListPage
    }

    private void deleteRecipe() {
        exitWindow();
    }

    // private void editRecipe() {
    // if (this.editing) {
    // this.instructions.setEditable(false);
    // this.editing = false;
    // } else {
    // this.instructions.setEditable(true);
    // this.editing = true;
    // }
    // }

    private void saveRecipe(Recipe recipe) throws IOException {
        // call CSVHandler for saving
        CSVHandler.writeRecipes(recipe);
        exitWindow();
    }
}
