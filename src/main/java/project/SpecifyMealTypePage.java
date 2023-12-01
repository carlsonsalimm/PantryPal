package project;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

class SpecifyMealTypePage extends VBox {

    // Audio
    private AudioRecording recordButton;
    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    private String recorderButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button recorderButton;
    public String mealType;
    private String errorMsgStyle = "-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: #DF0000;";
    private Text errorMsg;
    private Boolean errorFlag = false;

    private HBox promptContainer;
    private HBox subPromptContainer;
    private HBox recordButtonContainer;

    private String promptStyle = "-fx-font-size: 20;-fx-font-weight: bold;";
    private String subPromptStyle = "-fx-font-size: 20;";

    private Label prompt;
    private Label subPrompt;

    private String backButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button backButton;


    SpecifyMealTypePage() {
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
        recorderButton = new Button("Hold to Record");
        recorderButton.setStyle(recorderButtonStyle);
            
        recorderButton.setPrefSize(300, 50);
        this.getChildren().add(recorderButton);
    
        // Set the button actions for mouse press and release
        recorderButton.setOnMousePressed(event -> startRecording());
        recorderButton.setOnMouseReleased(event -> stopRecordingAndProcessMealType());


        
        promptContainer = new HBox(prompt);
        promptContainer.setAlignment(Pos.CENTER);
        subPromptContainer = new HBox(subPrompt);
        subPromptContainer.setAlignment(Pos.CENTER);
        recordButtonContainer = new HBox(recordButton);
        recordButtonContainer.setAlignment(Pos.CENTER);
        
        // Setting the Back Button
        backButton = new Button("x");
        backButton.setStyle(backButtonStyle);
        
        //place stack panges onto border pane

        this.getChildren().addAll(promptContainer, subPromptContainer, recordButtonContainer, backButton);
        this.requestLayout();

        //cancelButton.setOnAction(event -> Main.setPage(Main.temp));
    }

    public AudioRecording getAudioRecording() {
        return this.recordButton;
    }

    public void setBackButtonAction(EventHandler<ActionEvent> eventHandler){
        backButton.setOnAction(eventHandler);
    }

    public void setRecordButtonAction(EventHandler<ActionEvent> eventHandler){
        recordButton.setOnAction(eventHandler);
    }

}

