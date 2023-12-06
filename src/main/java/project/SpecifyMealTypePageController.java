package project;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.sound.sampled.*;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class SpecifyMealTypePageController implements Controller {
    private SpecifyMealTypePage view;
    private Model model;

    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    public String mealType;
    private Boolean errorFlag = false;

    public SpecifyMealTypePageController(SpecifyMealTypePage view ,Model model){
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

        this.view.setBackButtonAction(event -> {
            try {
                handleBackButton(event);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    // Returns the audio format to use for the recording for Specify Ingredient Page
    // and Specify Meal Type Page
    // NOTE: This is the same format that is used for the Whisper transcribeAudio
    // method

    public void handleRecordHoldButton(MouseEvent event) throws IOException{
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
    
    public String handleRecordReleasetButton(MouseEvent event) throws IOException{
        if (targetDataLine == null) {
            return null;
        }

        targetDataLine.stop();
        targetDataLine.close();
        System.out.println("Recording stopped.");

        try {
            String transcribedText = model.performRequest("POST",null,null,null,TEMP_AUDIO_FILE_PATH,null,null,null,null,null,null);
            System.out.println("Transcription: " + transcribedText);

            String mealType = detectMealType(transcribedText);

            if (mealType != null) {
                SpecifyIngredientsPage temp = new SpecifyIngredientsPage(mealType);
                Main.setPage(temp);
                Main.setController(new SpecifyIngredientsPageController(temp,model, mealType));
            } else {
                System.out.println("Please try again");

                if(!errorFlag) {
                    // Add label child to layout here
                    view.errorMsg();
                    errorFlag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Main.showAlert("Error", "Server temporarily unavailable. Please try again later."); 
            // Handle exceptions appropriately
        } 
        return null;
    }

    public boolean handleBackButton(ActionEvent event) throws IOException{
        try {
        // Add Recipe Information
         String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null, null);
        List<Recipe> recipes = Main.extractRecipeInfo(JSON);
        RecipeListPage listPage = new RecipeListPage(recipes);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
        return true;
        } catch (Exception e) {
            Main.showAlert("Error", "Server temporarily unavailable. Please try again later."); 
            return false;
        }
    }


    // Returns the meal type if it is found in the transcribed text, otherwise
    // returns null (Helper Function)
    public String detectMealType(String transcribedText) {
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

   
