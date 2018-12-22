package com.blah.recipes;

import com.blah.recipes.model.DefaultRecipe;
import com.blah.recipes.model.Recipe;
import com.blah.recipes.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);

    private RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
        addDefaultRecipeToDatabaseIfNotAlreadyThere();
    }

    @RequestMapping(value = "/recipes", method = RequestMethod.GET)
    public Iterable<Recipe> getAllRecipes() {
        LOGGER.info("getAllRecipes");

        return recipeRepository.findAll();
    }

    private void addDefaultRecipeToDatabaseIfNotAlreadyThere() {
        var recipes = this.recipeRepository.findByName("Boiled eggs");
        if (recipes.isEmpty()) {
            LOGGER.info("Default recipe not found. Adding to the database.");
            this.recipeRepository.save(DefaultRecipe.build());
        }
    }
}