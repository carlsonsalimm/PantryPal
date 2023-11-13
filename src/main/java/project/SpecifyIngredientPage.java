package project;

import java.io.*;

import javax.sound.sampled.*;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

// import org.junit.platform.reporting.shadow.org.opentest4j.reporting.events.core.Sources;

public class SpecifyIngredientPage extends BorderPane {

    private String mealType;
    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    private String recorderButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button recordButton;
    private ChatGPT chatGPT;
    private Whisper whisper;

    // Constructor for Specify Ingredient Page
    SpecifyIngredientPage(String mealType, ChatGPT chatGPT, Whisper whisper) {
        recordButton = new Button();
        recordButton.setStyle(recorderButtonStyle);
        this.chatGPT = chatGPT;
        this.whisper = whisper;
        this.mealType = mealType;
        this.getChildren().add(recordButton);
        this.setCenter(recordButton);

        addListeners();
    }

    public void addListeners() {
        // Delete recipe and go back to RecipeListPage
        recordButton.setOnMousePressed(event -> startRecording());
        recordButton.setOnMouseReleased(event -> stopRecordingAndProcessRecipe());
    }

    // Returns the audio format to use for the recording for Specify Ingredient Page
    // and Specify Meal Type Page
    // NOTE: This is the same format that is used for the Whisper transcribeAudio
    // method
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

    // Returns the audio format to use for the recording for Specify Ingredient Page
    // NOTE: This is the same format that is used for the Whisper transcribeAudio
    // method
    private void stopRecordingAndProcessRecipe() {
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
            System.out.println("Recording stopped.");

            try {
                // Transcribe the audio file to text using Whisper
                String transcribedText = this.whisper.transcribeAudio(TEMP_AUDIO_FILE_PATH);
                System.out.println("Transcription: " + transcribedText);

                // Send the transcribed text to ChatGPT and get a response
                String response = this.chatGPT.getGPTResponse(transcribedText, mealType);
                System.out.println("ChatGPT Response: " + response);

                String recipeTitle = response.substring(0, response.indexOf("\n"));
                String recipeInstructions = response.substring(response.indexOf("\n"));

                Recipe newRecipe = new Recipe(recipeTitle, recipeInstructions);

                Main.setPage(new DetailedRecipePage(newRecipe));

                // Handle the UI update or user notification with the generated recipe response
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions appropriately
            }
        }
    }

    // Returns the audio format to use for the recording for Specify Ingredient Page
    // and Specify Meal Type Page
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000; // typically 44100 for music
        int sampleSizeInBits = 16;
        int channels = 1; // mono
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

}
