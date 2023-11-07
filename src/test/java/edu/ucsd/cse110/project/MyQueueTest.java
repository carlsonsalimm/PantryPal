// Replace with Our files
package test.java.edu.ucsd.cse110.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyCreationTest {
    private Recipe recipe;
    
    @Test
    void testValidInput() {
        Voice("Make dinner");
        assertEquals(1, recipe.getRecipe());
    }
    

    @Test
    void testUpdate() {
        Recipe spaghetti = new Recipe();
        spaghetti.name = "spaghetti";
        spaghetti.ingredient = "pasta";
        Voice("make Dinner");
        asserEquals(spaghetti, recipe.getRecipe("spaghetti"));
    }
}
