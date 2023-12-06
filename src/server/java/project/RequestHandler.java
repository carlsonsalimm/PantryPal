package project;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.bson.Document;
import org.json.JSONException;

public class RequestHandler implements HttpHandler {

  private static final String TEMP_AUDIO_FILE_PATH = "tempAudio.wav";
  private static Whisper whisper;
  public static ChatGPT chatGPT;
  public static DallE dallE;
  // private DALLE dallE;

  Map<String, String> queryParams;

  RequestHandler() {
    whisper = new Whisper();
    chatGPT = new ChatGPT();
    dallE = new DallE();
  }

  public void handle(HttpExchange httpExchange) throws IOException {
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();

    try {
      if (method.equals("GET")) {
        response = handleGet(httpExchange);
      } else if (method.equals("POST")) {
        response = handlePost(httpExchange);
      } else if (method.equals("DELETE")) {
        response = handleDelete(httpExchange);
      } else {
        throw new Exception("Not Valid Request Method");
      }
    } catch (Exception e) {
      System.out.println("An erroneous request");
      response = e.toString();
      e.printStackTrace();
    }

    // Sending back response to the client
    httpExchange.sendResponseHeaders(200, response.length());
    OutputStream outStream = httpExchange.getResponseBody();
    outStream.write(response.getBytes());
    outStream.close();
  }

  // Login & getRecipeList & Refresh
  private String handleGet(HttpExchange httpExchange) throws IOException {
    String response = "Invalid GET Request";
    queryParams = parseQueryParams(httpExchange.getRequestURI().getQuery());
    String action = queryParams.get("action");
    if (action == null) {
      System.out.println("Received an " + response);
      return response;
    }

    if (action.equals("getRecipeList")) {
      // get recipe list for acct
      String username = this.decodeURL(queryParams.get("username"));
      String password = this.decodeURL(queryParams.get("password"));
      List<Document> recipes = MongoDBProject.getRecipeList(username, password);
      // ArrayList<String> jsons = new ArrayList<String>();
      response = "{";
      for (int i = 0; i < recipes.size(); i++) {
        response += "\"" + i + "\" :" + recipes.get(i).toJson();
        if (i < recipes.size() - 1) {
          response += ",";
        }
      }
      response += "}";

    } else if (action.equals("generateImage")) {
      String prompt = queryParams.get("title");
      try {
        String imageURL = dallE.generateImageURL(prompt);
        response = "{ \"imageURL\": \"" + imageURL + "\" }"; // Format the response as JSON
      } catch (IOException | InterruptedException | URISyntaxException e) {
        e.printStackTrace();
        response = "Error generating image: " + e.getMessage();
      }

    } else if (action.equals("getRecipeDetails")) {
      // Fetch a specific recipe's details
      String username = this.decodeURL(queryParams.get("username"));
      String password = this.decodeURL(queryParams.get("password"));
      String title = this.decodeURL(queryParams.get("title"));

      Document recipe = MongoDBProject.getRecipeByTitle(username, password, title);
      if (recipe != null) {
        response = recipe.toJson();
      } else {
        response = "Recipe not found";
      }

    } else if (action.equals("getShare")) {
      // Generate a url to share
      response = "Invalid GET request (share)";

      String title = this.decodeURL(queryParams.get("title"));
      String mealType = this.decodeURL(queryParams.get("mealType"));
      String ingredients = this.decodeURL(queryParams.get("ingredients"));
      String instructions = this.decodeURL(queryParams.get("instructions"));
      String imageURL = this.decodeURL(queryParams.get("imageURL"));

      StringBuilder htmlBuilder = new StringBuilder();
      htmlBuilder
          .append("<html>")
          .append("<body>")
          .append("<h1>")
          .append(title)
          .append("</h1>")
          .append("<h2>")
          .append(mealType)
          .append("</h2>")
          .append("<img src=\"")
          .append(imageURL)
          .append("\">")
          .append("<p>")
          .append(ingredients)
          .append("</p>")
          .append("<p>")
          .append(instructions)
          .append("</p>")
          .append("</body>")
          .append("</html>");

      // encode HTML content
      response = htmlBuilder.toString();
    }

    return response;
  }

