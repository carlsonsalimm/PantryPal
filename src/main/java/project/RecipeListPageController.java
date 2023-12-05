package project;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import org.json.Cookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class RecipeListPageController implements Controller {
    // actions to account for: addButton, RecipeItem, detailedViewButton
    private RecipeListPage view;
    private Model model;

    public RecipeListPageController(RecipeListPage view, Model model) {
        this.view = view;
        this.model = model;

        this.view.setAddButtonAction(event -> {
            try {
                handleAddButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.view.setDetailedViewButtonsAction(event -> {
            try {
                handleDetailedViewButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.view.setSignOutButtonAction(event -> {
            try {
                handleSignOutButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.view.setSortBoxAction(event -> {
            try {
                handleSortBoxButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.view.setFilterBoxAction(event -> {
            try {
                handleFilterBoxButton(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleAddButton(ActionEvent event) throws IOException {
        SpecifyMealTypePage temp = new SpecifyMealTypePage();
        Main.setPage(temp);
        Main.setController(new SpecifyMealTypePageController(temp, model));
    }

    private void handleDetailedViewButton(ActionEvent event) throws IOException {
        Object obj = event.getSource();
        Parent par = ((Button) (obj)).getParent();
        System.out.println(par);
        Parent par2 = ((StackPane) (par)).getParent();
        System.out.println(par2);
        Parent par3 = ((BorderPane) (par2)).getParent();
        System.out.println(par3);
        System.out.println(((RecipeItem) par3).getRecipe().getTitle());

        Recipe recipe = ((RecipeItem) par3).getRecipe();
        DetailedRecipePage temp = new DetailedRecipePage(recipe, false);
        Main.setPage(temp);
        Main.setController(new DetailedRecipePageController(temp, model));
    }

    private void handleSortBoxButton(ActionEvent event) throws IOException {
        List<Recipe> tempRecipes = view.recipes;
        String selectedItem = view.getSortBox().getSelectionModel().getSelectedItem();
        // clear list
        for (int i = 0; i < view.vbox.getChildren().size(); i++) {
            view.vbox.getChildren().removeIf(recipeItem -> recipeItem instanceof RecipeItem);
        }
        System.out.println(view.vbox.getChildren().size());
        // compare, then determine sort method
        if (selectedItem == "A-Z") { // sort A-Z logic
            System.out.println("A-Z selected");
            Collections.sort(tempRecipes, Comparator.comparing(Recipe::getTitle));
        } else if (selectedItem == "Z-A") { // sort Z-A logic
            System.out.println("Z-A selected");
            Collections.sort(tempRecipes, Comparator.comparing(Recipe::getTitle).reversed());
        } else if (selectedItem == "Oldest First") { // sort oldest first logic use creation time
            System.out.println("Oldest first selected");
            Collections.sort(tempRecipes, Comparator.comparing(recipe -> Integer.parseInt(recipe.getCreationTime())));
        } else { // default; newest first
            System.out.println("newest first selected");
            // Collections.sort(tempRecipes, Comparator.comparing(recipe ->
            // Integer.parseInt(recipe.getCreationTime())).reversed());
        }
        // re-populate list
        for (Recipe x : tempRecipes) {
            RecipeItem Item = new RecipeItem();
            Item.setRecipe(x);
            view.vbox.getChildren().add(Item);
            view.setDetailedViewButtonsAction(event1 -> {
                try {
                    handleDetailedViewButton(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println(view.vbox.getChildren().size());

    }

    private void handleFilterBoxButton(ActionEvent event) throws IOException {
        List<Recipe> tempRecipes = view.recipes;
        String selectedItem = view.getFilterBox().getSelectionModel().getSelectedItem();
        // clear list
        for (int i = 0; i < view.vbox.getChildren().size(); i++) {
            view.vbox.getChildren().removeIf(recipeItem -> recipeItem instanceof RecipeItem);
        }
        if (selectedItem == "Breakfast") {
            for (Recipe x : tempRecipes) {
                if (x.getMealType() == "Breakfast") {
                    RecipeItem Item = new RecipeItem();
                    Item.setRecipe(x);
                    view.vbox.getChildren().add(Item);
                    view.setDetailedViewButtonsAction(event1 -> {
                        try {
                            handleDetailedViewButton(event);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } else if (selectedItem == "Lunch") {
            for (Recipe x : tempRecipes) {
                if (x.getMealType() == "Lunch") {
                    RecipeItem Item = new RecipeItem();
                    Item.setRecipe(x);
                    view.vbox.getChildren().add(Item);
                    view.setDetailedViewButtonsAction(event1 -> {
                        try {
                            handleDetailedViewButton(event);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } else if (selectedItem == "Dinner") {
            for (Recipe x : tempRecipes) {
                if (x.getMealType() == "Dinner") {
                    RecipeItem Item = new RecipeItem();
                    Item.setRecipe(x);
                    view.vbox.getChildren().add(Item);
                    view.setDetailedViewButtonsAction(event1 -> {
                        try {
                            handleDetailedViewButton(event);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } else {
            for (Recipe x : tempRecipes) {
                RecipeItem Item = new RecipeItem();
                Item.setRecipe(x);
                view.vbox.getChildren().add(Item);
                view.setDetailedViewButtonsAction(event1 -> {
                    try {
                        handleDetailedViewButton(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private void handleSignOutButton(ActionEvent event) throws IOException {
        LoginPage loginPage = new LoginPage();
        LoginPageController controller = new LoginPageController(loginPage, new Model());

        // Reset Rememvber Me Logic
        FileWriter file = new FileWriter("RememberMe.csv", false);
        file.write("0");
        file.close();

        // Set Page
        Main.setPage(loginPage);
        Main.setController(controller);
    }
}
