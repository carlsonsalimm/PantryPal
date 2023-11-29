package project;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class MongoDBProject {
    private static final String CONNECTION_STRING = "mongodb+srv://carlsonsalimm:bADauWkgMpSjWAfw@cluster0.zha98w6.mongodb.net/?retryWrites=true&w=majority";
    private static final String DATABASE_NAME = "PantryPal";
    private static final String COLLECTION_NAME = "users";

    public static void main(String[] args) {
        String username = "carl";
        String password = "1234";
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
        clearAllRecipes(username,password);
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

    //updateRecipe creates a new recipe if the recipeTitle does not exist, and updates if it exists.
    public static void updateRecipe(String username, String password, String recipeTitle, String updatedMealType, String updatedIngredients,String updatedInstructions) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    
            // Check if the username and password match
            Document userDocument = collection.find(new Document("username", username).append("password", password)).first();
    
            if (userDocument != null) {
                // Check if the recipe with the specified title exists
                List<Document> recipeList = (List<Document>) userDocument.get("RecipeList");
    
                if (recipeExistsWithTitle(recipeList, recipeTitle)) {
                    // Update the specified recipe in RecipeList
                    Document updateFields = new Document("RecipeList.$.recipeTitle", recipeTitle)
                            .append("RecipeList.$.mealType", updatedMealType)
                            .append("RecipeList.$.ingredients", updatedIngredients)
                            .append("RecipeList.$.instructions", updatedInstructions)
                            .append("RecipeList.$.creation_date", System.currentTimeMillis());
    
                    collection.updateOne(
                            new Document("username", username).append("password", password)
                                    .append("RecipeList.recipeTitle", recipeTitle),
                            new Document("$set", updateFields)
                    );
                    //System.out.println("Recipe updated successfully.");
                } else {
                    // Add a new recipe to RecipeList
                    Document newRecipe = new Document("recipeTitle", recipeTitle)
                            .append("mealType", updatedMealType)
                            .append("ingredients", updatedIngredients)
                            .append("instructions", updatedInstructions)
                            .append("creation_date", System.currentTimeMillis());
    
                    collection.updateOne(
                            new Document("username", username).append("password", password),
                            new Document("$push", new Document("RecipeList", newRecipe))
                    );
                    //System.out.println("New recipe added successfully.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    private static boolean recipeExistsWithTitle(List<Document> recipeList, String recipeTitle) {
        for (Document existingRecipe : recipeList) {
            String existingTitle = existingRecipe.getString("recipeTitle");
            if (existingTitle != null && existingTitle.equals(recipeTitle)) {
                return true;
            }
        }
        return false;
    }

}
