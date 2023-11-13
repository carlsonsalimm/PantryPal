package project;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

// import org.junit.platform.reporting.shadow.org.opentest4j.reporting.events.core.Sources;

public class SpecifyIngredientPage extends BorderPane {

    private AudioRecording recordButton;

    private String cancelButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button cancelButton;

    private StackPane topContainer;
    private StackPane bodyContainer;

    private String promptStyle = "-fx-font-size: 20;-fx-font-weight: bold;";
    private String subPromptStyle = "-fx-font-size: 20;";

    private Label prompt;
    private Label subPrompt;
    
    //Constructor for Specify Ingredient Page
    SpecifyIngredientPage(String mealType) {
        //button to cancel recording and return to recipe list page
        cancelButton = new Button("x");
        cancelButton.setStyle(cancelButtonStyle);
        topContainer = new StackPane(cancelButton);
        StackPane.setMargin(cancelButton, new Insets(0, 10, 0, 0));

        // Set up labels
        prompt = new Label("What ingredients do you have?");
        prompt.setStyle(promptStyle);
        subPrompt = new Label("Tell us what you have and how much!");
        subPrompt.setStyle(subPromptStyle);
        
        // button to record audio
        recordButton = new AudioRecording(mealType);
        bodyContainer = new StackPane(prompt,subPrompt,recordButton);
        
        StackPane.setMargin(cancelButton, new Insets(10, 300, 0, 0));
        StackPane.setMargin(prompt, new Insets(10, 0, 0, 0));
        StackPane.setMargin(prompt, new Insets(10, 0, 0, 0));
        //place stack panges onto border pane
        this.setTop(topContainer);
        this.setCenter(bodyContainer);

        //this.setAlignment(Pos.CENTER);

        cancelButton.setOnAction(event -> Main.setPage(Main.temp));
    }

}


