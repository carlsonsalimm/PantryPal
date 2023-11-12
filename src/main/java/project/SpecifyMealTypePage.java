package project;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class SpecifyMealTypePage extends BorderPane {
    
    private AudioRecording recordButton;

    SpecifyMealTypePage() {
        //button to record audio
        recordButton = new AudioRecording();
        //button place at center of page
        this.setCenter(recordButton);

        //button to cancel recording and return to recipe list page
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> Main.setPage(Main.temp));
        //button place at bottom center of page
        this.setBottom(cancelButton);
        this.setAlignment(cancelButton, Pos.CENTER);

        
    }
}
