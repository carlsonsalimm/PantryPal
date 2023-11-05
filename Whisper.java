import java.io.*;
import java.net.*;
import org.json.*;

public class Whisper {

    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";
    //private static final String FILE_PATH = "/Users/arthurandersen/Documents/CSE110/Lab4/lib/Ground_beef_pasta_tomatoes.mp3";

   
    public static String transcribeAudio(String filePath) throws IOException, URISyntaxException, JSONException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        String boundary = "Boundary-" + System.currentTimeMillis();

        // Set up the connection
        URL url = new URL(API_ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        // Create the request body
        try (OutputStream outputStream = connection.getOutputStream()) {
            writeParameterToOutputStream(outputStream, "model", MODEL, boundary);
            writeFileToOutputStream(outputStream, file, boundary);
            outputStream.write(("--" + boundary + "--\r\n").getBytes());
            outputStream.flush();
        }

        // Get the response from the server
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return handleSuccessResponse(connection);
        } else {
            handleErrorResponse(connection);
            return null; // or throw an exception
        }
    }

    private static void writeParameterToOutputStream(OutputStream outputStream, String parameterName, String parameterValue, String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes());
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    private static void writeFileToOutputStream(OutputStream outputStream, File file, String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n").getBytes());
        outputStream.write("Content-Type: audio/mpeg\r\n\r\n".getBytes());

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private static String handleSuccessResponse(HttpURLConnection connection) throws IOException, JSONException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        JSONObject responseJson = new JSONObject(response.toString());
        return responseJson.getString("text");
    }

    private static void handleErrorResponse(HttpURLConnection connection) throws IOException, JSONException {
        StringBuilder errorResponse = new StringBuilder();
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
        }
        System.err.println("Error during transcription: " + errorResponse.toString());
        // Here you might want to throw an exception instead
    }

    // public static void main(String[] args) {
    //     try {
    //         String transcription = transcribeAudio(FILE_PATH);
    //         if (transcription != null) {
    //             System.out.println("Transcription: " + transcription);
    //         }
    //     } catch (IOException | URISyntaxException | JSONException e) {
    //         e.printStackTrace();
    //     }
    // }

    
}






