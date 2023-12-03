package project;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DallEInterface {
    String generateImagePath(String prompt) throws IOException, InterruptedException, URISyntaxException;
}
