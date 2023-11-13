package project;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockGPT implements GPTInterface {

    public String getGPTResponse(String prompt, String mealType)
            throws IOException, InterruptedException, URISyntaxException {
        return "Test title from MockGPT\nTest instructions from MockGPT";
    }

}
