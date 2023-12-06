package project;

import org.junit.jupiter.api.Test;

import javafx.event.ActionEvent;

import org.bson.Document;
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

public class MS2Testing {
    Model model = new Model();
    

    @Test
    void testValidLogin() throws IOException, InterruptedException, URISyntaxException {

        MockLoginPageController controller = new MockLoginPageController(model);
        String username = "carl";
        String password = "1234";

        controller.setUsername(username);
        controller.setPassword(password);
        Boolean result = controller.handleSignInButton(new ActionEvent());
        assertEquals(true, result);

    }

    @Test
    void testInvalidLogin() throws IOException, InterruptedException, URISyntaxException {

        MockLoginPageController controller = new MockLoginPageController(model);
        String username = "carl";
        String password = "4109247189274891";

        controller.setUsername(username);
        controller.setPassword(password);
        Boolean result = controller.handleSignInButton(new ActionEvent());

        assertEquals(false, result);

    }

    @Test
    void testValidAccountCreation() throws IOException, InterruptedException, URISyntaxException {
        // TODO make sure to delete account after
        MockLoginPageController controller = new MockLoginPageController(model);
        String username = "deleteTest";
        String password = "passpass";

        controller.setUsername(username);
        controller.setPassword(password);
        Boolean result = controller.handleCreateAccountButton(new ActionEvent());
        MongoDBProject.deleteUser(username, password);

        assertEquals(true, result);
    }

    @Test
    void testInvalidAccountCreation() throws IOException, InterruptedException, URISyntaxException {
        MockLoginPageController controller = new MockLoginPageController(model);
        String username = "carl";
        String password = "passpass";

        controller.setUsername(username);
        controller.setPassword(password);
        Boolean result = controller.handleSignInButton(new ActionEvent());

        assertEquals(false, result);
    }

    @Test
    void testSendAudio() throws IOException, InterruptedException, URISyntaxException {
        MockWhisper whisper = new MockWhisper();
        String response = whisper.transcribeAudio("testType.wav");
        assertEquals(response, "Dinner.");
    }

    @Test
    void testGetRecipeList() {
        model.setUsername("carl");
        model.setPassword("1234");
        String response = model.performRequest("GET", "getRecipeList",
                null, null,
                null, null,
                null, null,
                null, null, null);
        assertEquals(
                "{\"0\" :{\"recipeTitle\": \"chicken thigh\", \"mealType\": \"lunch\", \"ingredients\": \"chicken thigh\", \"instructions\": \"cook in a pan\", \"creationTime\": 1701677928859},\"1\" :{\"recipeTitle\": \"beef brocolli\", \"mealType\": \"dinner\", \"ingredients\": \"beef and brocolli\", \"instructions\": \"cook in wok\", \"creationTime\": 1701677929859}}",
                response);
    }

    @Test
    void testEncodeDecodeEmpty() {
        String orig = "";
        String encoded = model.encodeURL(orig);
        String decoded = model.decodeURL(encoded);

        assertEquals(orig, decoded);
    }

    @Test
    void testEncodeDecodeNotEmpty() {
        String orig = "jfdskaluiowearewa\'\"fdsajiej\nfjdsiajei";
        String encoded = model.encodeURL(orig);
        String decoded = model.decodeURL(encoded);

        assertEquals(orig, decoded);
    }

    @Test
    void testSort() throws IOException{
        MockRecipeListPageController controller = new MockRecipeListPageController(model);
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("eggs",null,null,"dinner", "0"));
        recipes.add(new Recipe("bread",null,null,"lunch", "5"));
        recipes.add(new Recipe("sausage",null,null,"breakfast", "2"));
        recipes.add(new Recipe("bacon",null,null,"breakfast", "1"));


        List<Recipe> AtoZ = new ArrayList<>();
        AtoZ.add(new Recipe("bacon",null,null,"breakfast", "1"));
        AtoZ.add(new Recipe("bread",null,null,"lunch", "5"));
        AtoZ.add(new Recipe("eggs",null,null,"dinner", "0"));
        AtoZ.add(new Recipe("sausage",null,null,"breakfast", "2"));
        
        controller.setSort("A-Z");
        controller.setRecipes(recipes);
        assertTrue(controller.handleSortBoxButton(new ActionEvent()).equals(AtoZ));

        List<Recipe> ZtoA = new ArrayList<>();
        ZtoA.add(new Recipe("sausage",null,null,"breakfast", "2"));
        ZtoA.add(new Recipe("eggs",null,null,"dinner", "0"));
        ZtoA.add(new Recipe("bread",null,null,"lunch", "5"));
        ZtoA.add(new Recipe("bacon",null,null,"breakfast", "1"));

        controller.setSort("Z-A");
        controller.setRecipes(recipes);
        assertTrue(controller.handleSortBoxButton(new ActionEvent()).equals(ZtoA));

        List<Recipe> OldtoFirst = new ArrayList<>();
        OldtoFirst.add(new Recipe("bread",null,null,"lunch", "5"));
        OldtoFirst.add(new Recipe("sausage",null,null,"breakfast", "2"));
        OldtoFirst.add(new Recipe("bacon",null,null,"breakfast", "1"));
        OldtoFirst.add(new Recipe("eggs",null,null,"dinner", "0"));

        controller.setSort("Oldest first");
        controller.setRecipes(recipes);
        assertTrue(controller.handleSortBoxButton(new ActionEvent()).equals(OldtoFirst));

        List<Recipe> FirsttoOld = new ArrayList<>();
        FirsttoOld.add(new Recipe("eggs",null,null,"dinner", "0"));
        FirsttoOld.add(new Recipe("bacon",null,null,"breakfast", "1"));
        FirsttoOld.add(new Recipe("sausage",null,null,"breakfast", "2"));
        FirsttoOld.add(new Recipe("bread",null,null,"lunch", "5"));
        
        controller.setSort("First Oldest");
        controller.setRecipes(recipes);
        assertTrue(controller.handleSortBoxButton(new ActionEvent()).equals(FirsttoOld));
    }

      @Test
    void testFilter() throws IOException{
        MockRecipeListPageController controller = new MockRecipeListPageController(model);
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("eggs",null,null,"dinner", "0"));
        recipes.add(new Recipe("bread",null,null,"lunch", "5"));
        recipes.add(new Recipe("sausage",null,null,"breakfast", "2"));
        recipes.add(new Recipe("bacon",null,null,"breakfast", "1"));

        controller.setMealType("Breakfast");
        controller.setRecipes(recipes);
        assertEquals(2, controller.handleFilterBoxButton(new ActionEvent()).size());

        controller.setMealType("Lunch");
        controller.setRecipes(recipes);
        assertEquals(1, controller.handleFilterBoxButton(new ActionEvent()).size());

        controller.setMealType("Dinner");
        controller.setRecipes(recipes);
        assertEquals(1, controller.handleFilterBoxButton(new ActionEvent()).size());
    }



}
