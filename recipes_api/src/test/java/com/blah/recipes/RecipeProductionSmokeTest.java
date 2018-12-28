package com.blah.recipes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
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

    @Test
    public void shouldReturnDefaultRecipe() {
        var recipesJson = restTemplate.getForObject(
                "http://localhost:" + port + "/recipes",
                String.class);

        LOGGER.info("Json returned: " + recipesJson);

        assertThat(recipesJson).contains("Cheese Omlette");
    }
}
