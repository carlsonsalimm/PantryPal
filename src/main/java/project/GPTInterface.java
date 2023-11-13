package project;

import java.io.IOException;
import java.net.URISyntaxException;

public interface GPTInterface {

    String getGPTResponse(String prompt, String mealType) throws IOException, InterruptedException, URISyntaxException;

}