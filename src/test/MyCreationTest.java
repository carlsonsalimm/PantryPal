package test;

import PantryPal.ChatGPT;
import PantryPal.CSVHandler;
import PantryPal.Recipe;

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
        String result = ChatGPT.getGPTResponse("eggs");
        assertTrue(result != null);
    }
    
    // Test for Duplicates
    @Test
    void testUpdate() throws IOException {
        Recipe eggs = new Recipe("eggs", "instructions");
        CSVHandler.writeRecipes(eggs);
        CSVHandler.writeRecipes(eggs);

        assertEquals(1 , CSVHandler.readRecipes().size());
    }
}
