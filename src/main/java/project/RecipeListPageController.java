package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import org.json.Cookie;

import javafx.event.ActionEvent;

public class RecipeListPageController {
    // actions to account for: addButton, RecipeItem, detailedViewButton
    private RecipeListPage view;
    private Model model;

    public RecipeListPageController(RecipeListPage view ,Model model){
        this.view = view;
        this.model = model;

        this.view.setAddButtonAction(event -> {
            try {
                handleAddButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        this.view.setDetailedViewButtonAction(event -> {
            try {
                handleDetailedViewButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleAddButton(ActionEvent event) throws IOException{
        SpecifyMealTypePage temp = new SpecifyMealTypePage();
        Main.setPage(temp);
    }

    private void handleDetailedViewButton(ActionEvent event) throws IOException{
        Recipe recipe = recipe.getRecipe();
        DetailedRecipePage temp = new DetailedRecipePage(recipe);
        Main.setPage(temp);
    }

   
}
