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

}
