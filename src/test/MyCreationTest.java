package PantryPal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyCreationTest {
    CSVHandler csv = new CSVHander();
    ChatGPT prompt = new ChatGpt();
    
    // Tests if a valid output is given
    @Test
    void testValidInput() {
        assertTrue(prompt.getGPTResponse("eggs") != NULL);
    }
    
    // Test for Duplicates
    @Test
    void testUpdate() {
        csv.writeRecipes("eggs");
        csv.writeRecipes("eggs");

        assertEquals(1 , csv.readRecipes().size());
    }
}
