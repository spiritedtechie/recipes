package com.blah.recipe.repository;

import com.blah.recipe.model.Recipe;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface RecipeRepository extends CrudRepository<Recipe, String> {

}
