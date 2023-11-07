package PantryPal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyCreationTest {
    ChatGPT prompt = new ChatGpt();
    
    @Test
    void testValidInput() {
        //Voice("Make dinner");
        prompt.getGPTResponse("eggs")
        assertEquals(1, 1);
    }
    

   /*  @Test
    void testUpdate() {
        Recipe spaghetti = new Recipe();
        spaghetti.name = "spaghetti";
        spaghetti.ingredient = "pasta";
        Voice("make Dinner");
        asserEquals(spaghetti, recipe.getRecipe("spaghetti"));
    }*/
}
