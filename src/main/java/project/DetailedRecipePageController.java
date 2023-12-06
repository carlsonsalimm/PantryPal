package project;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class DetailedRecipePageController implements Controller {
    private DetailedRecipePage view;
    private Model model;

    public DetailedRecipePageController(DetailedRecipePage view, Model model) {
        this.view = view;
        this.model = model;

        this.view.setDeleteButtonAction(event -> {
            try {
                handleDeleteButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.view.setSaveButtonAction(event -> {
            try {
                handleSaveButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.view.setBackButtonAction(event -> {
            try {
                handleBackButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.view.setRefreshButtonAction(event -> {
            try {
                handleRefreshButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.view.setShareButtonAction(event -> {
            try {
                handleShareButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.view.setEditButtonAction(event -> {
            try {
                handleEditButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleDeleteButton(ActionEvent event) throws IOException {
        Recipe recipe = view.getRecipe();

        model.performRequest("DELETE", "deleteRecipe", null, null, null, null, null, recipe.getTitle(), null, null,
                null);

        // Exit Window
        // Add login stuff
        String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null,
                null);
        List<Recipe> recipes = Main.extractRecipeInfo(JSON);
        RecipeListPage listPage = new RecipeListPage(recipes);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
    }

    public void handleSaveButton(ActionEvent event) throws IOException {
        Recipe recipe = view.getRecipe();
        model.performRequest("POST", "updateRecipe", null, null, null, null, view.getIngredients(), view.getTitle(),
                view.getInstructions(), recipe.getCreationTime(), null);

        // Exit Window
        String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null,
                null);
        List<Recipe> recipes = Main.extractRecipeInfo(JSON);
        RecipeListPage listPage = new RecipeListPage(recipes);

        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        // Exit Window\

        String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null,
                null);
        List<Recipe> recipes = Main.extractRecipeInfo(JSON);
        RecipeListPage listPage = new RecipeListPage(recipes);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
    }

    // public void handleRefreshButton(ActionEvent event) throws IOException {
    // Recipe recipe = view.getRecipe();

    // // Generate a new recipe
    // String response = model.performRequest("POST", "generateRecipe", null, null,
    // null, recipe.getMealType(), recipe.getIngredients(), null, null, null, null);

    // // Debugging: Print the server's response
    // System.out.println("Response: " + response);

    // if (response == null || response.isEmpty()) {
    // System.out.println("Error: Received empty or null response from server.");
    // // Handle the error appropriately, e.g., show an error message to the user
    // return;
    // }

    // try {
    // // Parse the response to extract the new recipe details
    // JSONObject jsonResponse = new JSONObject(response);
    // String newTitle = jsonResponse.optString("title", "Default Title");
    // String newInstructions = jsonResponse.optString("instructions", "Default
    // Instructions");
    // String newIngredients = jsonResponse.optString("ingredients", "Default
    // Ingredients");

    // // Request a new image URL from the server
    // String newImageURL = model.performRequest("GET", "generateImage", null, null,
    // null, null, null, newTitle, null, null, null);

    // // Update the view with the new recipe details
    // view.setTitle(newTitle);
    // view.setInstructions(newInstructions);
    // view.setIngredients(newIngredients);
    // view.setImage(newImageURL);
    // } catch(JSONException e) {
    // System.out.println("Error parsing JSON response: " + e.getMessage());
    // // Handle the error appropriately, e.g., show an error message to the user
    // }
    // }

    public void handleRefreshButton(ActionEvent event) throws IOException {
        Recipe recipe = view.getRecipe();

        // Generate a new recipe
        String response = model.performRequest("POST", "generateRecipe", null, null, null, recipe.getMealType(),
                recipe.getIngredients(), null, null, null, null);

        // Debugging: Print the server's response
        System.out.println("Response: " + response);

        if (response == null || response.isEmpty()) {
            System.out.println("Error: Received empty or null response from server.");
            return;
        }

        // Assuming the response is a plain string with the recipe details
        // You would parse this string based on its format
        // For example, if it's a newline-separated string:
        String[] parts = response.split("\n");
        String newTitle = parts.length > 0 ? parts[0] : "Default Title";
        String newInstructions = parts.length > 1 ? parts[1] : "Default Instructions";
        // String newIngredients = parts.length > 2 ? parts[2] : "Default Ingredients";

        // Request a new image URL from the server
        String newImageURLResponse = model.performRequest("GET", "generateImage", null, null, null, null, null,
                newTitle, null, null, null);

        // Extracting the URL from the response
        String newImageURL = newImageURLResponse.startsWith("{") ? newImageURLResponse.split("\"")[3]
                : newImageURLResponse;

        // Test print the new image URL
        System.out.println("New image URL: " + newImageURL);

        // Update the view with the new recipe details
        view.setTitle(newTitle);
        view.setInstructions(newInstructions);
        // view.setIngredients(newIngredients);
        view.setImage(newImageURL);
    }

    public void handleShareButton(ActionEvent event) throws IOException {
        Recipe recipe = view.getRecipe();
        String url = model.performRequest("GET", "getShare", null, null, null, null, null, recipe.getTitle(), null,
                recipe.getCreationTime(), null);

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(url);
        clipboard.setContent(content);
    }

    public void handleEditButton(ActionEvent event) throws IOException {
        // Handle Edit Button
        view.instructions.setEditable(true);
    }

    // public Recipe createRecipe(String gptResponse) {
    // String recipeTitle = gptResponse.substring(0, gptResponse.indexOf("\n"));
    // String recipeInstructions = gptResponse.substring(gptResponse.indexOf("\n"));
    // Recipe recipe = view.getRecipe();
    // Recipe newrecipe = new Recipe(recipeTitle, recipeInstructions,
    // recipe.getIngredients(), recipe.getMealType());
    // return newrecipe;
    // }

}
