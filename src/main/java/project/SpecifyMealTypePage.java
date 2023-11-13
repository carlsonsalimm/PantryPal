package project;
import java.io.*;

import javax.sound.sampled.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.scene.control.Label;


public class SpecifyMealTypePage extends BorderPane {
    
    private AudioRecording recordButton;

    private String cancelButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button cancelButton;

    private StackPane topContainer;
    private StackPane bodyContainer;

    private String promptStyle = "-fx-font-size: 20;-fx-font-weight: bold;";
    private String subPromptStyle = "-fx-font-size: 20;";
    private String errorMsgStyle = "-fx-font-size: 20;-fx-font-weight: bold; -fx-font-color: #DF0000;";

    private Label prompt;
    private Label subPrompt;
    private Label errorMsg;


    SpecifyMealTypePage() {
        //button to cancel recording and return to recipe list page
        cancelButton = new Button("x");
        cancelButton.setStyle(cancelButtonStyle);
        topContainer = new StackPane(cancelButton);
        StackPane.setMargin(cancelButton, new Insets(0, 10, 0, 0));

        // Set up labels
        prompt = new Label("What kind of meal are you making?");
        subPrompt = new Label("Say \"breafast, \"lunch,\" or \"dinner.\"");
        errorMsg = new Label("Sorry, we didn't catch that. Please Try Again");

        //button to record audio
        recordButton = new AudioRecording();
        bodyContainer = new StackPane(recordButton);
        
        //button place at center of page
        this.setTop(topContainer);
        this.setCenter(bodyContainer);

        cancelButton.setOnAction(event -> Main.setPage(Main.temp));

        //button place at bottom center of page
        //this.setAlignment(cancelButton, Pos.CENTER);

        
    }

}
