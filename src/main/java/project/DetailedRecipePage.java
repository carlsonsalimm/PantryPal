package project;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

import java.io.IOException;

class Header extends HBox {
    private Label title;
    private Button delButton;
    private Button saveButton;
    private Button backButton;
    private Button refreshButton;
    private Button shareButton;
    private Button editButton;
    private BorderPane pane;
    private Pane titleContainer;
    private Pane addContainer;

    private Boolean newMealFlag;

    static String defaultButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";

    Header(Boolean newCreation) {
        this.setPrefSize(600, 90); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;");
        pane = new BorderPane();
        pane.setPrefSize(565, 40);

        title = new Label("Recipe"); // Text of the Header

        title.setStyle("-fx-font-size: 20;-fx-font-weight: bold;");
        title.setPrefSize(475, 40); // sets size of Recipe
        title.setTextAlignment(TextAlignment.CENTER);

        delButton = new Button(); // text displayed on add button
        ImageView trash = new ImageView("./icons/trash.png");
        trash.setFitWidth(20);
        trash.setFitHeight(20);
        delButton.setGraphic(trash);
        delButton.setStyle(defaultButtonStyle); // styling the button

        saveButton = new Button(); // text displayed on add button
        ImageView save = new ImageView("./icons/save.png");
        save.setFitWidth(20);
        save.setFitHeight(20);
        saveButton.setGraphic(save);
        saveButton.setStyle(defaultButtonStyle); // styling the button

        backButton = new Button(); // text displayed on add button
        ImageView back = new ImageView("./icons/back.png");
        back.setFitWidth(20);
        back.setFitHeight(20);
        backButton.setGraphic(back);
        backButton.setStyle(defaultButtonStyle); // styling the button

        refreshButton = new Button(); // text displayed on add button
        ImageView refresh = new ImageView("./icons/refresh.png");
        refresh.setFitWidth(20);
        refresh.setFitHeight(20);
        refreshButton.setGraphic(refresh);
        refreshButton.setStyle(defaultButtonStyle); // styling the button
        

        shareButton = new Button(); // text displayed on add button
        ImageView share = new ImageView("./icons/share.png");
        share.setFitWidth(20);
        share.setFitHeight(20);
        shareButton.setGraphic(share);
        shareButton.setStyle(defaultButtonStyle); // styling the button

        editButton = new Button(); // text displayed on add button
        ImageView edit = new ImageView("./icons/edit.png");
        edit.setFitWidth(20);
        edit.setFitHeight(20);
        editButton.setGraphic(edit);
        editButton.setStyle(defaultButtonStyle); // styling the button

        titleContainer = new Pane();
        titleContainer.getChildren().addAll(backButton, title);

        if(newCreation){
            titleContainer.getChildren().add(refreshButton);
        }

        title.relocate(20, 35);
        backButton.relocate(20, 0);
        refreshButton.relocate(70,0);

        addContainer = new Pane();
        addContainer.getChildren().addAll(delButton, shareButton, editButton, saveButton);
        delButton.relocate(-100, 0);
        shareButton.relocate(-50, 0);
        editButton.relocate(0,0);
        saveButton.relocate(50,0);

        pane.setLeft(titleContainer);
        pane.setRight(addContainer);
        BorderPane.setMargin(titleContainer,new Insets(10,0,0,0));
        BorderPane.setMargin(addContainer,new Insets(10,0,20,0));
        this.getChildren().add(pane);
        this.setAlignment(Pos.CENTER);
    }

    public Button getDelButton() {
        return delButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getShareButton() {
        return shareButton;
    }
}

public class DetailedRecipePage extends BorderPane {

    public static String defaultButtonStyle = "-fx-background-color: #D9D9D9; -fx-background-radius: 5em; ";
    public static String defaultMouseOverButtonStyle = "-fx-background-color: #bfbfbf; -fx-background-radius: 5em;";
    public static String defaultMouseClickButtonStyle = "-fx-background-color: #D9D9D9; -fx-background-radius: 5em;";
    public static String defaultBackgroundStyle = "-fx-background-color: -fx-text-box-border, -fx-control-inner-background; -fx-text-box-border: transparent;";
    public static String defaultTextStyle = "-fx-font-weight: bold;";

    private Header header;
    private Button deleteButton;
    private Button backButton;
    private Button saveButton;
    private Button refreshButton;
    private Button shareButton;
    private Button editButton;

    private ImageView image;
    private Text mealType;
    private Text title;
    public TextArea ingredients;
    public TextArea instructions; // includes ingredients


    private Recipe recipe;

