package project;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;

public class MockWhisper implements WhisperInterface {

    public String getText(String filePath) throws IOException, URISyntaxException, JSONException {
        return "Dinner.";
    }

}
