package project;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.*;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class MockSpecifyMealTypePageController implements Controller{
    private SpecifyMealTypePage view;
    private Model model;
    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    public String mealType;
    private Boolean errorFlag = false;
    public String transcribedText;

    public MockSpecifyMealTypePageController(Model model){
        this.model = model;
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

    public String handleRecordReleasetButton(MouseEvent event) throws IOException{
        


        System.out.println("Recording stopped.");

        try {
            System.out.println("Transcription: " + transcribedText);

            String mealType = detectMealType(transcribedText);

            if (mealType != null) {
                return mealType;
            } else {
                System.out.println("Please try again");

               
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        } 
        return null;
    }

    public boolean handleBackButton(ActionEvent event) throws IOException{


        return true;
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

