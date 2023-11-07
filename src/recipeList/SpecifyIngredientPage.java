package recipeList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class SpecifyIngredientPage {

    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";

    public void start(Stage primaryStage) {
        // Create a button that the user can press and hold to record
        Button recordButton = new Button("Hold to Record");
        recordButton.getStyleClass().add("record-button");

        // Set the button actions for mouse press and release
        recordButton.setOnMousePressed(event -> startRecording());
        recordButton.setOnMouseReleased(event -> stopRecordingAndProcess());

        // Set up the scene and stage
        StackPane root = new StackPane(recordButton);
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("PantryPal - Recipe Recorder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startRecording() {
        try {
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
