package project;

import org.junit.jupiter.api.Test;

import javafx.event.ActionEvent;

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
    

    @Test
    void testValidLogin() throws IOException, InterruptedException, URISyntaxException {
        LoginPage login = new LoginPage();
        LoginPageController controller = new LoginPageController(login, new Model());
        String username = "Carl";
        String password = "1234";

        login.setUsername(username);
        login.setPassword(password);
        boolean result = controller.handleSignInButton(new ActionEvent());

        assertEquals(true, result);
        
    }
}
