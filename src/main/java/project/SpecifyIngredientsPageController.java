package project;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.*;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class SpecifyIngredientsPageController implements Controller {
    private SpecifyIngredientsPage view;
    private Model model;

    private TargetDataLine targetDataLine;
    private AudioFormat audioFormat;
    private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
    public String mealType;
    private String transcribedText;

    public SpecifyIngredientsPageController(SpecifyIngredientsPage view, Model model, String mealType) {
        this.view = view;
        this.model = model;
        this.mealType = mealType;

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

    // Returns the audio format to use for the recording for Specify Ingredient Page
    // and Specify Meal Type Page
    // NOTE: This is the same format that is used for the Whisper transcribeAudio
    // method

    public void handleRecordHoldButton(MouseEvent event) throws IOException {
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

    public Recipe handleRecordReleasetButton(MouseEvent event) throws IOException {
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
            System.out.println("Recording stopped.");

            try {

                // Transcripe Audio
                String transcribed = model.performRequest("POST", null, null, null, TEMP_AUDIO_FILE_PATH, null,
                        null, null, null, null, null);

                if (transcribed == null || transcribed.isEmpty()) {
                    transcribed = "No ingredients";
                }
                this.transcribedText = transcribed;
                System.out.println("Transcription: " + transcribedText);

                // Send the transcribed text to ChatGPT and get a response
                String response = model.performRequest("POST", null, null, null, null, mealType, transcribedText, null,
                        null, null, null);
                System.out.println("ChatGPT Response: " + response);
                Recipe recipe = createRecipe(response);
                DetailedRecipePage temp = new DetailedRecipePage(recipe, true);
                Main.setPage(temp);
                Main.setController(new DetailedRecipePageController(temp, model));
                return recipe;
                // Handle the UI update or user notification with the generated recipe response
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions appropriately
            }
        }
        return null;
    }

    private boolean handleCancelButton(ActionEvent event) throws IOException {

        // Add Recipe Information
        String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null,
                null);
        List<Recipe> recipes = Main.extractRecipeInfo(JSON);
        RecipeListPage listPage = new RecipeListPage(recipes);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
        return true;
    }

    // public Recipe createRecipe(String gptResponse) {
    // String recipeTitle = gptResponse.substring(0, gptResponse.indexOf("\n"));
    // String recipeInstructions = gptResponse.substring(gptResponse.indexOf("\n"));

    // Recipe recipe = new Recipe(recipeTitle, recipeInstructions, transcribedText,
    // mealType);
    // return recipe;
    // }

    public Recipe createRecipe(String gptResponse) {
        int firstNewLineIndex = gptResponse.indexOf("\n");

        // Check if the newline character is present
        if (firstNewLineIndex == -1) {
            // Handle the case where there is no newline character
            // For example, you can set the title to the entire response
            // and set the instructions to an empty string or some default value
            return new Recipe(gptResponse, "", transcribedText, mealType);
        } else {
            // Split the response into lines
            String[] lines = gptResponse.split("\n");

            // Extract the recipe title
            String recipeTitle = lines[0];

            // Initialize StringBuilder for ingredients and instructions
            StringBuilder ingredients = new StringBuilder();
            StringBuilder instructions = new StringBuilder();

            // Boolean flag to switch from reading ingredients to instructions
            boolean readingInstructions = false;

            // Iterate over the lines to separate ingredients and instructions
            for (int i = 1; i < lines.length; i++) {
                // Check if the line indicates the start of instructions
                if (lines[i].trim().equals("Instructions:")) {
                    readingInstructions = true;
                    continue;
                }

                // Append the line to the appropriate StringBuilder
                if (readingInstructions) {
                    instructions.append(lines[i]).append("\n");
                } else {
                    ingredients.append(lines[i]).append("\n");
                }
            }

            // Request a new image URL from the server
            String newImageURLResponse = model.performRequest("GET", "generateImage", null, null, null, null, null,
                    recipeTitle, null, null, null);

            // Extracting the URL from the response
            String newImageURL = newImageURLResponse.startsWith("{") ? newImageURLResponse.split("\"")[3]
                    : newImageURLResponse;

            return new Recipe(recipeTitle, instructions.toString().trim(), transcribedText, mealType, null,
                    newImageURL);
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
