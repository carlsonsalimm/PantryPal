package project;
import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// import org.junit.platform.reporting.shadow.org.opentest4j.reporting.events.core.Sources;

class SpecifyIngredientContent extends VBox {
    private AudioRecording recordButton;

    private HBox promptContainer;
    private HBox subPromptContainer;
    private HBox recordButtonContainer;

    private String promptStyle = "-fx-font-size: 20;-fx-font-weight: bold;";
    private String subPromptStyle = "-fx-font-size: 20;";

    private Label prompt;
    private Label subPrompt;

    private String username;

    private String password;


    SpecifyIngredientContent(String mealType, String username, String password) {
        this.setPrefSize(600, 700); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        //button to cancel recording and return to recipe list page
        

        // Set up labels
        prompt = new Label("What ingredients do you have?");
        prompt.setStyle(promptStyle);
        subPrompt = new Label("Tell us what you have and how much!");
        subPrompt.setStyle(subPromptStyle);

        //button to record audio
        recordButton = new AudioRecording(mealType, this.username, this.password);
        //bodyContainer = new StackPane();
        
        promptContainer = new HBox(prompt);
        promptContainer.setAlignment(Pos.CENTER);
        subPromptContainer = new HBox(subPrompt);
        subPromptContainer.setAlignment(Pos.CENTER);
        recordButtonContainer = new HBox(recordButton);
        recordButtonContainer.setAlignment(Pos.CENTER);
        
        //place stack panges onto border pane

        this.getChildren().addAll(promptContainer, subPromptContainer, recordButtonContainer);
        this.requestLayout();
    }

    public AudioRecording getAudioRecording() {
        return this.recordButton;
    }

}

public class SpecifyIngredientPage extends BorderPane {
    private SpecifyIngredientContent content;
    private String cancelButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button cancelButton;
    private String username;
    private String password;

    SpecifyIngredientPage(String mealType, String username, String password) {
        this.username = username;
        this.password = password;
        
        content = new SpecifyIngredientContent(mealType, username, password);
        cancelButton = new Button("x");
        cancelButton.setStyle(cancelButtonStyle);
        cancelButton.setPadding(new Insets(10, 0, 0, 10));

        this.setTop(cancelButton);
        this.setCenter(content);
        addListeners();
    }

    public void addListeners() {
        cancelButton.setOnAction(event -> {
            RecipeListPage temp;
            try {
                temp = new RecipeListPage(this.username, this.password);
                Main.setPage(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
}