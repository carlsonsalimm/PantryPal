package project;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockDallE implements DallEInterface {

    //method to return the generated image url
    public String generateImageURL(String prompt) throws IOException, InterruptedException, URISyntaxException {
        System.out.println("MockDallE generating image for prompt: " + prompt);
        // Mock response - for example, print a placeholder URL
        return "http://example.com/mockimage.jpg";
    }
}
