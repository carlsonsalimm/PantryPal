package project;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockDallE implements DallEInterface {

    public void generateImage(String prompt) throws IOException, InterruptedException, URISyntaxException {
        System.out.println("MockDallE generating image for prompt: " + prompt);
        // Mock response - for example, print a placeholder URL
        System.out.println("Mock Image URL: http://example.com/mockimage.jpg");
    }
}
