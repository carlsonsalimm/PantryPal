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
        model.performRequest("POST", "updateRecipe", null, null, null, null, null, view.getTitle(), view.getInstructions(), null);

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
}
