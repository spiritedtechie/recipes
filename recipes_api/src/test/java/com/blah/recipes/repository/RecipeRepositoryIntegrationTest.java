package com.blah.recipes.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.blah.config.DynamoDBTestConfig;
import com.blah.config.S3TestConfig;
import com.blah.recipes.model.DefaultRecipe;
import com.blah.recipes.model.Recipe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.function.Supplier;

import static com.blah.imageutils.TestImages.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RecipeRepository.class, DynamoDBTestConfig.class, S3TestConfig.class})
public class RecipeRepositoryIntegrationTest {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket.images}")
    private String s3Bucket;

    @Autowired
    private RecipeRepository repository;

    @MockBean
    private Supplier<UUID> uuidSupplier;

    private DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

    @Before
    public void setup() {
        createTable();
        createS3Bucket();
        when(uuidSupplier.get()).thenReturn(TEST_IMAGE_FILE_UUID);
    }

    @After
    public void teardown() {
        deleteTable();
        deleteS3Resources();
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

    private void createS3Bucket() {
        amazonS3.createBucket(s3Bucket);
    }

    private void deleteS3Resources() {
        amazonS3.deleteBucket(s3Bucket);
    }

    @Test
    public void testSaveGeneratesNewId() {
        var recipe = DefaultRecipe.getInstance().build();
        assertThat(recipe.getId()).isNull();

        var saved = repository.saveRecipe(recipe);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void testSaveWithExistingId() {
        var id = "12345";
        var recipe = DefaultRecipe.getInstance().withId(id).build();

        repository.saveRecipe(recipe);

        var result = repository.findById(id);
        assertThat(result).isNotEmpty();
        assertThat(result).get().hasFieldOrPropertyWithValue("id", id);
    }

    @Test
    public void testSavesPersistsAllData() {
        var testRecipe = DefaultRecipe.getInstance().build();

        repository.saveRecipe(testRecipe);

        var savedRecipe = repository.findById(testRecipe.getId());
        assertThat(savedRecipe).isPresent();
        assertThat(savedRecipe).get().isEqualTo(testRecipe);
    }

    @Test
    public void testSaveStoresImageInS3() {
        var testRecipe = DefaultRecipe.getInstance().withRawImageBase64(testImageOfABus()).build();

        var recipe = repository.saveRecipe(testRecipe);

        assertThat(recipe.getImageUrlSafe()).isNotEmpty();
        assertThat(recipe.getImageUrlSafe().get()).isEqualTo(
                "https://" + s3Bucket + ".s3.amazonaws.com/" + TEST_IMAGE_FILE_NAME);

        var object = amazonS3.getObject(new GetObjectRequest(s3Bucket, TEST_IMAGE_FILE_NAME));
        assertThat(object).isNotNull();
    }

    @Test
    public void testFindByName() {
        var testRecipe = DefaultRecipe.getInstance().build();
        repository.saveRecipe(testRecipe);

        var results = repository.findByName("Cheese Omlette");

        assertThat(results).isNotEmpty();
        assertThat(results).first().isEqualTo(testRecipe);
    }

    @Test
    public void testDelete() {
        var testRecipeId = "12345";
        var testRecipe = DefaultRecipe.getInstance().withId(testRecipeId).build();

        repository.saveRecipe(testRecipe);

        var savedRecipe = repository.findById(testRecipeId);
        assertThat(savedRecipe).isNotEmpty();

        repository.deleteById(testRecipeId);

        var result = repository.findById(testRecipe.getId());
        assertThat(result).isEmpty();
    }
}
