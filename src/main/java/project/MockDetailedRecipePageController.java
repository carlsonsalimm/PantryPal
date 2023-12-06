package project;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Pair;

public class MockDetailedRecipePageController {
    private Model model;
    Recipe recipe;

    String title,instructions, ingredients;

    public MockDetailedRecipePageController(Model model){
        this.model = model;
    }

    public void setRecipeTarget(Recipe recipe){
        this.recipe = recipe;
    }

    public void setUpdateInfo(String title, String instructions, String ingredients){
        this.title = title;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    public void handleDeleteButton(ActionEvent event) throws IOException {
        
        model.performRequest("DELETE", "deleteRecipe", null, null, null, null, null, recipe.getTitle(), null, null,null);
        
    }

    public void handleSaveButton(ActionEvent event) throws IOException {
        
        model.performRequest("POST", "updateRecipe", null, null, null, null, ingredients, title, instructions, recipe.getCreationTime(), null);

    }

    public boolean handleBackButton(ActionEvent event) throws IOException{
       return true;
    }

    public Pair<Recipe,String> handleRefreshButton(ActionEvent event) throws IOException, InterruptedException, URISyntaxException{
       MockDallE mock = new MockDallE();
       String url = mock.generateImageURL("test");
       Recipe recipe = new Recipe("Cereal", "Milk then Cereal", "Milk,Cereal", "Breakfast");
       Pair<Recipe,String> result = new Pair<Recipe,String>(recipe, url);
    return result;
        
    }

    public String handleShareButton(ActionEvent event) throws IOException{
     
       return "Google.com";
    }

    public boolean handleEditButton(ActionEvent event) throws IOException{
        // Handle Edit Button
        return true;
    }

    public Recipe createRecipe(String gptResponse) {
        String recipeTitle = gptResponse.substring(0, gptResponse.indexOf("\n"));
        String recipeInstructions = gptResponse.substring(gptResponse.indexOf("\n"));
        Recipe newrecipe = new Recipe(recipeTitle, recipeInstructions, recipe.getIngredients(), recipe.getMealType());
        return newrecipe;
    }
}
