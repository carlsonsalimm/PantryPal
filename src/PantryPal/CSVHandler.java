package src.PantryPal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVHandler {
    private static String fileName = "Recipes.csv";

    public static List<Recipe> readRecipes() throws IOException{

        List<Recipe> recipes = new ArrayList<Recipe>();
        FileReader file; // Contents of file
        try {
            file = new FileReader(fileName);
            BufferedReader buffer = new BufferedReader(file, 16384);
            
            try {
                String line = ""; // Reads line by line of file
                while ((line = buffer.readLine()) != null) { // Sets a new task per line
                String[] tokens = line.split(";");
                Recipe recipe = new Recipe(tokens[0], tokens[1]);
                recipes.add(recipe);
                }

                buffer.close();
                file.close();
            } catch (Exception e) {
            }

        } catch (FileNotFoundException e) {
        }

        return recipes;
    }

    public static void writeRecipes(Recipe recipe) throws IOException {
        boolean recipeExists = false;

        // check if the recipe we're saving already exists
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String next;
            while (br.ready()) {
                next = br.readLine();
                String[] tokens = next.split(";");
                if (tokens[0].trim().equals(recipe.getTitle()) && tokens[1].trim().equals(recipe.getInstructions())) {
                    recipeExists = true;
                }
            }
            fr.close();
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error loading recipes");
        }

        // save the new/updated recipe
        if (!recipeExists) {
            try {
                String oldFile = Files.readString(Paths.get(fileName));
                FileWriter fw = new FileWriter(fileName);
                fw.write(recipe.getTitle() + ";" + recipe.getInstructions() + "\r\n");
                fw.append(oldFile);
                fw.close();
            } catch (Exception e) {
                System.err.println("Error saving recipe");
            } 
        }
    }
}
