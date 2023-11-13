package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.View;

import javafx.event.ActionEvent;

public class DetailedRecipePageController {
    private DetailedRecipePage view;
    private Model model;

    public DetailedRecipePageController(DetailedRecipePage view, Model model) {
        this.view = view;
        this.model = model;

    }

    // performRequest(METHOD, newRe)
    private List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<Recipe>();
        List<String> response = model.performRequest("GET", null, null);
        for (String s : response) {
            String[] recipeTokens = s.split(";");
            Recipe recipe = new Recipe(recipeTokens[0], recipeTokens[1].replace("\\n", "\n"));
            recipes.add(recipe);
        }
        return recipes;
    }

    private void handleAddButton(ActionEvent event) {
        // Go to SpecifyMealType & Specify Ingredient Page...
        // Get the responses from there

        // Recipe toSend = new Recipe();
        List<String> response = model.performRequest("POST", null, /* toSend */null);
    }
}