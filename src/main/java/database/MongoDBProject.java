package database;

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

import java.util.ArrayList;
import java.util.Collections;
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
        addRecipe(username, password, "beef brocolli", "saute beef and brocolli", "dinner", "beef & brocolli");
        printRecipeList(username, password);
        updateRecipe(username, password, "new recipe", "", "", "");
        updateRecipe(username, password, "beef brocolli", "new instructions", "lunch", "beef");
        printRecipeList(username, password);
        addRecipe(username, password, "beef", "saute beef and brocolli", "dinner", "beef & brocolli");
        deleteRecipe(username, password, "beef brocolli");
    }

    public static boolean createUser(String username, String password) { // return true if there is no user with the
                                                                         // username and we added it to the database
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
            Document userDocument = collection.find(new Document("username", username).append("password", password))
                    .first();

            // If the user document is not null, login is successful
            if (userDocument != null) {
                System.out.println("Login successful for user: " + username);
                return true;
            } else {
                System.out.println("Invalid username or password");
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
            System.out.println("User not found");
            return null; // or return null, or throw an exception, depending on your requirements
        }
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}


    public static void addRecipe(String username, String password, String recipeTitle, String instructions,
            String mealType, String ingredients) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Check if the username and password match
            Document userDocument = collection.find(new Document("username", username).append("password", password))
                    .first();

            if (userDocument != null) {
                List<Document> recipeList = (List<Document>) userDocument.get("RecipeList"); // we first check if there
                                                                                             // exist the recipeTitle in
                                                                                             // recipeList, if it exists
                                                                                             // then just return.
                for (Document existingRecipe : recipeList) {
                    if (existingRecipe.getString("recipeTitle").equals(recipeTitle)) {
                        System.out.println(
                                "Recipe with title '" + recipeTitle + "' already exists. Cannot add a new one.");
                        return; // Exit the method if the recipe title already exists
                    }
                }
                // Create a new recipe document
                Document newRecipe = new Document("recipeTitle", recipeTitle)
                        .append("instructions", instructions)
                        .append("mealType", mealType)
                        .append("ingredients", ingredients)
                        .append("creation-date", System.currentTimeMillis()); // represents the time created

                // Update the user document to add the new recipe to RecipeList
                collection.updateOne(
                        new Document("username", username).append("password", password),
                        new Document("$push", new Document("RecipeList", newRecipe)));
                System.out.println("Recipe added successfully.");
            } else {
                System.out.println("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateRecipe(String username, String password, String recipeTitle, String updatedInstructions,
        String updatedMealType, String updatedIngredients) {
    try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        // Check if the username and password match
        Document userDocument = collection.find(new Document("username", username).append("password", password))
                .first();

        if (userDocument != null) {
            // Construct the update based on non-null fields
            Document updateFields = new Document();
            if (updatedInstructions != null) {
                updateFields.append("RecipeList.$.instructions", updatedInstructions);
            }
            if (updatedMealType != null) {
                updateFields.append("RecipeList.$.mealType", updatedMealType);
            }
            if (updatedIngredients != null) {
                updateFields.append("RecipeList.$.ingredients", updatedIngredients);
            }
            // Update the specified recipe in RecipeList if it exists
            Bson query = Filters.and(
                Filters.eq("username", username),
                Filters.eq("password", password),
                Filters.elemMatch("RecipeList", Filters.eq("recipeTitle", recipeTitle))
            );

            Bson update = Updates.combine(updateFields);

            UpdateResult updateResult = collection.updateOne(query, update);

            if (updateResult.getModifiedCount() > 0) {
                System.out.println("Recipe updated successfully.");
            } else {
                System.out.println("Recipe with title '" + recipeTitle + "' does not exist. Cannot update.");
            }
        } else {
            System.out.println("Invalid username or password");
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
            Document userDocument = collection.find(new Document("username", username).append("password", password))
                    .first();

            if (userDocument != null) {
                // Check if the recipe with the specified title exists
                List<Document> recipeList = (List<Document>) userDocument.get("RecipeList");
                boolean recipeExists = false;

                for (Document existingRecipe : recipeList) {
                    if (existingRecipe.getString("recipeTitle").equals(recipeTitle)) {
                        recipeExists = true;
                        break;
                    }
                }

                // If the recipe exists, proceed with deletion
                if (recipeExists) {
                    // Delete the specified recipe from RecipeList
                    collection.updateOne(
                            new Document("username", username).append("password", password),
                            new Document("$pull",
                                    new Document("RecipeList", new Document("recipeTitle", recipeTitle))));
                    System.out.println("Recipe deleted successfully.");
                } else {
                    System.out.println("Recipe with title '" + recipeTitle + "' does not exist. Cannot delete.");
                }
            } else {
                System.out.println("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    // In MongoDBProject.java

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
                System.out.println("All recipes cleared for user: " + username);
            } else {
                System.out.println("Invalid username or password. Cannot clear recipes.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
