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
