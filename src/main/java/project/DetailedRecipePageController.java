package project;

import java.io.IOException;

import javafx.event.ActionEvent;

public class DetailedRecipePageController {
    private DetailedRecipePage view;
    private Model model;

    public DetailedRecipePageController(DetailedRecipePage view ,Model model){
        this.view = view;
        this.model = model;

        this.view.setDeleteButtonAction(event -> {
                handleDeleteButton(event);
        });


        this.view.setSaveButtonAction(event -> {
                handleSaveButton(event);
        });

        this.view.setBackButtonAction(event -> {
                handleBackButton(event);
        });
    }

    private void handleDeleteButton(ActionEvent event) throws IOException {
        Recipe recipe = view.getRecipe();
        
        CSVHandler.deleteRecipe(recipe);

        RecipeListPage temp = new RecipeListPage();
        Main.setPage(temp);
    }
}
