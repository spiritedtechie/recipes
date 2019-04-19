package com.blah.recipes;

import com.amazonaws.services.s3.AmazonS3;
import com.blah.imageutils.TestImages;
import com.blah.recipes.model.DefaultRecipe;
import com.blah.recipes.model.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.function.Supplier;

import static com.blah.imageutils.TestImages.TEST_IMAGE_FILE_NAME;
import static com.blah.imageutils.TestImages.TEST_IMAGE_FILE_UUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RecipeProductionSmokeTest {

    private static Logger LOGGER = LoggerFactory.getLogger(RecipeProductionSmokeTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket.images}")
    private String s3Bucket;

    @MockBean
    private Supplier<UUID> uuidSupplier;

    private String baseUrl;

    @Before
    public void setup() {
        baseUrl = "http://localhost:" + port + "/recipes";
        when(uuidSupplier.get()).thenReturn(TEST_IMAGE_FILE_UUID);
        amazonS3.deleteObject(s3Bucket, TEST_IMAGE_FILE_NAME);
    }

    @Test
    public void shouldReturnDefaultRecipe() {
        var responseEntity = restTemplate.getForEntity(baseUrl, String.class);

        assertThat(responseEntity).isNotNull();
        LOGGER.info("Json returned: " + responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains("Cheese Omlette");
    }

    @Test
    public void shouldStoreAndRetrieveRecipe() {
        var testRecipeId = "fba40fa2-70be-4fab-8cd7-2319a20eca9c";
        var testRecipe = buildRecipe(testRecipeId);
        deleteTestRecipe(testRecipe);

        restTemplate.postForEntity(baseUrl, testRecipe, Recipe.class);

        var responseEntity = restTemplate.getForEntity(baseUrl + "/" + testRecipeId, Recipe.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Recipe recipe = responseEntity.getBody();
        assertThat(recipe.getName()).contains("Test Recipe");
    }

    private void deleteTestRecipe(Recipe recipe) {
        restTemplate.delete(baseUrl + "/" + recipe.getId());

        var responseEntity = restTemplate.getForEntity(baseUrl + "/" + recipe.getId(), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Recipe buildRecipe(String id) {
        return DefaultRecipe.getInstance()
                .withName("Test Recipe")
                .withRawImageBase64(TestImages.testImageOfABus())
                .withId(id)
                .build();
    }
}
