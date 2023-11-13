package project;
import java.io.IOException;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.scene.text.*;

// class MealTypeHeader extends VBox {
//     private Label questionPrompt;
//     private Button cancelButton;
//     private BorderPane pane;
//     private StackPane titleContainer;
//     private StackPane addContainer;

//     MealTypeHeader() {
//         this.setPrefSize(600, 70); // Size of the header
//         this.setStyle("-fx-background-color: #FFFFFF;");
//         pane = new BorderPane();
//         pane.setPrefSize(565, 40); // sets size of Recipe
//     }

// } 

public class SpecifyMealTypePage extends BorderPane {
    String cancelButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private AudioRecording recordButton;
    SpecifyMealTypePage() {
        //button to record audio
        recordButton = new AudioRecording();
        //button place at center of page
        this.setCenter(recordButton);

        //button to cancel recording and return to recipe list page
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle(cancelButtonStyle);
        cancelButton.setOnAction(event -> Main.setPage(Main.temp));
        //button place at bottom center of page
        this.setBottom(cancelButton);
        this.setAlignment(cancelButton, Pos.CENTER);

        // UI implementation v2 
        

        
    }
}
