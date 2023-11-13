package project;

import javax.swing.text.View;

import javafx.event.ActionEvent;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        this.view.setBackButtonAction(this::handleBackButton);
        this.view.setDeleteRecipeButtonAction(this::handleDelButton);
        this.view.setSaveButtonAction(this::handleSaveButton);
        this.view.setGetButtonAction(this::handleViewButton);
    }


    // performRequest(METHOD, newRe)
    public static List<Recipe> getRecipes() {
        List<Recipe> response = model.performRequest("GET")
        return response;
    }

    private void handleBackButton(ActionEvent event) {
        // Go back to RecipeListPage
        String response = model.performRequest("POST", language, year, null);
        
        
    }

    private void handleDelButton(ActionEvent event) {
        String query = view.getQuery();
        String response = model.performRequest("GET", null, null, query);
        view.showAlert("Response", response);
    }

    private void handleSaveButton(ActionEvent event) {
        
        String language = view.getLanguage();
        String year = view.getYear();
        String response = model.performRequest("PUT", language, year, null);
        view.showAlert("Response", response);
    }

    private void handleViewButton(ActionEvent event) {
        String query = view.getQuery();
        String response = model.performRequest("DELETE", null, null, query);
        view.showAlert("Response", response);
    }
}