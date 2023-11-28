package project;

import org.junit.jupiter.api.Test;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyCreationTest {
    Recipe recipe1 = new Recipe("scrambled eggs", "heat pan then scramble desired amount of eggs");
    Recipe recipe2 = new Recipe("sunnyside eggs", "just eggs");
    MockGPT gpt = new MockGPT();
    MockWhisper whisper = new MockWhisper();
    
    // Tests with non-trivial testing-class coverage
    // Tests if a valid output is given
    @Test
    void testValidInput() throws IOException, InterruptedException, URISyntaxException {
        String result = gpt.getGPTResponse("eggs", "breakfast").toString();
        assertTrue(result != null);
    }
    
    // Test for Duplicates
    @Test
    void testUpdate() throws IOException {
        int size = CSVHandler.readRecipes(null, null).size();
        if(CSVHandler.writeRecipes(null, null, recipe1)){
            size += 1;
        }
        CSVHandler.writeRecipes(null, null, recipe1);
        
        assertEquals(size , CSVHandler.readRecipes(null, null).size());
    }


    // Test voice-command to recipe construction for Recipe Creation
    @Test
    void testRecipeCreation() throws IOException, JSONException, URISyntaxException, InterruptedException{
        String type = whisper.transcribeAudio("testType.wav");
        String ingredients = whisper.transcribeAudio("testIngredients.wav");
        String gptResponse = gpt.getGPTResponse(ingredients, type);
        Recipe testRecipe = AudioRecording.createRecipe(gptResponse);

        assertEquals("Test title from MockGPT", testRecipe.getTitle());
        assertEquals("\nTest instructions from MockGPT", testRecipe.getInstructions());
    }

    // Test that meal type check passes when meal type is valid
    @Test
    void testMealTypeCheckValid() throws IOException, JSONException, URISyntaxException{
        String type = whisper.transcribeAudio("testType.wav"); // -> Dinner.
        String detectedMealType = AudioRecording.detectMealType(type);
        assertEquals("dinner", detectedMealType);
    }

    // Test that meal type check fails when meal type is invalid
    @Test
    void testMealTypeCheckInvalid() throws IOException, JSONException, URISyntaxException{
        String detectedMealType = AudioRecording.detectMealType("invalid");
        assertEquals(null, detectedMealType);
    }

    // Test Detailed Recipe Display 
    @Test
    void testRecipeDisplay() throws IOException{
        
        CSVHandler.clearAll(null, null);
        CSVHandler.writeRecipes(null, null, recipe2);
       
        assertEquals(recipe2.getTitle(), CSVHandler.readRecipes(null, null).get(0).getTitle());
        assertEquals(recipe2.getInstructions(), CSVHandler.readRecipes(null, null).get(0).getInstructions());
    }

    // Test Recipe Saving
    @Test
    void testRecipeSaving() throws IOException{
        CSVHandler.clearAll(null, null);
        CSVHandler.writeRecipes(null, null, recipe1);
       
        assertEquals(recipe1.getTitle(), CSVHandler.readRecipes().get(0).getTitle());
        assertEquals(recipe1.getInstructions(), CSVHandler.readRecipes().get(0).getInstructions());
        
    }

    // Test View List of Recipes
    @Test
    void testViewList() throws IOException{
        CSVHandler.clearAll();
        CSVHandler.writeRecipes(recipe1);
        CSVHandler.writeRecipes(recipe2);


        List<String> expected = new ArrayList<String>();
        expected.add(recipe2.getTitle());
        expected.add(recipe1.getTitle());
        
        List<String> actual = new ArrayList<String>();
        List<Recipe> recipes = CSVHandler.readRecipes();
        for(Recipe r: recipes){
            actual.add(r.getTitle());
        }

        assertEquals(expected, actual);
        
    }

    // Test Recipe Editing
    @Test
    void testEditing() throws IOException{
        CSVHandler.clearAll();
        CSVHandler.writeRecipes(recipe1);

        CSVHandler.updateRecipe(recipe1, recipe2);

        assertEquals(recipe2.getInstructions(), CSVHandler.readRecipes().get(0).getInstructions());
        
    }

    // Test Recipe Deletion
    @Test
    void testRecipeDeletion() throws IOException{
        CSVHandler.writeRecipes(recipe2);
        //Size after insertion
        int size = CSVHandler.readRecipes().size();
        

        CSVHandler.deleteRecipe(recipe2);

        assertEquals(size-1, CSVHandler.readRecipes().size());
    }

    // Deletion when updating a recipe
    @Test
    void testCombination() throws IOException{
         CSVHandler.clearAll();
         CSVHandler.writeRecipes(recipe2);
         CSVHandler.updateRecipe(recipe2, recipe1);

         Recipe combine = new Recipe("sunnyside eggs", "heat pan then scramble desired amount of eggs");
         CSVHandler.deleteRecipe(combine);

         assertEquals(0, CSVHandler.readRecipes().size());
    }

    @Test
    void testDeletion() throws IOException{
        CSVHandler.clearAll();
        CSVHandler.writeRecipes(recipe2);

        assertFalse(CSVHandler.deleteRecipe(recipe1));
    }
    
}

