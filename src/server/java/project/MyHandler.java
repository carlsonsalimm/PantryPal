package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MyHandler implements HttpHandler {
 private final Map<String, String> data;


 public MyHandler(Map<String, String> data) {
   this.data = data;
 }
    
    public void handle(HttpExchange httpExchange) throws IOException {
    
    String response = "Request Received";
    String method = httpExchange.getRequestMethod();
     
    try {
        if (method.equals("GET")) {
          response = handleGet(httpExchange);
        } else {
          throw new Exception("Not Valid Request Method");
        }
      } catch (Exception e) {
        System.out.println("An erroneous request");
        response = e.toString();
        e.printStackTrace();
      }

      //Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
        
        
         
 }

    public String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String name = uri.getRawQuery();
        name = name.substring(1, name.length());
        if (name != null) {
        String value = name.substring(name.indexOf("=") + 1);
        String year = data.get(value); // Retrieve data from hashmap
        if (year != null) {
        response = year;
        System.out.println("Queried for " + value + " and found " + year);
        } else {
        response = "No data found for " + value;
        }
    }
   

        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder
        .append("<html>")
        .append("<body>")
        .append("<h1>")
        .append("Hello ")
        .append(name)
        .append("</h1>")
        .append("</body>")
        .append("</html>");


        // encode HTML content
        response = htmlBuilder.toString();

         return response;
    }
}