package com.blah.recipe;

import com.blah.recipe.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@RestController
public class RecipeController {

    Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);

    @RequestMapping(value = "/recipe/default", method = RequestMethod.GET)
    public Recipe getRecipe() {
        LOGGER.info("Returning default recipe");
        return defaultRecipe();
    }

    private Recipe defaultRecipe() {
        var quantity = new Quantity(2, Unit.PORTION);
        var ingredient = new Ingredient("Eggs", quantity, Optional.empty());

        var instructions = new Instructions();
        instructions.addNextStep("Heat a small pan of water till boiling");
        instructions.addNextStep("Using tablespoon, lower egg(s) into boiling water");
        instructions.addNextStep("Turn heat down to simmer and cook egg(s) for 8 minutes");
        instructions.addNextStep("Remove eggs using tablespoon and allow to cool for 5 minutes");
        instructions.addNextStep("Crack shell of eggs with a spoon and peel off all egg shell");

        return new Recipe("Boiled eggs", Set.of(ingredient), instructions);
    }
}
