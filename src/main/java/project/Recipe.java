package project;

public class Recipe {
    String title;
    String instructions;
    String ingredients;
    String mealType;
    String creationTime; 

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
}
