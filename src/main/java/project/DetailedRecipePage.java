package project;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import java.io.IOException;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;

class Header extends HBox {
    private Button deleteButton;
    private Button editButton;
    private Button saveButton;

    Header() {
        this.setPrefSize(500, 80);
        this.setStyle("fx-background-color: #FFFFFF;");
        this.setSpacing(15);
        this.setPadding(new Insets(0, 30, 0, 30));

        // set a default style for buttons
        
        
        int buttonRadius = 22;

        ImageView backIcon = new ImageView(
                new Image(DetailedRecipePage.class.getResource("/icons/back.png").toString(),
                        50, 50, true, false));
        deleteButton = new Button(); // text displayed on delete contacts button
        deleteButton.setGraphic(backIcon);
        deleteButton.setStyle(DetailedRecipePage.defaultButtonStyle); // styling the button
        deleteButton.setShape(new Circle(buttonRadius));
        deleteButton.setMinSize(buttonRadius * 2, buttonRadius * 2);
        deleteButton.setMaxSize(buttonRadius * 2, buttonRadius * 2);

        ImageView saveIcon = new ImageView(
                new Image(DetailedRecipePage.class.getResource("/icons/save.png").toString(),
                        50, 50, true, false));
        saveButton = new Button(); // text displayed on save contacts button
        saveButton.setGraphic(saveIcon);
        saveButton.setStyle(DetailedRecipePage.defaultButtonStyle); // styling the button
        saveButton.setShape(new Circle(buttonRadius));
        saveButton.setMinSize(buttonRadius * 2, buttonRadius * 2);
        saveButton.setMaxSize(buttonRadius * 2, buttonRadius * 2);

        ImageView editIcon = new ImageView(
                new Image(DetailedRecipePage.class.getResource("/icons/edit.png").toString(),
                        50, 50, true, false));
        editButton = new Button(); // text displayed on sort contacts button
        editButton.setGraphic(editIcon);
        editButton.setStyle(DetailedRecipePage.defaultButtonStyle); // styling the button
        editButton.setShape(new Circle(buttonRadius));
        editButton.setMinSize(buttonRadius * 2, buttonRadius * 2);
        editButton.setMaxSize(buttonRadius * 2, buttonRadius * 2);

        HBox left = new HBox(deleteButton);
        left.setAlignment(Pos.CENTER_LEFT);
        left.setSpacing(10);
        left.setPadding(new Insets(0, 100, 0, 0));

        HBox right = new HBox(editButton, saveButton);
        right.setAlignment(Pos.CENTER_RIGHT);
        right.setSpacing(10);
        right.setPadding(new Insets(0, 0, 0, 150));

        HBox fillerSpace = new HBox();
        HBox.setHgrow(fillerSpace, Priority.ALWAYS);
        // System.out.println(bounds);

        this.getChildren().addAll(left, fillerSpace, right); // adding buttons to footer
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

    public static String defaultButtonStyle = "-fx-background-color: #D9D9D9; -fx-background-radius: 5em; ";
    public static String defaultMouseOverButtonStyle = "-fx-background-color: #bfbfbf; -fx-background-radius: 5em;";
    public static String defaultMouseClickButtonStyle = "-fx-background-color: #D9D9D9; -fx-background-radius: 5em;";
    public static String defaultBackgroundStyle = "-fx-background-color: -fx-text-box-border, -fx-control-inner-background; -fx-text-box-border: transparent; -fx-focus-color: transparent;";

    private Header header;
    private Button deleteButton;
    private Button editButton;
    private Button saveButton;

    private TextField title = new TextField();
    private TextArea instructions = new TextArea(); // includes ingredients

    private Boolean editing = false;

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

        instructions.setWrapText(true);

        createUI();
        addListeners(recipe);
    }

    private void createUI() {
        this.setStyle(defaultBackgroundStyle);

        VBox titleContainer = new VBox(title);
        VBox bodyText = new VBox(instructions);
        ScrollPane sp = new ScrollPane(bodyText);
        sp.setFitToWidth(true);
        sp.setFitToHeight(false);

        titleContainer.setPadding(new Insets(0, 20, 0, 20));
        titleContainer.setStyle( defaultBackgroundStyle);
        bodyText.setPadding(new Insets(0, 20, 0, 20));
        bodyText.setStyle( defaultBackgroundStyle);

        VBox container = new VBox(titleContainer, bodyText);
        container.setStyle(defaultBackgroundStyle);
        container.setSpacing(10);
        this.setTop(header);
        this.setCenter(container);
    }

    public void addListeners(Recipe recipe) {
        // Delete recipe and go back to RecipeListPage
        deleteButton.setOnAction(e -> {
            try {
                deleteRecipe(recipe);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        deleteButton.setOnMouseEntered(event -> deleteButton.setStyle(defaultMouseOverButtonStyle));
        deleteButton.setOnMouseExited(event -> deleteButton.setStyle(defaultButtonStyle));

        // Toggle editing of ingredients & instructions
        editButton.setOnAction(e -> {
            editRecipe();
        });

        editButton.setOnMouseEntered(event -> {if (!editing)editButton.setStyle(defaultMouseOverButtonStyle);});
        editButton.setOnMouseExited(event -> {if (!editing)editButton.setStyle(defaultButtonStyle);});

        // Save recipe and go back to RecipeListPage
        saveButton.setOnAction(e -> {
            try {
                saveRecipe(recipe);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });
        saveButton.setOnMouseEntered(event -> saveButton.setStyle(defaultMouseOverButtonStyle));
        saveButton.setOnMouseExited(event -> saveButton.setStyle(defaultButtonStyle));
    }

    private void exitWindow() throws IOException {
        // Go back to RecipeListPage
        RecipeListPage temp = new RecipeListPage();
        Main.setPage(temp);
    }

    private void deleteRecipe(Recipe recipe) throws IOException {
        CSVHandler.deleteRecipe(recipe);
        exitWindow();
    }

    private void editRecipe() {
        if (this.editing) {
            this.instructions.setEditable(false);
            this.editing = false;
            this.editButton.setStyle(defaultMouseOverButtonStyle);
        } else {
            this.instructions.setEditable(true);
            this.editing = true;
            this.editButton.setStyle(defaultMouseClickButtonStyle);
        }
    }

    private void saveRecipe(Recipe oldRecipe) throws IOException {
        // call CSVHandler for saving new recipe or updating old recipe
        // System.out.println(oldRecipe.getInstructions() + "\n");
        // System.out.println(this.instructions.getText() + "\n");
        if (oldRecipe.getInstructions().equals(this.instructions.getText())) {
            CSVHandler.writeRecipes(oldRecipe);
        } else {
            CSVHandler.updateRecipe(oldRecipe, new Recipe(this.title.getText(), this.instructions.getText()));
        }

        exitWindow();
    }
}
