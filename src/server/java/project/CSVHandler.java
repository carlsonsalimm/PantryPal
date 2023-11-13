package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {
    private String fileName = "Recipes.csv";

    public List<Recipe> readRecipes() throws IOException {

        List<Recipe> recipes = new ArrayList<Recipe>();
        FileReader file; // Contents of file
        try {
            file = new FileReader(fileName);
            BufferedReader buffer = new BufferedReader(file);

            try {
                String line = ""; // Reads line by line of file
                while ((line = buffer.readLine()) != null) { // Sets a new task per line
                    String[] tokens = line.split(";");
                    Recipe recipe = new Recipe(tokens[0], tokens[1].replace("\\n", "\n"));
                    recipes.add(recipe);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("CSVHandler: reading recipes error");
            }

            buffer.close();
            file.close();
        } catch (FileNotFoundException e) {
            File recipesCSV = new File(fileName);
            try {
                recipesCSV.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return recipes;
    }

    public boolean writeRecipes(Recipe recipe) throws IOException {
        boolean recipeExists = false;

        // check if the recipe we're saving already exists
        System.out.println("Writing a new recipe.");
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String next;
            while (br.ready()) {
                next = br.readLine();
                String[] tokens = next.split(";");
                if (tokens[0].trim().equals(recipe.getTitle().trim())
                        && tokens[1].trim().replace("\\n", "\n").equals(recipe.getInstructions())) {
                    recipeExists = true;
                    break; // Stop iterating if the recipe is found
                }
            }
            fr.close();
            br.close();
        } catch (FileNotFoundException e) {
            File recipesCSV = new File(fileName);
            try {
                recipesCSV.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // save the new recipe at the top of the CSV
        if (!recipeExists) {
            try {
                List<Recipe> recipes = readRecipes(); // Read existing recipes
                recipes.add(0, recipe);
                FileWriter fw = new FileWriter(fileName);
                for (Recipe r : recipes) {
                    fw.write(r.getTitle().trim() + ";" + r.getInstructions().trim().replace("\n", "\\n") + "\r\n");
                }
                fw.close();
            } catch (Exception e) {
                System.err.println("Error saving recipe");
            }
            return true;
        }
        return false;
    }

    public void updateRecipe(Recipe oldRecipe, Recipe newRecipe) throws IOException {
        // update oldRecipe to be newRecipe
        List<Recipe> recipes = readRecipes(); // Read existing recipes

        // Find and update the changed recipe
        for (Recipe r : recipes) {
            if (r.getTitle().equals(oldRecipe.getTitle())
                    && r.getInstructions().equals(oldRecipe.getInstructions())) {
                r.setInstructions(newRecipe.getInstructions());
                break; // Stop iterating once the recipe is found and removed
            }
        }

        // Write the updated list of recipes back to the CSV file
        try {
            FileWriter fw = new FileWriter(fileName);
            for (Recipe r : recipes) {
                fw.write(r.getTitle().trim() + ";" + r.getInstructions().trim().replace("\n", "\\n") + "\r\n");
            }
            fw.close();
        } catch (Exception e) {
            System.err.println("Error deleting recipe");
        }

    }

    public void deleteRecipe(Recipe recipe) throws IOException {
        List<Recipe> recipes = readRecipes(); // Read existing recipes

        // Find and remove the recipe to be deleted
        for (Recipe r : recipes) {
            if (r.getTitle().equals(recipe.getTitle()) && r.getInstructions().equals(recipe.getInstructions())) {
                recipes.remove(r);
                break; // Stop iterating once the recipe is found and removed
            }
        }

        // Write the updated list of recipes back to the CSV file
        try {
            FileWriter fw = new FileWriter(fileName);
            for (Recipe r : recipes) {
                fw.write(r.getTitle().trim() + ";" + r.getInstructions().trim().replace("\n", "\\n") + "\r\n");
            }
            fw.close();
        } catch (Exception e) {
            System.err.println("Error deleting recipe");
        }
    }
}
