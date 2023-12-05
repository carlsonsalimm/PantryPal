package project;

import java.io.*;
import java.net.*;

import org.json.JSONException;
import org.json.JSONObject;

public class Model {

    String username;
    String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String encode(String requestArgument) {
        // Encode request argument
        String encodedArgument = "";
        return encodedArgument;
    }

    public String decode(String requestResponse) {
        // Encode request response
        String decodedResponse = "";
        return decodedResponse;
    }

    public String performRequest(String method, String action, String iniUsername, String iniPassword,
            String audioFilePath, String mealType,
            String ingredients, String title, String instructions, String creationTime, String imageURL) {

        File file = new File("dwukadhkadwa");
        try {
            String urlString = "https://pantrypal-team31.onrender.com/";
            // String urlString = "http://localhost:8100/";

            if (method.equals("GET")) {

                if (username != null && password != null && title == null && creationTime == null) {
                    // Get recipe list (needs username, password)
                    urlString += "?action=getRecipeList&username=" + username + "&password=" + password;

                } else if (username != null && password != null && title != null && creationTime == null) {
                    // Generate image for recipe (needs username, password, title, returns url of
                    // image)
                    urlString += "?action=getImage&title=" + title;

                } else if (username != null && password != null && title == null && creationTime != null) {
                    // Get share URL for recipe (needs username, password, creationTime)
                    urlString += "?action=getShare&username=" + username + "&password=" + password + "&creationTime="
                            + creationTime;
                }

            } else if (method.equals("POST")) {

                if (audioFilePath != null) {
                    // Transcribe audioFilePath (needs audioFilePath, returns mealType/ingredients
                    // as string)
                    // TO-DO: Handle audioFilePath
                    file = new File(audioFilePath);
                    if (!file.exists()) {
                        throw new FileNotFoundException("File not found: " + audioFilePath);
                    }
                    urlString += "?action=transcribeaudioFile&audioFile=" + file.getName();
                } else if (action != null && action.equals("login") && iniUsername != null && iniPassword != null) {
                    // Login (needs username, password, action)
                    urlString += "?action=login&username=" + iniUsername + "&password=" + iniPassword;

                } else if (action != null && action.equals("signup") && iniUsername != null && iniPassword != null) {
                    // Sign up (needs username, password, action)
                    urlString += "?action=signup&username=" + iniUsername + "&password=" + iniPassword;

                } else if (username != null && password != null && mealType != null && ingredients != null
                        && title != null && instructions != null && creationTime == null) {
                    // Create a new recipe (requires username, password, mealType,
                    // ingredients, title, instructions)
                    urlString += "?action=createRecipe&username=" + username + "&password=" + password + "&mealType="
                            + mealType + "&ingredients=" + ingredients + "&title=" + title + "&instructions="
                            + instructions;

                } else if (username != null && password != null && title != null && instructions != null
                        && creationTime != null) {
                    // Updates an existing recipe (requires username, password, ingredients, title,
                    // instructions, creationTime)
                    urlString += "?action=updateRecipe&username=" + username + "&password=" + password + "&ingredients="
                            + ingredients + "&title=" + title + "&instructions=" + instructions + "&creationTime="
                            + creationTime;

                } else if (mealType != null && ingredients != null) {
                    // Generate recipe instructions w/GPT (needs meal type and ingredients, returns
                    // instructions as string)
                    urlString += "?action=generateRecipe&mealType=" + mealType + "&ingredients=" + ingredients;
                }else if(title!=null && imageURL!=null){
                    //generate image for the recipe
                    urlString += "?action=generateImage=" + title;
                }
                // } else if(title!=null &&  mealType != null && ingredients != null & imageURL!=null){
                //     //generate new recipe with image (CURRENTLY USING THIS FOR REFRESH TESTING)
                //     urlString += "?action=regenerateRecipe&title=" + title + "&mealType=" + mealType + "&ingredients="
                //             + ingredients + "&imageUrl=" + imageURL;
                // } 
            } else if (method.equals("DELETE")) {
                if (username != null && password != null && title != null) {
                    // Deletes an existing recipe (requires username, password, title)
                    urlString += "?action=deleteRecipe&username=" + username + "&password=" + password + "&title="
                            + title;

                } else if (username != null && password != null && title == null) {
                    // Deletes an existing user (requires username, password)
                    urlString += "?action=deleteUser&username=" + username + "&password=" + password;
                }

            }

            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            if (audioFilePath != null) {
                setConnectionHeaders(conn, file);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    private void setConnectionHeaders(HttpURLConnection conn, File audioFile) throws IOException {
        // Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        conn.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=" + boundary);
        conn.setRequestProperty("Content-Length", String.valueOf(audioFile.length()));

        // Set up output stream to write request body
        OutputStream outputStream = conn.getOutputStream();

        // Write file parameter to request body
        writeFileToOutputStream(outputStream, audioFile, boundary);

        // Write closing boundary to request body
        // outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        // Flush and close output stream
        outputStream.flush();
        outputStream.close();

    }

    private void writeFileToOutputStream(OutputStream outputStream, File file, String boundary)
            throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                ("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n").getBytes());
        outputStream.write("Content-Type: audio/wav\r\n\r\n".getBytes());

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

}

// private String handleSuccessResponse(HttpURLConnection connection) throws
// IOException, JSONException {
// StringBuilder response = new StringBuilder();
// try (BufferedReader in = new BufferedReader(new
// InputStreamReader(connection.getInputStream()))) {
// String inputLine;
// while ((inputLine = in.readLine()) != null) {
// response.append(inputLine);
// }
// }

// JSONObject responseJson = new JSONObject(response.toString());
// return responseJson.getString("text");
// }

// private void handleErrorResponse(HttpURLConnection connection) throws
// IOException, JSONException {
// StringBuilder errorResponse = new StringBuilder();
// try (BufferedReader errorReader = new BufferedReader(new
// InputStreamReader(connection.getErrorStream()))) {
// String errorLine;
// while ((errorLine = errorReader.readLine()) != null) {
// errorResponse.append(errorLine);
// }
// }
// System.err.println("Error during transcription: " +
// errorResponse.toString());
// // Here you might want to throw an exception instead
// }
// }
