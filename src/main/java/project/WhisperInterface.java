package project;

import java.io.IOException;
import java.net.URISyntaxException;
import org.json.JSONException;

public interface WhisperInterface {

    String transcribeAudio(String filePath) throws IOException, URISyntaxException, JSONException;

}