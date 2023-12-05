package project;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
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
      return response;
    }

    if (action.equals("getRecipeList")) {
      // get recipe list for acct
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      List<Document> recipes = MongoDBProject.getRecipeList(username, password);
      // ArrayList<String> jsons = new ArrayList<String>();
      response = "{";
      for (Document d : recipes) {
        response += d.toJson() + ",";
      }
      response += "}";
    } else if (action.equals("getImage")) {
      // Do DALL-E call
      String title = queryParams.get("password");
      String prompt = queryParams.get("prompt");
    } else if (action.equals("getRecipeDetails")) {
      // Fetch a specific recipe's details
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      String title = queryParams.get("title");

      Document recipe = MongoDBProject.getRecipeByTitle(username, password, title);
      if (recipe != null) {
        response = recipe.toJson();
      } else {
        response = "Recipe not found";
      }
    }

    return response;
  }

  // ChatGPT, Whisper, DALL-E, addRecipe
  private String handlePost(HttpExchange httpExchange) throws IOException {
    String response = "Invalid POST Request";
    queryParams = parseQueryParams(httpExchange.getRequestURI().getQuery());
    String action = queryParams.get("action");
    if (action == null) {
      return response;
    }

    if (action.equals("transcribeaudioFile")) {
      // transcribe audio to text
      String CRLF = "\r\n";
      Headers httpHeaders = httpExchange.getRequestHeaders();
      int fileSize = Integer.valueOf(httpHeaders.getFirst("Content-Length"));
      System.out.println("Expected audio file size: " + fileSize);
      // String FILE_TO_RECEIVED = queryParams.get("audioFile");
      System.out.println(queryParams.get("audioFile"));
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
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      response = String.valueOf(MongoDBProject.login(username, password));

      // String response_string = Boolean.valueOf(response) ? "success" : "failure";
      // System.out.println("Tried to login user: " + username + ": " + response_string);

    } else if (action.equals("signup")) {
      // try signing up with acct details
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      response = String.valueOf(MongoDBProject.createUser(username, password));

      // String response_string = Boolean.valueOf(response) ? "success" : "failure";
      // System.out.println("Tried to signup user: " + username + ": " + response_string);

    } else if (action.equals("createRecipe")) {
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      String mealType = queryParams.get("mealType");
      String ingredients = queryParams.get("ingredients");
      String title = queryParams.get("title");
      String instructions = queryParams.get("instructions");
      String imageURL = queryParams.get("imageURL");
      // TODO change to addRecipe once the method is added in MongoDBProject
      MongoDBProject.updateRecipe(username, password, title, mealType, ingredients, instructions, 0);
      response = "Added recipe: " + title;

    } else if (action.equals("updateRecipe")) {
      // update/add recipe
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      String mealType = queryParams.get("mealType");
      String ingredients = queryParams.get("ingredients");
      String title = queryParams.get("title");
      String instructions = queryParams.get("instructions");
      Long creationTime = Long.parseLong(queryParams.get("creationTime"));
      //String imageURL = queryParams.get("imageURL");
      MongoDBProject.updateRecipe(username, password, title, instructions, mealType, ingredients, creationTime);
      response = "Updated recipe: " + title;
      // replace with what we are expecting as a response

    } else if (action.equals("generateRecipe")) {
      String mealType = queryParams.get("mealType");
      String ingredients = queryParams.get("ingredients");
      try {
        response = chatGPT.getGPTResponse(ingredients, mealType);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }

    else if (action.equals("generateImage")) {
      String prompt = queryParams.get("prompt");
      try {
          String imageURL = dallE.generateImageURL(prompt);
          response = "{ \"imageURL\": \"" + imageURL + "\" }"; // Format the response as JSON
      } catch (IOException | InterruptedException | URISyntaxException e) {
          e.printStackTrace();
          response = "Error generating image: " + e.getMessage();
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
      return response;
    }

    if (action.equals("deleteRecipe")) {
      String username = queryParams.get("username");
      String password = queryParams.get("password");
      String title = queryParams.get("title");
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