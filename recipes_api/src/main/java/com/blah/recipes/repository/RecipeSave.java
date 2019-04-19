package com.blah.recipes.repository;

import com.blah.recipes.model.Recipe;

interface RecipeSave {
    Recipe saveRecipe(Recipe recipe);
}