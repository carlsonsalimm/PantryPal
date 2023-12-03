package project;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockDallE implements DallEInterface {

    // Method to return the generated image URL (mock implementation)
    @Override
    public String generateImagePath(String prompt) throws IOException, InterruptedException, URISyntaxException {
        System.out.println("MockDallE generating image for prompt: " + prompt);

        // Mock response - for example, return a placeholder URL
        return "http://example.com/mockimage.jpg";
    }
}
