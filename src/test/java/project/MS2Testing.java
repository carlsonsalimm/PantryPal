package project;

import org.junit.jupiter.api.Test;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import org.bson.Document;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;
import java.io.BufferedReader;
import java.io.FileReader;
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


        String[] AtoZ = new String[]{"bacon", "bread", "eggs", "sausage"};
        String[] actualAtoZ = new String[4];
        controller.setSort("A-Z");
        controller.setRecipes(recipes);
        int i = 0;
        for(Recipe recipe:  controller.handleSortBoxButton(new ActionEvent())){
            actualAtoZ[i] = recipe.getTitle();
            i++;
        }
    
        assertTrue(AtoZ.equals(actualAtoZ));

        String[] ZtoA = new String[]{ "sausage", "eggs", "bread", "bacon" };
        String[] actualZtoA = new String[4];
        controller.setSort("Z-A");
        controller.setRecipes(recipes);
        i = 0;
        for(Recipe recipe:  controller.handleSortBoxButton(new ActionEvent())){
            actualZtoA[i] = recipe.getTitle();
            i++;
        }
       
        assertTrue(ZtoA.equals(actualZtoA));

        String[] OldFirst = new String[]{ "bread", "sausage", "bacon", "eggs" };
        String[] actualOldFirst = new String[4];
        controller.setSort("Oldest first");
        controller.setRecipes(recipes);
        i = 0;
        for(Recipe recipe:  controller.handleSortBoxButton(new ActionEvent())){
            actualOldFirst[i] = recipe.getTitle();
            i++;
        }
        
        assertTrue(OldFirst.equals(actualAtoZ));

        String[] FirstOld = new String[]{ "bread", "sausage", "bacon", "eggs" };
        String[] actualFirstOld = new String[4];
        controller.setSort("First Oldest");
        controller.setRecipes(recipes);
        i = 0;
        for(Recipe recipe:  controller.handleSortBoxButton(new ActionEvent())){
            actualFirstOld[i] = recipe.getTitle();
            i++;
        }
        
        assertTrue(FirstOld.equals(actualFirstOld));
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

    // Tests if RememberMe csv file re written to not remember
    @Test
    void testSignOut() throws IOException{
        MockRecipeListPageController controller = new MockRecipeListPageController(model);
        controller.handleSignOutButton(new ActionEvent());

        FileReader file = new FileReader("RememberMe.csv");
        BufferedReader br =new BufferedReader(file);
        assertEquals("0", br.readLine());
        file.close();
        br.close();
    }

    // Transcribes correct mealType
    @Test
    void testRecordMealType() throws IOException{
        MockSpecifyMealTypePageController controller = new MockSpecifyMealTypePageController(model);
        controller.setTranscribedText("Dinner");
        controller.handleRecordHoldButton(new MouseEvent(null, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null));
        String text = controller.handleRecordReleasetButton(new MouseEvent(null, 0, 0, 0, 0, null, 0, false, false, false, false, false, false, false, false, false, false, null));
        assertEquals("Dinner", text);
    }

    @Test
    void testIngredients(){

    }



    /**
     * 
     * 
     */
    @Test
    void endToEnd() throws IOException {
        
    }



}
