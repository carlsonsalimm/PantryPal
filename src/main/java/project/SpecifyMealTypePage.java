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
    private String mealType;
    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    private String recorderButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button recordButton;
    private ChatGPT chatGPT;
    private Whisper whisper;
    
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

        prompt = new Label("What kind of meal are you making?");
        subPrompt = new Label("Say \"breafast, \"lunch,\" or \"dinner.\"");
        errorMsg = new Label("Sorry, we didn't catch that. Please Try Again");

        //button to record audio
        recordButton = new Button();
        bodyContainer = new StackPane(recordButton);
        
        //button place at center of page
        this.setTop(topContainer);
        this.setCenter(bodyContainer);

        cancelButton.setOnAction(event -> Main.setPage(Main.temp));
        recordButton.setOnMousePressed(event -> startRecording());
        recordButton.setOnMouseReleased(event -> stopRecordingAndProcessMealType());
        //button place at bottom center of page
        //this.setAlignment(cancelButton, Pos.CENTER);

        
    }

    // Returns the audio format to use for the recording for Specify Ingredient Page and Specify Meal Type Page
    // NOTE: This is the same format that is used for the Whisper transcribeAudio method
    private void startRecording() {
        try {
            System.out.println("Starting to Record");
            audioFormat = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            Thread recordingThread = new Thread(() -> {
                try {
                    AudioInputStream inputStream = new AudioInputStream(targetDataLine);
                    File audioFile = new File(TEMP_AUDIO_FILE_PATH);
                    AudioSystem.write(inputStream, AudioFileFormat.Type.WAVE, audioFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            recordingThread.start();
            System.out.println("Recording started...");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Returns the audio format to use for the recording for Specify Ingredient Page and Specify Meal Type Page
    private void stopRecordingAndProcessMealType() {
        if (targetDataLine == null) {
            return;
        }
    
        targetDataLine.stop();
        targetDataLine.close();
        System.out.println("Recording stopped.");
    
        try {
            String transcribedText = whisper.transcribeAudio(TEMP_AUDIO_FILE_PATH);
            System.out.println("Transcription: " + transcribedText);
    
            mealType = detectMealType(transcribedText);
    
            if (mealType != null) {
                Main.setPage(new SpecifyIngredientPage(mealType, chatGPT, whisper));
            } else {
                System.out.println("Please try again");
                // Display error message on UI here
                Main.setPage(new SpecifyMealTypePage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }
    
    // Returns the meal type if it is found in the transcribed text, otherwise returns null (Helper Function)
    private String detectMealType(String transcribedText) {
        String[] mealTypes = {"breakfast", "lunch", "dinner"};
        
        for (String meal : mealTypes) {
            if (transcribedText.toLowerCase().contains(meal)) {
                return meal;
            }
        }
        return null;
    }
    
    // Returns the audio format to use for the recording for Specify Ingredient Page and Specify Meal Type Page
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000; // typically 44100 for music
        int sampleSizeInBits = 16;
        int channels = 1; // mono
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}
