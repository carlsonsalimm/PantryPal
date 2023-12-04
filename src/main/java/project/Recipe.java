package project;

public class Recipe {
    String title;
    String instructions;

    public Recipe(String title, String instructions) {
        this.title = title;
        this.instructions = instructions;
    }

    public String getTitle() {
        return this.title;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public void setTitle(String s) {
        this.title = s;
    }

    public void setInstructions(String s) {
        this.instructions = s;
    }
}

// Updated Recipe class, please uncomment and implement
// package project;

// public class Recipe {
// String title;
// String instructions;
// String ingredients;
// String mealType;
// String creationTime;

// public Recipe(String title, String instructions, String ingredients, String
// mealType) {
// this.title = title;
// this.instructions = instructions;
// this.ingredients = ingredients;
// this.mealType = mealType;
// this.creationTime = null;
// }

// public Recipe(String title, String instructions, String ingredients, String
// mealType, String creationTime) {
// this.title = title;
// this.instructions = instructions;
// this.ingredients = ingredients;
// this.mealType = mealType;
// this.creationTime = creationTime;
// }

// public String getTitle() {
// return this.title;
// }

// public String getInstructions() {
// return this.instructions;
// }

// public String getIngredients() {
// return this.ingredients;
// }

// public String getMealType() {
// return this.mealType;
// }

// public String getCreationTime() {
// return this.creationTime;
// }

// public void setTitle(String s) {
// this.title = s;
// }

// public void setInstructions(String s) {
// this.instructions = s;
// }

// public void setIngredients(String s) {
// this.ingredients = s;
// }
// }
