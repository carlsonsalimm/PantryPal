package project;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;

public class Model {
    // For getting recipe list
    public List<String> performRequest(String method, Recipe oldRecipe, Recipe newRecipe) {
        // Implement your HTTP request logic here and return the response
        List<String> response = new ArrayList<String>();
        try {
            String urlString = "http://localhost:8100/";
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            // if (method.equals("POST") || method.equals("PUT")) {
            // OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            // out.write(language + "," + year);
            // out.flush();
            // out.close();
            // }

            if (method.equals("GET")) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) { // Sets a new task per line
                    response.add(line);
                }
                in.close();
            } else if (method.equals("POST")) {

            } else if (method.equals("PUT")) {

            } else if (method.equals("DELETE")) {

            }

            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            response.add("Error: " + ex.getMessage());
            return response;
        }
    }

    //
}