  // ChatGPT, Whisper, DALL-E, addRecipe
  private String handlePost(HttpExchange httpExchange) throws IOException {
    String response = "Invalid POST Request";
    queryParams = parseQueryParams(httpExchange.getRequestURI().getQuery());
    String action = queryParams.get("action");
    if (action == null) {
      System.out.println("Received an " + response);
      return response;
    }

    if (action.equals("transcribeaudioFile")) {
      // transcribe audio to text
      String CRLF = "\r\n";
      Headers httpHeaders = httpExchange.getRequestHeaders();
      int fileSize = Integer.valueOf(httpHeaders.getFirst("Content-Length"));
      System.out.println("Expected audio file size: " + fileSize);
      // String FILE_TO_RECEIVED = queryParams.get("audioFile");
      System.out.println(this.decodeURL(queryParams.get("audioFile")));
      String FILE_TO_RECEIVED = String.valueOf(System.currentTimeMillis()) + ".wav";
      File file = new File(FILE_TO_RECEIVED);
      if (!file.exists()) {
        file.createNewFile();
      }
      InputStream input = httpExchange.getRequestBody();
      String nextLine = "";
      do {
        nextLine = readLine(input, CRLF);
      } while (!nextLine.equals(""));

      byte[] wavFileByteArray = new byte[fileSize];
      int readOffset = 0;
      while (readOffset < fileSize) {
        int bytesRead = input.read(wavFileByteArray, readOffset, fileSize);
        readOffset += bytesRead;
      }

      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(FILE_TO_RECEIVED));

      bos.write(wavFileByteArray, 0, fileSize);
      bos.flush();
      bos.close();
      System.out.println(file.getName() + " created with size: " + file.length());

      try {
        response = whisper.transcribeAudio(FILE_TO_RECEIVED);
      } catch (JSONException e) {
        e.printStackTrace();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }

      // delete the file after we've parsed it to save space in the long term
      if (file.delete()) {
        System.out.println("Deleted the file: " + file.getName());
      } else {
        System.out.println("Failed to delete file: " + file.getName());
      }

    } else if (action.equals("login")) {
      // try logging in with acct details
      String username = this.decodeURL(queryParams.get("username"));
      String password = this.decodeURL(queryParams.get("password"));
      response = String.valueOf(MongoDBProject.login(username, password));

      // String response_string = Boolean.valueOf(response) ? "success" : "failure";
      // System.out.println("Tried to login user: " + username + ": " +
      // response_string);

    } else if (action.equals("signup")) {
      // try signing up with acct details
      String username = this.decodeURL(queryParams.get("username"));
      String password = this.decodeURL(queryParams.get("password"));
      response = String.valueOf(MongoDBProject.createUser(username, password));

      // String response_string = Boolean.valueOf(response) ? "success" : "failure";
      // System.out.println("Tried to signup user: " + username + ": " +
      // response_string);

    } else if (action.equals("createRecipe")) {
      String username = this.decodeURL(queryParams.get("username"));
      String password = this.decodeURL(queryParams.get("password"));
      String mealType = this.decodeURL(queryParams.get("mealType"));
      String ingredients = this.decodeURL(queryParams.get("ingredients"));
      String title = this.decodeURL(queryParams.get("title"));
      String instructions = this.decodeURL(queryParams.get("instructions"));
      MongoDBProject.updateRecipe(username, password, title, mealType, ingredients, instructions, 0);
      response = "Added recipe: " + title;

    } else if (action.equals("updateRecipe")) {
      // update/add recipe
      String username = this.decodeURL(queryParams.get("username"));
      String password = this.decodeURL(queryParams.get("password"));
      String ingredients = this.decodeURL(queryParams.get("ingredients"));
      String title = this.decodeURL(queryParams.get("title"));
      String instructions = this.decodeURL(queryParams.get("instructions"));
      Long creationTime = Long.parseLong(this.decodeURL(queryParams.get("creationTime")));
      MongoDBProject.updateRecipe(username, password, title, null, ingredients, instructions, creationTime);
      response = "Updated recipe: " + title;
      // replace with what we are expecting as a response

    } else if (action.equals("generateRecipe")) {
      String mealType = this.decodeURL(queryParams.get("mealType"));
      String ingredients = this.decodeURL(queryParams.get("ingredients"));
      try {
        response = chatGPT.getGPTResponse(ingredients, mealType);
        System.out.println(response);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }


    }

    // response include generated recipe and image url (in the last part)
    return response;
  }

  // deleteRecipe
  private String handleDelete(HttpExchange httpExchange) throws IOException {
    String response = "Invalid DELETE request";
    queryParams = parseQueryParams(httpExchange.getRequestURI().getQuery());
    String action = queryParams.get("action");
    if (action == null) {
      System.out.println("Received an " + response);
      return response;
    }

    if (action.equals("deleteRecipe")) {
      String username = this.decodeURL(queryParams.get("username"));
      String password = this.decodeURL(queryParams.get("password"));
      String title = this.decodeURL(queryParams.get("title"));
      MongoDBProject.deleteRecipe(username, password, title);
      response = "Deleted recipe: " + title;
      // replace with what we are expecting as a response
    }
    return response;
  }

  private Map<String, String> parseQueryParams(String query) {
    Map<String, String> params = new HashMap<>();
    if (query != null) {
      String[] pairs = query.split("&");
      for (String pair : pairs) {
        String[] keyValue = pair.split("=");
        if (keyValue.length == 2) {
          params.put(keyValue[0], keyValue[1]);
        }
      }
    }
    return params;
  }

  public String encodeURL(String url) {
    String encodedURL = null;
    try {
      encodedURL = URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      System.out.println("Error encoding URL: " + e.getMessage());
    }
    return encodedURL;
  }

  public String decodeURL(String encodedURL) {
    String decodedURL = null;
    try {
      decodedURL = URLDecoder.decode(encodedURL, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      System.out.println("Error decoding URL: " + e.getMessage());
    }
    return decodedURL;
  }

  private String readLine(InputStream is, String lineSeparator)
      throws IOException {

    int off = 0, i = 0;
    byte[] separator = lineSeparator.getBytes("UTF-8");
    byte[] lineBytes = new byte[1024];

    while (is.available() > 0) {
      int nextByte = is.read();
      if (nextByte < -1) {
        throw new IOException(
            "Reached end of stream while reading the current line!");
      }

      lineBytes[i] = (byte) nextByte;
      if (lineBytes[i++] == separator[off++]) {
        if (off == separator.length) {
          return new String(
              lineBytes, 0, i - separator.length, "UTF-8");
        }
      } else {
        off = 0;
      }

      if (i == lineBytes.length) {
        throw new IOException("Maximum line length exceeded: " + i);
      }
    }

    throw new IOException(
        "Reached end of stream while reading the current line!");
  }
}