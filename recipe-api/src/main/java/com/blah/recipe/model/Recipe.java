package com.blah.recipe.model;

import java.util.Set;

public class Recipe {

    private String name;

    private Set<Ingredient> ingredients;

    private Instructions instructions;

    public Recipe(String name, Set<Ingredient> ingredients, Instructions instructions) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public Instructions getInstructions() {
        return instructions;
    }
}
