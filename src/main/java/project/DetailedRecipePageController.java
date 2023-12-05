package project;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class DetailedRecipePageController implements Controller{
    private DetailedRecipePage view;
    private Model model;

    public DetailedRecipePageController(DetailedRecipePage view ,Model model){
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
       
        model.performRequest("DELETE", "deleteRecipe", null, null, null, null, null, recipe.getTitle(), null, null,null);

        // Exit Window
        // Add login stuff
        String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null,null);
        List<Recipe> recipes = Main.extractRecipeInfo(JSON);
        RecipeListPage listPage = new RecipeListPage(recipes);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
    }

    public void handleSaveButton(ActionEvent event) throws IOException {
        Recipe recipe = view.getRecipe();
        model.performRequest("POST", "updateRecipe", null, null, null, null, null, view.getTitle(), view.getInstructions(), recipe.getCreationTime(), null);

        // Exit Window
        String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null,null);
        List<Recipe> recipes = Main.extractRecipeInfo(JSON);
        RecipeListPage listPage = new RecipeListPage(recipes);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
    }

    public void handleBackButton(ActionEvent event) throws IOException{
        // Exit Window\


        String JSON = model.performRequest("GET", "getRecipeList", null, null, null, null, null, null, null, null, null);
        List<Recipe> recipes = Main.extractRecipeInfo(JSON);
        RecipeListPage listPage = new RecipeListPage(recipes);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage, model));
    }

    public void handleRefreshButton(ActionEvent event) throws IOException{
        Recipe recipe = view.getRecipe();
        String response = model.performRequest("POST","generateRecipe",null,null,null, recipe.getMealType(), recipe.getIngredients(),null,null,null, null);
        Recipe newRecipe = createRecipe(response);
        view.setTitle(newRecipe.getTitle());
        view.setInstructions(newRecipe.getInstructions());
        view.setIngredients(newRecipe.getIngredients());
        
    }

    public void handleShareButton(ActionEvent event) throws IOException{
        Recipe recipe = view.getRecipe();
        String url = model.performRequest("GET", "getShare", null, null, null, null, null, recipe.getTitle(), null, recipe.getCreationTime(), null);

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(url);
        clipboard.setContent(content);
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
