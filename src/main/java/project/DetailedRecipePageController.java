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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        });


        this.view.setSaveButtonAction(event -> {
                try {
                    handleSaveButton(event);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        });

        this.view.setBackButtonAction(event -> {
                try {
                    handleBackButton(event);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        });
    }

    private void handleDeleteButton(ActionEvent event) throws IOException {
        Recipe recipe = view.getRecipe();
        
        CSVHandler.deleteRecipe(recipe);

        // Exit Window
        RecipeListPage temp = new RecipeListPage();
        Main.setPage(temp);
        Main.setController(new RecipeListPageController(temp,model));
    }

    public void handleSaveButton(ActionEvent event) throws IOException {
        Recipe oldRecipe = view.getRecipe();
        String recipe = view.getTitle();
        String instructions = view.getInstructions();
        
        if (oldRecipe.getInstructions().equals(instructions)) {
            CSVHandler.writeRecipes(oldRecipe);
        }
        else {
            CSVHandler.updateRecipe(oldRecipe, new Recipe(recipe, instructions));
        }

        // Exit Window
        RecipeListPage temp = new RecipeListPage();
        Main.setPage(temp);
        Main.setController(new RecipeListPageController(temp,model));
    }

    public void handleBackButton(ActionEvent event) throws IOException{
        
        // Exit Window
        RecipeListPage temp = new RecipeListPage();
        Main.setPage(temp);
        Main.setController(new RecipeListPageController(temp,model));
    }
}
