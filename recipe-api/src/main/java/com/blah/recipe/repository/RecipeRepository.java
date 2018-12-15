package com.blah.recipe.repository;

import com.blah.recipe.model.Recipe;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

    List<Recipe> findByName(String name);

    @EnableScan
    Iterable<Recipe> findAll();
}
