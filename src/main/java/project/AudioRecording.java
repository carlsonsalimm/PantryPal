package project;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class AudioRecording extends VBox {
    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    private String recorderButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button recorderButton;
    public String mealType;
    private String errorMsgStyle = "-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: #DF0000;";
    private Text errorMsg;
    private Boolean errorFlag = false;

    // Constructor for Specify Meal Type Page
    AudioRecording() {
        // Create a button that the user can press and hold to record
        this.setPrefSize(300, 500);
        this.setStyle("-fx-background-color: #FFFFFF;");

        recorderButton = new Button("Hold to Record");
        recorderButton.setStyle(recorderButtonStyle);
            
        recorderButton.setPrefSize(300, 50);
        this.getChildren().add(recorderButton);
    
        // Set the button actions for mouse press and release
        recorderButton.setOnMousePressed(event -> startRecording());
        recorderButton.setOnMouseReleased(event -> stopRecordingAndProcessMealType());
    }

    // Constructor for Specify Ingredient Page
    AudioRecording(String mealType) {
        // Create a button that the user can press and hold to record
        this.setPrefSize(100, 100);
        this.setStyle("-fx-background-color: #FFFFFF;");

        recorderButton = new Button("Hold to Record");

        this.getChildren().add(recorderButton);

        // Set the button actions for mouse press and release
        recorderButton.setOnMousePressed(event -> startRecording());
        recorderButton.setOnMouseReleased(event -> stopRecordingAndProcessRecipe());
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
                Whisper whisper = new Whisper();

                String transcribedText = whisper.transcribeAudio(TEMP_AUDIO_FILE_PATH);
                System.out.println("Transcription: " + transcribedText);

                // Send the transcribed text to ChatGPT and get a response
                ChatGPT chatGPT = new ChatGPT();

                String response = chatGPT.getGPTResponse(transcribedText, mealType);
                System.out.println("ChatGPT Response: " + response);

            
                Main.setPage(new DetailedRecipePage(createRecipe(response)));

                // Handle the UI update or user notification with the generated recipe response
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions appropriately
            }
        }
    }

    public static Recipe createRecipe(String gptResponse) {
        String recipeTitle = gptResponse.substring(0, gptResponse.indexOf("\n"));
        String recipeInstructions = gptResponse.substring(gptResponse.indexOf("\n"));

        Recipe recipe = new Recipe(recipeTitle, recipeInstructions);
        return recipe;
    }

    

    // Returns the audio format to use for the recording for SpecifyMealTypePage
    // and Specify Meal Type Page
    private void stopRecordingAndProcessMealType() {
        if (targetDataLine == null) {
            return;
        }

        targetDataLine.stop();
        targetDataLine.close();
        System.out.println("Recording stopped.");

        try {
            Whisper whisper = new Whisper();
            String transcribedText = whisper.transcribeAudio(TEMP_AUDIO_FILE_PATH);
            System.out.println("Transcription: " + transcribedText);

            String mealType = detectMealType(transcribedText);

            if (mealType != null) {
               // Main.setPage(new SpecifyIngredientPage(mealType));
            } else {
                System.out.println("Please try again");

                if(!errorFlag) {
                    // Add label child to layout here
                    errorMsg = new Text("Sorry, we didn't catch that. Please Try Again");
                    errorMsg.setStyle(errorMsgStyle);
                    this.getChildren().add(errorMsg);
                    errorFlag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }

    // Returns the meal type if it is found in the transcribed text, otherwise
    // returns null (Helper Function)
    public static String detectMealType(String transcribedText) {
        String[] mealTypes = { "breakfast", "lunch", "dinner" };

        for (String meal : mealTypes) {
            if (transcribedText.toLowerCase().contains(meal)) {
                return meal;
            }
        }
        return null;
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