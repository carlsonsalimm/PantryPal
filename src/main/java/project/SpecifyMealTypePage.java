package project;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

class SpecifyMealTypeContent extends VBox {
    private AudioRecording recordButton;

    private HBox promptContainer;
    private HBox subPromptContainer;
    private HBox recordButtonContainer;

    private String promptStyle = "-fx-font-size: 20;-fx-font-weight: bold;";
    private String subPromptStyle = "-fx-font-size: 20;";

    private Label prompt;
    private Label subPrompt;


    SpecifyMealTypeContent() {
        this.setPrefSize(600, 700); // Size of the header
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        //button to cancel recording and return to recipe list page
        

        // Set up labels
        prompt = new Label("What kind of meal are you making?");
        prompt.setStyle(promptStyle);
        subPrompt = new Label("Say \"breakfast, \"lunch,\" or \"dinner.\"");
        subPrompt.setStyle(subPromptStyle);

        //button to record audio
        recordButton = new AudioRecording();
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

        //cancelButton.setOnAction(event -> Main.setPage(Main.temp));
    }

    public AudioRecording getAudioRecording() {
        return this.recordButton;
    }

}

public class SpecifyMealTypePage extends BorderPane {
    private SpecifyMealTypeContent content;
    private String cancelButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button cancelButton;

    SpecifyMealTypePage() {
        content = new SpecifyMealTypeContent();
        cancelButton = new Button("x");
        cancelButton.setStyle(cancelButtonStyle);
        cancelButton.setPadding(new Insets(10, 0, 0, 10));

        this.setTop(cancelButton);
        this.setCenter(content);
        addListeners();
    }

    public void addListeners() {
        cancelButton.setOnAction(event -> Main.setPage(Main.temp));
    }
    
}
