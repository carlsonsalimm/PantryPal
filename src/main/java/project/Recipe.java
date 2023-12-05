package project;

public class Recipe {
    String title;
    String instructions;
    String ingredients;
    String mealType;
    String creationTime; 
    String imageURL;

    public Recipe(String title, String instructions, String ingredients, String mealType){
        this.title = title;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.mealType = mealType;
        this.creationTime = null;
    }
    public Recipe(String title, String instructions, String ingredients, String mealType, String creationTime) {
        this.title = title;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.mealType = mealType;
        this.creationTime = creationTime;
    }

    //new constructor for recipe that includes imageURL (please update the other constructors if you change this one)
    public Recipe(String title, String instructions, String ingredients, String mealType, String creationTime, String imageURL) {
        this.title = title;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.mealType = mealType;
        this.creationTime = creationTime;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return this.title;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public String getMealType() {
        return this.mealType;
    }

    public String getCreationTime() {
        return this.creationTime;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
