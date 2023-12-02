package project;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DallEInterface {
    String generateImageURL(String prompt) throws IOException, InterruptedException, URISyntaxException;
}
