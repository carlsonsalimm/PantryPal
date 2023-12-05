package project;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class MockDetailedRecipePageController {
    private Model model;
    Recipe recipe;

    String title,instructions;

    public MockDetailedRecipePageController(Model model){
        this.model = model;
    }

    public void setRecipeTarget(Recipe recipe){
        this.recipe = recipe;
    }

    public void setUpdateInfo(String title, String instructions){
        this.title = title;
        this.instructions = instructions;
    }

    public void handleDeleteButton(ActionEvent event) throws IOException {
        
        model.performRequest("DELETE", "deleteRecipe", null, null, null, null, null, recipe.getTitle(), null, null,null);
        
    }

    public void handleSaveButton(ActionEvent event) throws IOException {
        
        model.performRequest("POST", "updateRecipe", null, null, null, null, null, title, instructions, recipe.getCreationTime(), null);

    }

    public boolean handleBackButton(ActionEvent event) throws IOException{
       return true;
    }

    public Recipe handleRefreshButton(ActionEvent event) throws IOException{
       
        return new Recipe("Cereal", "Milk then Cereal", "Milk,Cereal", "Breakfast");
        
    }

    public String handleShareButton(ActionEvent event) throws IOException{
     
       return "Google.com";
    }

    public void handleEditButton(ActionEvent event) throws IOException{
        // Handle Edit Button
        view.instructions.setEditable(true);
    }

    public Recipe createRecipe(String gptResponse) {
        String recipeTitle = gptResponse.substring(0, gptResponse.indexOf("\n"));
        String recipeInstructions = gptResponse.substring(gptResponse.indexOf("\n"));
        Recipe recipe = view.getRecipe();
        Recipe newrecipe = new Recipe(recipeTitle, recipeInstructions, recipe.getIngredients(), recipe.getMealType());
        return newrecipe;
    }
}
