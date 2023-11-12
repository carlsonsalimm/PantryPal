package project;
import javafx.scene.layout.BorderPane;

// import org.junit.platform.reporting.shadow.org.opentest4j.reporting.events.core.Sources;

public class SpecifyIngredientPage extends BorderPane {

    private AudioRecording recordButton;
    
    //Constructor for Specify Ingredient Page
    SpecifyIngredientPage(String mealType) {
        recordButton = new AudioRecording(mealType);

        this.setCenter(recordButton);
    }

}


