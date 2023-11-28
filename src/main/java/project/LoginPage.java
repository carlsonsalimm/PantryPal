package project;

import java.io.IOException;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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


class LoginBody extends HBox {
    private HBox hbox;

    private Label title;

    private TextField username;
    private TextField password;

    private Button rememberButton;
    private Label rememberMe;

    private Button signin;
    private Button createAccount;

    private BorderPane border;

    LoginBody() {
        this.setPrefSize(600, 630); // Size of the Body
        this.setStyle("-fx-background-color: #FFFFFF;");
        
        // Header space
        HBox hbox = new HBox();
        hbox.setPrefSize(600, 15);

        // LoginPage Title
        this.title = new Label("PantryPal");
        this.title.setStyle("-fx-font-size: 20;-fx-font-weight: bold;");

        // User Credentials
        this.username = new TextField();


        // Initialize Borders for page
        this.border = new BorderPane();
        border.setTop(hbox);
        border.setCenter(title);

        this.getChildren().addAll(border);
    }
}

public class LoginPage extends BorderPane{
    private LoginBody body;

    LoginPage() throws IOException{
        
        body = new LoginBody();

        this.setCenter(body);
    }
   
}