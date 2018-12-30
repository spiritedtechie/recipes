package com.blah.recipes.health;

import com.blah.recipes.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RecipeRepositoryHealthIndicator implements HealthIndicator {

    private static final String DEFAULT_RECIPE_FETCHED_KEY = "defaultRecipeFetched";

    private Function<Boolean, Health.Builder> booleanToHealth = (Boolean isUp) -> isUp ? Health.up() : Health.down();

    private RecipeRepository recipeRepository;

    @Autowired
    public RecipeRepositoryHealthIndicator(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Health health() {
        try {
            var found = recipeRepository.findByName("Boiled Eggs").isEmpty();
            return booleanToHealth.apply(found)
                    .withDetail(DEFAULT_RECIPE_FETCHED_KEY, found)
                    .build();
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
