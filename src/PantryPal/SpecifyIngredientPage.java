package PantryPal;

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

// import org.junit.platform.reporting.shadow.org.opentest4j.reporting.events.core.Sources;

public class SpecifyIngredientPage extends BorderPane {

    private RecordButton recordButton;

    SpecifyIngredientPage() {
        recordButton = new RecordButton();

        this.setCenter(recordButton);
    }

}

class RecordButton extends HBox {
    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    private Button recorderButton;

    RecordButton() {
        // Create a button that the user can press and hold to record
        this.setPrefSize(100, 100);
        this.setStyle("-fx-background-color: #FFFFFF;");

        recorderButton = new Button("Hold to Record");

        this.getChildren().add(recorderButton);

        // Set the button actions for mouse press and release
        recorderButton.setOnMousePressed(event -> startRecording());
        recorderButton.setOnMouseReleased(event -> stopRecordingAndProcess());
    }

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

    private void stopRecordingAndProcess() {
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
            System.out.println("Recording stopped.");

            try {
                // Transcribe the audio file to text using Whisper
                String transcribedText = Whisper.transcribeAudio(TEMP_AUDIO_FILE_PATH);
                System.out.println("Transcription: " + transcribedText);

                // Send the transcribed text to ChatGPT and get a response
                String response = ChatGPT.getGPTResponse(transcribedText);
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

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000; // typically 44100 for music
        int sampleSizeInBits = 16;
        int channels = 1; // mono
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}
