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
    ChatGPT prompt = new ChatGPT();
    Recipe recipe1 = new Recipe("scrambled eggs", "heat pan then scramble desired amount of eggs");
    Recipe recipe2 = new Recipe("sunnyside eggs", "just eggs");
    
    // Tests with non-trivial testing-class coverage
    // Tests if a valid output is given
    @Test
    void testValidInput() throws IOException, InterruptedException, URISyntaxException {
        String result = ChatGPT.getGPTResponse("eggs", "breakfast").toString();
        assertTrue(result != null);
    }
    
    // Test for Duplicates
    @Test
    void testUpdate() throws IOException {
        int size = CSVHandler.readRecipes().size();
        if(CSVHandler.writeRecipes(recipe1)){
            size += 1;
        }
        CSVHandler.writeRecipes(recipe1);
        
        assertEquals(size , CSVHandler.readRecipes().size());
    }


    // Test voice-command for Recipe Creation
    @Test
    void testVoiceCommand() throws IOException, JSONException, URISyntaxException, InterruptedException{
        String type = Whisper.transcribeAudio("testType.wav");
        String ingredients = Whisper.transcribeAudio("testIngredients.wav");
        String response = ChatGPT.getGPTResponse(ingredients,type);
        assertEquals("dinner", response); // replace dinner with expected gpt response
    }

    // Test voice-command to recipe construction for Recipe Creation
    @Test
    void testRecipeCreation() throws IOException, JSONException, URISyntaxException, InterruptedException{
        
    }

    // Test Meal Type Selection
    @Test
    void testMealType() throws IOException, JSONException, URISyntaxException{
        String type = Whisper.transcribeAudio("testType.wav");

        assertEquals("dinner", type);
    }

    // Test Detailed Recipe Display 
    // TODO
    @Test
    void testRecipeDisplay() throws IOException{
        //recipe.saveRecipe(recipe1);
        Main.launch(null);
        assertEquals(recipe1.getTitle(), detailedTitle);
        assertEquals(recipe1.getInstructions(), detailedInstruction);
    }

    // Test Recipe Saving
    @Test
    void testRecipeSaving() throws IOException{
        CSVHandler.clearAll();
        CSVHandler.writeRecipes(recipe1);
       
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
        Recipe recipe2 = new Recipe("hotdog", "bun");
        CSVHandler.writeRecipes(recipe2);
        //Size after insertion
        int size = CSVHandler.readRecipes().size();
        

        CSVHandler.deleteRecipe(recipe2);

        assertEquals(size-1, CSVHandler.readRecipes().size());
    }

    // Test Scalable to work on multiple platforms 
    @Test
    void testScalable() throws IOException{
        
    }

    // Integration Tests
}

