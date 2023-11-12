package project;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

// import org.junit.platform.reporting.shadow.org.opentest4j.reporting.events.core.Sources;

public class SpecifyIngredientPage extends BorderPane {

    private AudioRecording recordButton;
    
    //Constructor for Specify Ingredient Page
    SpecifyIngredientPage(String mealType) {
        recordButton = new AudioRecording(mealType);

        this.setCenter(recordButton);
    }

}


