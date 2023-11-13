package project;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;

public class MockWhisper implements WhisperInterface {

    public String transcribeAudio(String filePath) throws IOException, URISyntaxException, JSONException {
        return "Dinner.";
    }

}
