package project;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

public class MockSpecificIngredientsPageController implements Controller{
    private Model model;

    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    public String mealType;
    private String transcribedText;

    public MockSpecificIngredientsPageController(Model model){
        this.model = model;
    }

    public void setMealType(String meal){
        this.mealType = meal;
    }

    public void setTranscribedText(String text){
        this.transcribedText = text;
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

    public Pair<Recipe,String> handleRecordReleasetButton(MouseEvent event) throws IOException{
     
           
           System.out.println("Recording stopped.");

           try {
               
               // Transcripe Audio
               System.out.println("Transcription: " + transcribedText);

               // Send the transcribed text to ChatGPT and get a response
               String response = "Cereal\nMilk,Cereal";
               System.out.println("ChatGPT Response: " + response);
                MockDallE mock = new MockDallE();
               Pair<Recipe,String> result= new Pair<>(createRecipe(response), mock.generateImageURL("test"));
               return result;

               // Handle the UI update or user notification with the generated recipe response
           } catch (Exception e) {
               e.printStackTrace();
               // Handle exceptions appropriately
           }
      return null;
     
    }

    public boolean handleCancelButton(ActionEvent event) throws IOException{
        return true;
    }
    
    public Recipe createRecipe(String gptResponse) {
        String recipeTitle = gptResponse.substring(0, gptResponse.indexOf("\n"));
        String recipeInstructions = gptResponse.substring(gptResponse.indexOf("\n"));

        Recipe recipe = new Recipe(recipeTitle, recipeInstructions, transcribedText, mealType,null);
        return recipe;
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
