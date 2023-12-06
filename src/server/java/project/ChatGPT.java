package project;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPT implements GPTInterface {

    private final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private final String API_KEY = "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";
    private final String MODEL = "text-davinci-003";

    public String getGPTResponse(String prompt, String mealType)
            throws IOException, InterruptedException, URISyntaxException {

        // Set Request parameters
        prompt = "Generate a " + mealType + " recipe with the following ingredients: " + prompt
                + "\nFormat your response in the following format:\n\nTitle: GENERATED TITLE Instructions: GENERATED INSTRUCTIONS.\n\nGenerated title and generated instructions should be separated by a space. Generated instructions should not be a numbered list, and should be contained in one paragraph with no newlines. Follow this format even if there are no ingredients.";
        int maxTokens = 1000;

        // Create a request body which you will pass into the request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);

        // Create the HTTP Client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request object
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        // Send the request and receive the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Process the response
        String responseBody = response.body();

        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text");

        return generatedText.trim();
    }
}