    // Assumes that Recipe class has at least TextFields for title, ingredients, and
    // instructions
    DetailedRecipePage(Recipe recipe, Boolean newCreation) {
        this.header = new Header(newCreation);

        this.recipe = recipe;
        // Getting Header Buttons
        this.deleteButton = header.getDelButton();
        this.backButton = header.getBackButton();
        this.saveButton = header.getSaveButton();
        this.refreshButton = header.getRefreshButton();
        this.shareButton = header.getShareButton();
        this.editButton = header.getEditButton();

        this.title = new Text();
        this.mealType = new Text();
        this.ingredients = new TextArea();
        this.instructions = new TextArea();

        // Setting the title and Instructions
        title.setText(recipe.getTitle());
        mealType.setText(recipe.getMealType());
        ingredients.setText(recipe.getIngredients());
        ingredients.setPrefSize(500,180);
        instructions.setText(recipe.getInstructions());
        instructions.setPrefSize(550, 180);

        //title.setEditable(false);
        ingredients.setEditable(false);
        instructions.setEditable(false);
        instructions.setWrapText(true);

        // Page Style
        this.setStyle(defaultBackgroundStyle);

        // Setting UI Labels
        // Initialize the ImageView
        image = new ImageView(); // Initialize with no image or a placeholder image
        image.setFitWidth(150);
        image.setFitHeight(150);
        recipe.setImageURL("https://oaidalleapiprodscus.blob.core.windows.net/private/org-Sd9bwBmEf5IDns4KIh3k3fXp/user-TTwaqZd6kA45CPFJ9Srb7I12/img-frVJrR47vS0SgE3MDzYmj6Vf.png?st=2023-12-06T03%3A20%3A33Z&se=2023-12-06T05%3A20%3A33Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-12-05T19%3A52%3A36Z&ske=2023-12-06T19%3A52%3A36Z&sks=b&skv=2021-08-06&sig=OPcjqwb3CfdngGAS7n8PrDOHHVA6IFtkaOzeuUSN/SE%3D");
        if (recipe.getImageURL() != null && !recipe.getImageURL().isEmpty()) {
            //setImage(recipe.getImageURL());
            setImage(recipe.getImageURL());
        } else {
            // Set a default placeholder image if needed
            setImage("./icons/trash.png");
        }

        Text name = new Text("Recipe Title:");
        name.setStyle(defaultTextStyle);

        Text mealTypeName = new Text("Meal Type:");
        mealTypeName.setStyle(defaultTextStyle);

        Text ingredientName = new Text("Ingredients:");
        ingredientName.setStyle(defaultTextStyle);
        Text instruct = new Text("Instructions:");
        instruct.setStyle(defaultTextStyle);

        // Adding to a container
        HBox imageContainer = new HBox(image);
        HBox titleContainer = new HBox(name, title);
        titleContainer.setSpacing(5);
        HBox mealTypeContainer = new HBox(mealTypeName, mealType);
        mealTypeContainer.setSpacing(5);
        VBox ingredientBodyText = new VBox(ingredientName, ingredients);
        VBox instructBodyText = new VBox(instruct, instructions);

        ScrollPane spIngredients = new ScrollPane(ingredientBodyText);
        spIngredients.setFitToWidth(true);
        spIngredients.setFitToHeight(false);

        ScrollPane spInstruct = new ScrollPane(instructBodyText);
        spInstruct.setFitToWidth(true);
        spInstruct.setFitToHeight(false);

        titleContainer.setPadding(new Insets(0, 20, 0, 20));
        titleContainer.setStyle(defaultBackgroundStyle);
        mealTypeContainer.setPadding(new Insets(0, 20, 0, 20));
        mealTypeContainer.setStyle(defaultBackgroundStyle);
        ingredientBodyText.setPadding(new Insets(0, 20, 0, 20));
        ingredientBodyText.setStyle(defaultBackgroundStyle);
        instructBodyText.setPadding(new Insets(0, 20, 0, 20));
        instructBodyText.setStyle(defaultBackgroundStyle);

        VBox container = new VBox(imageContainer, titleContainer, mealTypeContainer, ingredientBodyText,instructBodyText);
        container.setStyle(defaultBackgroundStyle);
        
        container.setSpacing(10);
        this.setTop(header);
        this.setCenter(container);

        // Update the page with the initial recipe details
        updateRecipeDetails(recipe); 
    }

    public void setTitle(String title){
        this.title.setText(title);
    }

    public void setInstructions(String instructions){
        this.instructions.setText(instructions);
    }

    public void setIngredients(String ingredients){
        this.ingredients.setText(ingredients);
    }

    public String getMealType(){
        return this.mealType.toString();
    }

    public String getIngredients(){
        return this.ingredients.toString();
    }

    public Recipe getRecipe(){
        return this.recipe;
    }

    public String getTitle(){
        return title.getText();
    }

    public String getInstructions(){
        return instructions.getText();
    }

    public void setDeleteButtonAction(EventHandler<ActionEvent> eventHandler){
        deleteButton.setOnAction(eventHandler);
    }

    public void setSaveButtonAction(EventHandler<ActionEvent> eventHandler){
        saveButton.setOnAction(eventHandler);
    }

    public void setBackButtonAction(EventHandler<ActionEvent> eventHandler){
        backButton.setOnAction(eventHandler);
    }

    public void setRefreshButtonAction(EventHandler<ActionEvent> eventHandler){
        refreshButton.setOnAction(eventHandler);
    }

    public void setShareButtonAction(EventHandler<ActionEvent> eventHandler){
        shareButton.setOnAction(eventHandler);
    }

    public void setEditButtonAction(EventHandler<ActionEvent> eventHandler){
        editButton.setOnAction(eventHandler);
    }

    public void updateRecipeDetails(Recipe newRecipe) {
        this.recipe = newRecipe;
        this.title.setText(newRecipe.getTitle());
        this.mealType.setText(newRecipe.getMealType());
        this.ingredients.setText(newRecipe.getIngredients());
        this.instructions.setText(newRecipe.getInstructions());
    
        if (newRecipe.getImageURL() != null && !newRecipe.getImageURL().isEmpty()) {
            //newRecipe.setImageURL("./icons/trash.png");
            setImage(newRecipe.getImageURL());
        } 
    }

    void setImage(String imageURL) {
        try {
            Image newImage = new Image(imageURL, true); // true for background loading
            this.image.setImage(newImage);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid image URL: " + imageURL);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }

}
