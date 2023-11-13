package project;

import java.io.*;
import java.net.*;
import org.json.*;

public class Whisper implements WhisperInterface {

    private final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private final String TOKEN = "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";
    private final String MODEL = "whisper-1";

    public String transcribeAudio(String filePath) throws IOException, URISyntaxException, JSONException {

        // Create file object from file path
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        // Set up HTTP connection
        URL url = new URI(API_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);

        // Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();

        // Write model parameter to request body
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);

        // Write file parameter to request body
        writeFileToOutputStream(outputStream, file, boundary);

        // Write closing boundary to request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        // Flush and close output stream
        outputStream.flush();
        outputStream.close();

        // Get response code
        int responseCode = connection.getResponseCode();

        // Check response code and handle response accordingly
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return handleSuccessResponse(connection);
        } else {
            handleErrorResponse(connection);
            return null;
        }
    }

    private void writeParameterToOutputStream(OutputStream outputStream, String parameterName,
            String parameterValue, String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes());
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    private void writeFileToOutputStream(OutputStream outputStream, File file, String boundary)
            throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                ("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n").getBytes());
        outputStream.write("Content-Type: audio/mpeg\r\n\r\n".getBytes());

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private String handleSuccessResponse(HttpURLConnection connection) throws IOException, JSONException {
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

    private void handleErrorResponse(HttpURLConnection connection) throws IOException, JSONException {
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

}
