package com.blah.recipes.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.blah.config.DynamoDBTestConfig;
import com.blah.recipes.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RecipeRepository.class, DynamoDBTestConfig.class})
public class RecipeRepositoryIntegrationTest {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private RecipeRepository repository;

    private DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

    @Before
    public void setup() {
        createTable();
    }

    @After
    public void teardown() {
        deleteTable();
    }

    private void createTable() {
        var provisionedThroughput = new ProvisionedThroughput(1L, 1L);
        var createTableRequest = dynamoDBMapper.generateCreateTableRequest(Recipe.class);
        createTableRequest.setProvisionedThroughput(provisionedThroughput);
        createTableRequest.getGlobalSecondaryIndexes().forEach(
                index -> index.setProvisionedThroughput(provisionedThroughput));

        amazonDynamoDB.createTable(createTableRequest);
    }

    private void deleteTable() {
        var createTableRequest = dynamoDBMapper.generateDeleteTableRequest(Recipe.class);
        amazonDynamoDB.deleteTable(createTableRequest);
    }

    @Test
    public void testRepositorySavesWithId() {
        var recipe = DefaultRecipe.build();

        repository.save(recipe);

        assertThat(recipe.getId()).isNotNull();
    }

    @Test
    public void testRepositorySavesAllData() {
        var testRecipe = DefaultRecipe.build();

        repository.save(testRecipe);

        var savedRecipe = repository.findById(testRecipe.getId());
        assertThat(savedRecipe).isPresent();
        assertThat(savedRecipe).hasValueSatisfying(recipe -> {
            assertThat(recipe.getName()).isEqualTo("Boiled eggs");

            assertThat(recipe.getIngredients()).hasSize(1);
            assertThat(recipe.getIngredients()).contains(new Ingredient("Eggs", new Quantity(2, Unit.PORTION)));

            assertThat(recipe.getInstructions().getSteps()).hasSize(5);
            assertThat(recipe.getInstructions().getSteps()).containsExactlyInAnyOrder(
                    "Heat a small pan of water till boiling",
                    "Using tablespoon, lower egg(s) into boiling water",
                    "Turn heat down to simmer and cook egg(s) for 8 minutes",
                    "Remove eggs using tablespoon and allow to cool for 5 minutes",
                    "Crack shell of eggs with a spoon and peel off all egg shell"
            );
        });
    }

    @Test
    public void testFindByName() {
        var testRecipe = DefaultRecipe.build();
        repository.save(testRecipe);

        var results = repository.findByName("Boiled eggs");

        assertThat(results).isNotEmpty();
        assertThat(results).first().hasFieldOrPropertyWithValue("name", "Boiled eggs");
    }

}
