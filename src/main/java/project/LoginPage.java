package project;

import java.io.IOException;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.application.Application;


class LoginPage extends VBox {

    private Label title;

    private TextField username;
    private TextField password;

    private CheckBox rememberMeCB;
    private Label rememberMe;

    private Button signInButton;
    private Button createAccountButton;


    // styles
    private String titleStyle = "-fx-font-size: 25;-fx-font-weight: bold;";
    private String rememberMeStyle = "-fx-font-size: 12;";
    private String textfieldStyle =  "-fx-background-radius: 5; -fx-font-style: italic; -fx-background-color: #D9D9D9; -fx-font-weight: bold; -fx-font: 11 arial; -fx-top-spacing: 5;";
    private String actionButtonStyle =  "-fx-background-radius: 5; -fx-font-style: italic; -fx-background-color: #D9D9D9; -fx-font-weight: bold; -fx-font: 11 arial; -fx-top-spacing: 5;";
    LoginPage() {
        this.setPrefSize(600, 630); // Size of the Body
        this.setStyle("-fx-background-color: #FFFFFF;");
        
        // Header space
        HBox header = new HBox();
        header.setPrefSize(600, 100);

        // Initialize all contents 
        // LoginPage Title
        this.title = new Label("PantryPal");
        this.title.setStyle(titleStyle);
        // User Credentials
        this.username = new TextField();
        this.username.setPromptText("Username");
        this.username.setStyle(textfieldStyle);
        this.password = new TextField();
        this.password.setPromptText("Password");
        this.password.setStyle(textfieldStyle);
        // Remember Me section, restructure 
        this.rememberMeCB = new CheckBox();
        //this.rememberMeCB.setStyle(rememberButtonStyle);
        rememberMeCB.setIndeterminate(false);
        this.rememberMe = new Label("Remember Me");
        this.rememberMe.setStyle(rememberMeStyle);
        // // Sign-in / Create Section 
        this.signInButton = new Button("Sign In");
        this.signInButton.setStyle(actionButtonStyle);
        this.createAccountButton = new Button("Create Account");
        this.createAccountButton.setStyle(actionButtonStyle);

        // Initialize containers
        HBox titleContainer = new HBox(title);
        titleContainer.setAlignment(Pos.CENTER);
        HBox userContainer = new HBox(username);
        userContainer.setAlignment(Pos.CENTER);
        HBox passwordContainer = new HBox(password);
        passwordContainer.setAlignment(Pos.CENTER);

        HBox rememberMeContainer = new HBox(rememberMeCB);
        rememberMeContainer.getChildren().add(rememberMe);
        rememberMeContainer.setAlignment(Pos.CENTER);
        rememberMeContainer.setSpacing(5);

        HBox buttonContainer = new HBox(signInButton);
        buttonContainer.getChildren().add(createAccountButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(10);

        // Place containers onto VBox
        this.getChildren().addAll(header,titleContainer,userContainer,passwordContainer,rememberMeContainer,buttonContainer);
        this.setSpacing(5);
        this.requestLayout();
    }

    public String getUsername(){
        return username.getText();
    }

    public String getPassword(){
        return password.getText();
    }

    public boolean getRememberMe(){
        return rememberMeCB.isSelected();
    }

    public Button getSignInButton() {
        return signInButton;
    }

    public Button getCreateAccountButton() {    
        return createAccountButton;
    }

    public void setSignInButtonAction(EventHandler<ActionEvent> eventHandler){
        signInButton.setOnAction(eventHandler);
    }

    public void setCreateAccountButtonAction(EventHandler<ActionEvent> eventHandler){
        createAccountButton.setOnAction(eventHandler);
    }


    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
