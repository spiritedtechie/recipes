package com.blah.recipes.repository;

import com.blah.recipes.model.Recipe;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

    List<Recipe> findByName(String name);

    @EnableScan
    Iterable<Recipe> findAll();
}
