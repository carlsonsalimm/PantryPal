package project;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DallEInterface {
    void generateImage(String prompt) throws IOException, InterruptedException, URISyntaxException;
}
