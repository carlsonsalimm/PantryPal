package project;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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

    static String defaultButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";

    Header() {
        this.setPrefSize(600, 70); // Size of the header
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
        titleContainer.getChildren().addAll(backButton, title, refreshButton);
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
        BorderPane.setMargin(addContainer,new Insets(10,0,0,0));
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
    public static String defaultTextStyle;

    private Header header;
    private Button deleteButton;
    private Button backButton;
    private Button saveButton;
    private Button refreshButton;
    private Button shareButton;
    private Button editButton;

    private TextField title;
    private TextArea instructions; // includes ingredients

    private Recipe recipe;

    // Assumes that Recipe class has at least TextFields for title, ingredients, and
    // instructions
    DetailedRecipePage(Recipe recipe) {
        this.header = new Header();

        this.recipe = recipe;

        // Getting Header Buttons
        this.deleteButton = header.getDelButton();
        this.backButton = header.getBackButton();
        this.saveButton = header.getSaveButton();
        this.refreshButton = header.getRefreshButton();
        this.shareButton = header.getShareButton();
        this.editButton = header.getEditButton();

        this.title = new TextField();
        this.instructions = new TextArea();

        // Setting the title and Instructions
        title.setText(recipe.getTitle());
        instructions.setText(recipe.getInstructions());
        instructions.setPrefSize(600, 550);

        title.setEditable(false);
        instructions.setEditable(true);
        instructions.setWrapText(true);

        // Page Style
        this.setStyle(defaultBackgroundStyle);

        // Setting UI Labels
        Text name = new Text("Name:");
        name.setStyle(defaultTextStyle);

        Text instruct = new Text("Instructions:");
        instruct.setStyle(defaultTextStyle);

        // Adding to a container
        VBox titleContainer = new VBox(name, title);
        VBox bodyText = new VBox(instruct, instructions);
        ScrollPane sp = new ScrollPane(bodyText);
        sp.setFitToWidth(true);
        sp.setFitToHeight(false);

        titleContainer.setPadding(new Insets(0, 20, 0, 20));
        titleContainer.setStyle(defaultBackgroundStyle);
        bodyText.setPadding(new Insets(0, 20, 0, 20));
        bodyText.setStyle(defaultBackgroundStyle);

        VBox container = new VBox(titleContainer, bodyText);
        container.setStyle(defaultBackgroundStyle);
        container.setSpacing(10);

        this.setTop(header);
        this.setBottom(container);
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

}
