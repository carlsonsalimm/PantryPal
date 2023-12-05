package project;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.event.ActionEvent;

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

    private void handleDeleteButton(ActionEvent event) throws IOException {
        Recipe recipe = view.getRecipe();

        model.performRequest("DELETE", "deleteRecipe", null, null, null, null, null, recipe.getTitle(), null, null, null);

        // Exit Window
        // Add login stuff
        RecipeListPage listPage = new RecipeListPage(null);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
    }

    public void handleSaveButton(ActionEvent event) throws IOException {
        model.performRequest("POST", "updateRecipe", null, null, null, null, null, view.getTitle(),
                view.getInstructions(), null, null);

        // Exit Window
        RecipeListPage listPage = new RecipeListPage(null);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        // Exit Window
        RecipeListPage listPage = new RecipeListPage(null);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
    }

    public void handleRefreshButton(ActionEvent event) throws IOException {
        try {
            // Retrieve the current meal type from the view
            String currentMealType = view.getRecipe().getMealType();

            // Generate a new recipe with the same meal type
            Recipe newRecipe = generateNewRecipe(currentMealType);

            // Update the view with the new recipe details
            view.updateRecipeDetails(newRecipe);

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }

    public void handleShareButton(ActionEvent event) throws IOException {
        // Handle share actions here (
    }

    public void handleEditButton(ActionEvent event) throws IOException {
        // Handle Edit Button
        view.instructions.setEditable(true);
    }

    private Recipe generateNewRecipe(String mealType) {
        String newTitle = "New Recipe Title"; // Replace with actual logic
        String newInstructions = "New Instructions"; // Replace with actual logic
        String newIngredients = "New Ingredients"; // Replace with actual logic
        String newCreationTime = getCurrentTime();
    
        // Request a new image URL from the server
        String newImageURL = requestNewImageURLFromServer(newTitle);
    
        return new Recipe(newTitle, newInstructions, newIngredients, mealType, newCreationTime, newImageURL);
    }

    private String requestNewImageURLFromServer(String title) {
        try {
            // The performRequest method is called with the appropriate parameters
            String response = model.performRequest("GET", "getImage", null, null, null, null, null, title, null, null, null);
            // You might need to parse the response to extract the image URL
            return extractImageURLFromResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ""; // Or handle the exception more gracefully
        }
    }
    
    private String extractImageURLFromResponse(String response) {
        // Implement the logic to parse the response and extract the image URL
        // This will depend on the format of the response from your server
        try {
            JSONObject jsonResponse = new JSONObject(response);
            // Assuming the response structure is similar to what DallE returns.
            String generatedImageURL = jsonResponse.getJSONArray("data").getJSONObject(0).getString("url");
            return generatedImageURL;
        } catch (JSONException e) {
            e.printStackTrace();
            return null; // Handle JSON parsing error
        }
    }

    private String getCurrentTime() {
        // Returns the current time in a specified format
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
