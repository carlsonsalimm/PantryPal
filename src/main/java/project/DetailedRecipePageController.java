package project;

import java.io.IOException;

import javafx.event.ActionEvent;

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

    private void handleDeleteButton(ActionEvent event) throws IOException {
        Recipe recipe = view.getRecipe();
       
        model.performRequest("DELETE", "deleteRecipe", null, null, null, null, null, recipe.getTitle(), null, null);

        // Exit Window
        // Add login stuff
        RecipeListPage listPage = new RecipeListPage(null);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage,model));
    }

    public void handleSaveButton(ActionEvent event) throws IOException {
        Recipe recipe = view.getRecipe();
        model.performRequest("POST", "updateRecipe", null, null, null, null, view.getIngredients(), view.getTitle(), view.getInstructions(), recipe.getCreationTime());

        // Exit Window
        RecipeListPage listPage = new RecipeListPage(null);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage,model));
    }

    public void handleBackButton(ActionEvent event) throws IOException{
        // Exit Window
         RecipeListPage listPage = new RecipeListPage(null);
        Main.setPage(listPage);
        Main.setController(new RecipeListPageController(listPage,model));
    }

    public void handleRefreshButton(ActionEvent event) throws IOException{
        Recipe recipe = view.getRecipe();
        String response = model.performRequest("POST",null,null,null,null, recipe.getMealType(), recipe.getIngredients(),null,null,null);

        view.setTitle(response);
        view.setInstructions(response);
        view.setIngredients(response);
        
    }

    public void handleShareButton(ActionEvent event) throws IOException{
        // Handle share actions here (
    }

    public void handleEditButton(ActionEvent event) throws IOException{
        // Handle Edit Button
        view.instructions.setEditable(true);
    }

}
