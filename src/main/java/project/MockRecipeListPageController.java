package project;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MockRecipeListPageController implements Controller {
     // actions to account for: addButton, RecipeItem, detailedViewButton
    private Model model;
    public List<Recipe> recipes;
    public String sort;
    public String type;

    public MockRecipeListPageController(Model model) {
        this.model = model;

    }

    public void setMealType(String type){
        this.type = type;
    }
    public void setSort(String sort){
        this.sort = sort;
    }
    public void setRecipes(ArrayList<Recipe> recipes){
        this.recipes = recipes;
    }
    public boolean handleAddButton(ActionEvent event) throws IOException {
        return true;
    }

    public boolean handleDetailedViewButton(ActionEvent event) throws IOException {
        return true;
    }

    public List<Recipe> handleSortBoxButton(ActionEvent event) throws IOException {
        List<Recipe> tempRecipes = recipes;
        String selectedItem = sort;
        // clear list
        // compare, then determine sort method
        if (selectedItem == "A-Z") { // sort A-Z logic
            Collections.sort(tempRecipes, Comparator.comparing(Recipe::getTitle));
        } else if (selectedItem == "Z-A") { // sort Z-A logic
            Collections.sort(tempRecipes, Comparator.comparing(Recipe::getTitle).reversed());
        } else if (selectedItem == "Oldest first") { // sort oldest first logic use creation time 
            Collections.sort(tempRecipes, Comparator.comparingLong(Recipe -> Long.parseLong(Recipe.getCreationTime())));
            Collections.reverse(tempRecipes);
        } else { // default; newest first
            Collections.sort(tempRecipes, Comparator.comparingLong(Recipe -> Long.parseLong(Recipe.getCreationTime())));
        }
        // re-populate list)
        return tempRecipes;
    }

    public List<Recipe> handleFilterBoxButton(ActionEvent event) throws IOException {
        List<Recipe> tempRecipes = recipes;
        String selectedItem = type;
        List<Recipe> filter = new ArrayList<>();
        // clear list
        if (selectedItem.equals("Breakfast")) {
            for (Recipe x : tempRecipes) {
                if (x.getMealType() == "breakfast") {
                    filter.add(x);
                }
            }
        } else if (selectedItem.equals("Lunch")) {
            for (Recipe x : tempRecipes) {
                if (x.getMealType() == "lunch") {
                    filter.add(x);
                }
            }
        } else if (selectedItem.equals("Dinner")) {
            for (Recipe x : tempRecipes) {
                if (x.getMealType() == "dinner") {
                    filter.add(x);
                }
            }
        } 
        return filter;
    }

    public boolean handleSignOutButton(ActionEvent event) throws IOException {

        // Reset Rememvber Me Logic
        FileWriter file = new FileWriter("RememberMe.csv", false);
        file.write("0");
        file.close();

        return true;
    }
}
