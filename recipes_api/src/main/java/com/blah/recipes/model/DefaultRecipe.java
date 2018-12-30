package com.blah.recipes.model;

import java.util.ArrayList;
import java.util.List;

import static com.blah.recipes.model.Preparation.Method;
import static com.blah.recipes.model.Preparation.Style;
import static com.blah.recipes.model.Quantity.Unit;

public class DefaultRecipe {

    private List<Ingredient> ingredients = new ArrayList<>();
    private String id;
    private String name = "Cheese Omlette";

    public static DefaultRecipe getInstance() {
        return new DefaultRecipe();
    }

    public DefaultRecipe withId(String id) {
        this.id = id;
        return this;
    }

    public DefaultRecipe withIngedient(Ingredient ingedient) {
        this.ingredients.add(ingedient);
        return this;
    }

    public DefaultRecipe withName(String name) {
        this.name = name;
        return this;
    }

    public Recipe build() {
        var oilQuantity = new Quantity(1, Unit.TABLESPOON);
        var oil = new Ingredient("Olive Oil", oilQuantity);

        var eggsQuantity = new Quantity(2, Unit.WHOLE);
        var eggsPreparation = new Preparation(Method.BEAT);
        var eggs = new Ingredient("Egg", eggsQuantity, eggsPreparation);

        var cheeseQuantity = new Quantity(30, Unit.GRAMS);
        var cheesePreparation = new Preparation(Method.GRATE, Style.FINE);
        var cheese = new Ingredient("Cheddar Cheese", cheeseQuantity, cheesePreparation);

        var instructions = new Instructions();
        instructions.addNextStep(new Step("Heat olive oil in frying pan for a few minutes"));
        instructions.addNextStep(new Step("Pouring eggs mixture into frying pan"));
        instructions.addNextStep(new Step("Cook on medium heat until egg mixture hardens"));
        instructions.addNextStep(new Step("Once able, flip the omlette using a large spatula"));
        instructions.addNextStep(new Step("Add cheese over omlette and allow to melt"));
        instructions.addNextStep(new Step("Flip over half of omlette over itself to serve"));

        this.ingredients.add(oil);
        this.ingredients.add(eggs);
        this.ingredients.add(cheese);

        Recipe recipe = new Recipe(this.name, this.ingredients, instructions);
        recipe.setId(this.id);

        return recipe;
    }
}
