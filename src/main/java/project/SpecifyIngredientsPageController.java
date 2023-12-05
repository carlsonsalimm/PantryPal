package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;

import org.json.Cookie;
import javax.sound.sampled.*;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class SpecifyIngredientsPageController implements Controller{
    private SpecifyIngredientsPage view;
    private Model model;

    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    private String recorderButtonStyle = "-fx-background-radius: 100; -fx-font-style: italic; -fx-background-color: #D9D9D9;  -fx-font-weight: bold; -fx-font: 18 arial;";
    private Button recorderButton;
    public String mealType;
    private String errorMsgStyle = "-fx-font-size: 20;-fx-font-weight: bold; -fx-text-fill: #DF0000;";
    private Text errorMsg;
    private Boolean errorFlag = false;

    public SpecifyIngredientsPageController(SpecifyIngredientsPage view ,Model model){
        this.view = view;
        this.model = model;

        this.view.setRecordHoldAction(event -> {
            try {
                handleRecordHoldButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });


        this.view.setRecordReleaseAction(event -> {
            try {
                handleRecordReleasetButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        this.view.setCancelButtonAction(event -> {
            try {
                handleCancelButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    private void handleRecordHoldButton(MouseEvent event) throws IOException{
        startRecording();
       
    }

    private void handleRecordReleasetButton(MouseEvent event) throws IOException{
        stopRecordingAndProcessRecipe();
      
    }

    private void handleCancelButton(ActionEvent event) throws IOException{

        // Add Recipe Information
        String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null);
        List<Recipe> recipes = Main.extractRecipeInfo(JSON);
        RecipeListPage listPage = new RecipeListPage(recipes);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
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

    // Returns the audio format to use for the recording for SpecifyMealTypePage
    // and Specify Meal Type Page
    private void stopRecordingAndProcessRecipe() {
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
            System.out.println("Recording stopped.");

            try {
                
                // Transcripe Audio
                String transcribedText = model.performRequest("POST",null,null,null,TEMP_AUDIO_FILE_PATH,null,null,null,null,null);
                System.out.println("Transcription: " + transcribedText);

                // Send the transcribed text to ChatGPT and get a response
                String response = model.performRequest("POST","generateRecipe",null,null,null, mealType, transcribedText,null,null,null);
                System.out.println("ChatGPT Response: " + response);

                DetailedRecipePage temp = new DetailedRecipePage(createRecipe(response), true);
                Main.setPage(temp);
                Main.setController(new DetailedRecipePageController(temp,model));

                // Handle the UI update or user notification with the generated recipe response
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions appropriately
            }
        }
    }
    
    public Recipe createRecipe(String gptResponse) {
        List<Recipe> recipe = Main.extractRecipeInfo(gptResponse);
        return recipe.get(0);
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

   
