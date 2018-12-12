package com.blah.recipe.model;

import java.util.List;

public class DefaultRecipe {

    public static Recipe build() {
        var quantity = new Quantity(2, Unit.PORTION);
        var ingredient = new Ingredient("Eggs", quantity);

        var instructions = new Instructions();
        instructions.addNextStep("Heat a small pan of water till boiling");
        instructions.addNextStep("Using tablespoon, lower egg(s) into boiling water");
        instructions.addNextStep("Turn heat down to simmer and cook egg(s) for 8 minutes");
        instructions.addNextStep("Remove eggs using tablespoon and allow to cool for 5 minutes");
        instructions.addNextStep("Crack shell of eggs with a spoon and peel off all egg shell");

        return new Recipe("Boiled eggs", List.of(ingredient), instructions);
    }
}
