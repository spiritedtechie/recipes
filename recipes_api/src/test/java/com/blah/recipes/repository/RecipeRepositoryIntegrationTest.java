package com.blah.recipes.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.blah.config.DynamoDBTestConfig;
import com.blah.recipes.model.DefaultRecipe;
import com.blah.recipes.model.Recipe;
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
                index -> index
                        .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
                        .withProvisionedThroughput(provisionedThroughput));

        amazonDynamoDB.createTable(createTableRequest);
    }

    private void deleteTable() {
        var createTableRequest = dynamoDBMapper.generateDeleteTableRequest(Recipe.class);
        amazonDynamoDB.deleteTable(createTableRequest);
    }

    @Test
    public void testRepositorySavesGeneratingNewId() {
        var recipe = DefaultRecipe.getInstance().build();

        var saved = repository.save(recipe);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void testRepositorySavesWithExistingId() {
        String id = "12345";
        var recipe = DefaultRecipe.getInstance().withId(id).build();

        repository.save(recipe);

        var result = repository.findById(id);
        assertThat(result).isNotEmpty();
        assertThat(result).get().hasFieldOrPropertyWithValue("id", id);
    }

    @Test
    public void testRepositorySavesAllData() {
        var testRecipe = DefaultRecipe.getInstance().build();

        repository.save(testRecipe);

        var savedRecipe = repository.findById(testRecipe.getId());
        assertThat(savedRecipe).isPresent();
        assertThat(savedRecipe).get().isEqualTo(testRecipe);
    }

    @Test
    public void testFindByName() {
        var testRecipe = DefaultRecipe.getInstance().build();
        repository.save(testRecipe);

        var results = repository.findByName("Cheese Omlette");

        assertThat(results).isNotEmpty();
        assertThat(results).first().isEqualTo(testRecipe);
    }

    @Test
    public void testDeleteById() {
        String id = "12345";
        var testRecipe = DefaultRecipe.getInstance().withId(id).build();
        repository.save(testRecipe);
        var savedRecipe = repository.findById(id);
        assertThat(savedRecipe).isNotEmpty();

        repository.deleteById(id);

        var result = repository.findById(testRecipe.getId());
        assertThat(result).isEmpty();
    }

}
