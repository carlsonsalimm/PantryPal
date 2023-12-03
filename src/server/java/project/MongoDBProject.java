package project;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MongoDBProject {
    private static final String CONNECTION_STRING = "mongodb+srv://carlsonsalimm:bADauWkgMpSjWAfw@cluster0.zha98w6.mongodb.net/?retryWrites=true&w=majority";
    private static final String DATABASE_NAME = "PantryPal";
    private static final String COLLECTION_NAME = "users";
    private static final int BUFFER_SIZE = 4096;
    //this should be changed based on where we want to store the image we got from mongodb, output.jpg is the name of the image file we got from mongodb
    private static final String imagePath = "C:\\Users\\carls\\OneDrive\\Documents\\GitHub\\cse-110-project-team-31\\output.jpg"; 
    public static void main(String[] args) {
        String username = "carlTest";
        String password = "14141241";
        boolean success = createUser(username, password);
        if (success)
            System.out.println("user created");
        else
            System.out.println("user already exist or there is a problem");
        success = login(username, password);
        if (success)
            System.out.println("user exist");
        else
            System.out.println("user does not exist");
        //clearAllRecipes(username,password);
        String imagePath1 = "C:\\vscode_code\\cse 110\\mongoDB_project\\app\\output1.jpg";
        updateRecipe(username,password,"chicken thighssss", "dinner", "chicken thighs", "oven that chicken",0,imagePath);
        retrieveAndSaveImage(username, password, "chicken thighssss", imagePath1);
        // updateRecipe(username, password, "chicken thigh", "", "", "");
        // updateRecipe(username, password, "beef brocolli", "new instructions", "lunch", "beef");
        //printRecipeList(username, password);
        //deleteRecipe(username, password, "beef brocolli");
    }

    public static boolean createUser(String username, String password) { // return true if there is no user with the username and we added it to the database
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Check if the user already exists
            if (collection.find(new Document("username", username)).first() != null) {
                System.out.println("User already exists: " + username);
                return false; // User already exists
            }

            // If user does not exist, create the user
            List<Document> emptyRecipeList = new ArrayList<>(); // change to RecipeList class
            Document userDocument = new Document("username", username)
                    .append("password", password)
                    .append("RecipeList", emptyRecipeList);

            InsertOneResult result = collection.insertOne(userDocument);
            System.out.println("User created with _id: " + result.getInsertedId());
            return true; // User created successfully
        } catch (Exception e) {
            e.printStackTrace();
            return false; // An error occurred
        }
    }

    public static boolean login(String username, String password) { // return true if there exist a user with the
                                                                    // username and password in the database
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Check if the user exists with the provided username and password and
            // userDocument will now contain username, password, and RecipeList
            Document userDocument = collection.find(new Document("username", username).append("password", password)).first();

            // If the user document is not null, login is successful
            if (userDocument != null) {
                //System.out.println("Login successful for user: " + username);
                return true;
            } else {
                //System.out.println("Invalid username or password");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // An error occurred
        }
    }

    public static List<Document> getRecipeList(String username, String password) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Check if the username and password match
            Document userDocument = collection.find(new Document("username", username).append("password", password))
                    .first();

            if (userDocument != null) {
                // User found, get the RecipeList
                List<Document> recipeList = (List<Document>) userDocument.get("RecipeList");
                return recipeList;
            } else {
                // User not found, handle accordingly (throw exception, return empty list, etc.)
                //System.out.println("User not found");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //we can determine if we want to update or add a new recipe by specifying the creationTime, 
    //if it is 0 that means we want to add a new recipe, but if it's not !=0 then we just want to update a existing one
    public static void updateRecipe(String username, String password, String recipeTitle,
                                String updatedMealType, String updatedIngredients,
                                String updatedInstructions, long creationTime, String imagePath) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Check if the username and password match
            Document userDocument = collection.find(new Document("username", username).append("password", password)).first();

            if (userDocument != null) {
                // Check if the recipe with the specified creationTime exists
                List<Document> recipeList = (List<Document>) userDocument.get("RecipeList");
                boolean recipeExists = recipeExistsWithTitle(recipeList, recipeTitle);
                if (recipeExists) {
                    // Update the existing recipe based on creationTime
                    Document existingRecipe = findRecipeByCreationTime(recipeList, creationTime);
                    existingRecipe.put("recipeTitle", recipeTitle);
                    existingRecipe.put("mealType", updatedMealType);
                    existingRecipe.put("ingredients", updatedIngredients);
                    existingRecipe.put("instructions", updatedInstructions);
                     // Convert the image file to a byte array to store in mongodb
                    byte[] imageData = Files.readAllBytes(Paths.get(imagePath));
                    Binary imageBinary = new Binary(imageData);
                    existingRecipe.put("imageData", imageBinary); // For existing recipes
                    //existingRecipe.put("imageData", new Binary(imageData));
                    collection.updateOne(
                            new Document("username", username).append("password", password)
                                    .append("RecipeList.creationTime", creationTime),
                            new Document("$set", new Document("RecipeList.$", existingRecipe))
                    );
                    //System.out.println("Recipe updated successfully.");
                } else {
                    if (creationTime == 0) {
                        // Add a new recipe to RecipeList with current timestamp as creationTime
                        creationTime = System.currentTimeMillis();
                    }

                    Document newRecipe = new Document("recipeTitle", recipeTitle)
                            .append("mealType", updatedMealType)
                            .append("ingredients", updatedIngredients)
                            .append("instructions", updatedInstructions)
                            .append("creationTime", creationTime);
                    byte[] imageData = Files.readAllBytes(Paths.get(imagePath));
                    Binary imageBinary = new Binary(imageData);
                    newRecipe.put("imageData", imageBinary);
                    collection.updateOne(
                            new Document("username", username).append("password", password),
                            new Document("$push", new Document("RecipeList", newRecipe))
                    );
                    //System.out.println("Recipe added successfully.");
                }
            } else {
                System.out.println("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //delete user in mongodb based on username and password
    public static boolean deleteUser(String username, String password) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    
            // Check if the username and password match
            Document userDocument = collection.find(new Document("username", username).append("password", password)).first();
    
            if (userDocument != null) {
                // Delete the user document from the collection
                collection.deleteOne(new Document("username", username).append("password", password));
                //System.out.println("User deleted successfully.");
                return true;
            } else {
                //System.out.println("User with username '" + username + "' does not exist. Cannot delete.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // An error occurred
        }
    }
    
    public static void deleteRecipe(String username, String password, String recipeTitle) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    
            // Check if the username and password match
            Document userDocument = collection.find(new Document("username", username).append("password", password)).first();
    
            if (userDocument != null) {
                // Check if the recipe with the specified title exists
                List<Document> recipeList = (List<Document>) userDocument.get("RecipeList");
                // If the recipe exists, proceed with deletion
                if (recipeExistsWithTitle(recipeList, recipeTitle)) {
                    // Delete the specified recipe from RecipeList
                    collection.updateOne(
                            new Document("username", username).append("password", password),
                            new Document("$pull", new Document("RecipeList", new Document("recipeTitle", recipeTitle)))
                    );
                    //System.out.println("Recipe deleted successfully.");
                } else {
                    return;
                    //System.out.println("Recipe with title '" + recipeTitle + "' does not exist. Cannot delete.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //print it locally to check but maybe not needed in the project
    public static void printRecipeList(String username, String password) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Check if the username and password match
            Document userDocument = collection.find(new Document("username", username).append("password", password))
                    .first();

            if (userDocument != null) {
                // Extract and print the RecipeList field
                List<Document> recipeList = (List<Document>) userDocument.get("RecipeList");
                System.out.println("Recipe List for " + username + ":");
                for (Document recipe : recipeList) {
                    System.out.println(recipe.toJson());
                }
            } else {
                System.out.println("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //clears all the rceipe in RecipeList for a specific user iff user and pass matches
    public static void clearAllRecipes(String username, String password) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Check if the username and password match
            Document userDocument = collection.find(new Document("username", username).append("password", password))
                    .first();

            if (userDocument != null) {
                // Clear the RecipeList by setting it to an empty list
                collection.updateOne(
                        new Document("username", username).append("password", password),
                        new Document("$set", new Document("RecipeList", new ArrayList<>())));
                //System.out.println("All recipes cleared for user: " + username);
            } else {
                //System.out.println("Invalid username or password. Cannot clear recipes.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //helper method to getCreationTimeByTitle so we can get the creationDate before updating it
    public static Long getCreationTimeByTitle(String username, String password, String recipeTitle) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    
            // Check if the username and password match
            Document userDocument = collection.find(new Document("username", username).append("password", password)).first();
    
            if (userDocument != null) {
                // Check if the recipe with the specified title exists
                List<Document> recipeList = (List<Document>) userDocument.get("RecipeList");
                Document matchingRecipe = findRecipeByTitle(recipeList, recipeTitle);
    
                if (matchingRecipe != null) {
                    // Retrieve and return the creationTime of the matching recipe
                    return matchingRecipe.getLong("creationTime");
                } else {
                    //System.out.println("Recipe with title '" + recipeTitle + "' not found.");
                }
            } else {
                //System.out.println("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return null;
    }
    //so for this function the savePath is the path on which directory we want to save the output file 
    public static void retrieveAndSaveImage(String username, String password, String recipeTitle, String savePath) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    
            Document userDocument = collection.find(new Document("username", username).append("password", password)).first();
    
            if (userDocument != null) {
                List<Document> recipeList = (List<Document>) userDocument.get("RecipeList");
                Document recipe = findRecipeByTitle(recipeList, recipeTitle);
    
                if (recipe != null) {
                    Binary imageBinary = recipe.get("imageData", Binary.class);
    
                    if (imageBinary != null) {
                        Files.write(Paths.get(savePath), imageBinary.getData());
                        System.out.println("Image retrieved and saved to: " + savePath);
                    } else {
                        System.out.println("Image data not found for recipe with title '" + recipeTitle + "'.");
                    }
                } else {
                    System.out.println("Recipe with title '" + recipeTitle + "' not found.");
                }
            } else {
                System.out.println("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //helper method to determine if there exists a recipe with the title
    private static boolean recipeExistsWithTitle(List<Document> recipeList, String recipeTitle) {
        for (Document existingRecipe : recipeList) {
            String existingTitle = existingRecipe.getString("recipeTitle");
            if (existingTitle != null && existingTitle.equals(recipeTitle)) {
                return true;
            }
        }
        return false;
    }

    //helper method to find recipe by creation time
    private static Document findRecipeByCreationTime(List<Document> recipeList, long creationTime) {
        for (Document existingRecipe : recipeList) {
            Long existingCreationTime = existingRecipe.getLong("creationTime");
            if (existingCreationTime != null && existingCreationTime.equals(creationTime)) {
                return existingRecipe;
            }
        }
        return null;
    }
    
    //helper function to find recipe by title and returns the recipe
    private static Document findRecipeByTitle(List<Document> recipeList, String recipeTitle) {
        for (Document existingRecipe : recipeList) {
            String existingTitle = existingRecipe.getString("recipeTitle");
            if (existingTitle != null && existingTitle.equals(recipeTitle)) {
                return existingRecipe;
            }
        }
        return null;
    }

    // Method to fetch a recipe by its title and return it as a Document
    public static Document getRecipeByTitle(String username, String password, String recipeTitle) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Check if the username and password match
            Document userDocument = collection.find(new Document("username", username).append("password", password)).first();

            if (userDocument != null) {
                // Find the recipe by title in the user's RecipeList
                List<Document> recipeList = (List<Document>) userDocument.get("RecipeList");
                Document recipe = findRecipeByTitle(recipeList, recipeTitle);
                return recipe; // Return the found recipe or null if not found
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if there's an error or if the user is not found
    }
}