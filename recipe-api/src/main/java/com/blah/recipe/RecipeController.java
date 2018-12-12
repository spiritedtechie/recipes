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
        return DefaultRecipe.build();
    }


}
