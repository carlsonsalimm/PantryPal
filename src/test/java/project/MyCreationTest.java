package project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyCreationTest {
    CSVHandler csv = new CSVHandler();
    ChatGPT prompt = new ChatGPT();
    
    // Tests if a valid output is given
    @Test
    void testValidInput() throws IOException, InterruptedException, URISyntaxException {
        String result = ChatGPT.getGPTResponse("eggs", "breakfast").toString();
        assertTrue(result != null);
    }
    
    // Test for Duplicates
    @Test
    void testUpdate() throws IOException {
        Recipe eggs = new Recipe("eggs", "instructions");
        int size = CSVHandler.readRecipes().size();

        if(CSVHandler.writeRecipes(eggs)){
            size += 1;
        }
        CSVHandler.writeRecipes(eggs);
        
        assertEquals(size , CSVHandler.readRecipes().size());
    }

    // Test Recipe Creation
    @Test
    void testCreate() throws IOException{

    }

    // Test Meal Selection
    @Test
    void testMealType() throws IOException{

    }

    // Test Recipe Display
    @Test
    void testDisplay() throws IOException{

    }

    // Test Save Fucntionality
    @Test
    void testCreate() throws IOException{

    }

    // Test Recipe Creation
    @Test
    void testCreate() throws IOException{

    }

    // Test Recipe Creation
    @Test
    void testCreate() throws IOException{

    }
}
