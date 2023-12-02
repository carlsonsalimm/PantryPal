package project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

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
            String ingredients, String title, String instructions, String creationTime) {

        try {
            String urlString = "https://pantrypal-team31.onrender.com/";

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
                    urlString += "?action=transcribeaudioFilePath&audioFilePath=" + audioFilePath;

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

                }

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

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

}
