package com.blah.recipes;

import com.blah.recipes.model.DefaultRecipe;
import com.blah.recipes.model.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RecipeProductionSmokeTest {

    private static Logger LOGGER = LoggerFactory.getLogger(RecipeProductionSmokeTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @Before
    public void setup() {
        baseUrl = "http://localhost:" + port + "/recipes";
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
        var id = "fba40fa2-70be-4fab-8cd7-2319a20eca9c";
        var recipe = DefaultRecipe.getInstance().withName("Test Recipe").withId(id).build();
        restTemplate.delete(baseUrl + "/" + id);
        var responseEntity = restTemplate.getForEntity(baseUrl + "/" + id, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        restTemplate.put(baseUrl + "/" + id, recipe);
        responseEntity = restTemplate.getForEntity(baseUrl + "/" + id, String.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains("Test Recipe");
    }
}
