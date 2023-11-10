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
        recordButton = new AudioRecording();

        this.setCenter(recordButton);
    }
}
