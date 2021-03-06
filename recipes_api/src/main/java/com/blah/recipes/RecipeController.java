package com.blah.recipes;

import com.blah.recipes.model.DefaultRecipe;
import com.blah.recipes.model.Recipe;
import com.blah.recipes.repository.RecipeRepository;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Timed(value = "recipes", percentiles = {0.5, 0.95, 0.999}, histogram = true)
public class RecipeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);

    private RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @RequestMapping(value = "/recipes", method = RequestMethod.GET)
    public Iterable<Recipe> getAllRecipes() {
        LOGGER.info("getAllRecipes");
        return recipeRepository.findAll();
    }

    @RequestMapping(value = "/recipes/{id}", method = RequestMethod.GET)
    public Recipe getRecipe(@PathVariable String id) {
        LOGGER.info("getRecipe " + id);
        validateId(id);
        return recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @RequestMapping(value = "/recipes/default", method = RequestMethod.POST)
    public Recipe newDefaultRecipe() {
        LOGGER.info("newDefaultRecipe");
        var recipe = DefaultRecipe.getInstance().build();
        var recipes = this.recipeRepository.findByName(recipe.getName());
        if (!recipes.isEmpty()) {
            return recipes.get(0);
        }
        LOGGER.info("Default recipe not found. Adding to the database.");
        return this.recipeRepository.saveRecipe(recipe);
    }

    @RequestMapping(value = "/recipes", method = RequestMethod.POST)
    public Recipe newRecipe(@RequestBody Recipe recipe) {
        LOGGER.info("newRecipe " + recipe);
        return recipeRepository.saveRecipe(recipe);
    }

    @RequestMapping(value = "/recipes/{id}", method = RequestMethod.PUT)
    public Recipe updateRecipe(@PathVariable String id, @RequestBody Recipe recipe) {
        LOGGER.info("updateRecipe " + id);
        validateId(id);
        recipe.setId(id);
        return recipeRepository.saveRecipe(recipe);
    }

    @RequestMapping(value = "/recipes/{id}", method = RequestMethod.DELETE)
    public void deleteRecipe(@PathVariable String id) {
        LOGGER.info("deleteRecipe " + id);
        validateId(id);
        recipeRepository.deleteById(id);
    }

    private void validateId(@PathVariable String id) {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Recipe ID supplied is invalid", e);
        }
    }
}
