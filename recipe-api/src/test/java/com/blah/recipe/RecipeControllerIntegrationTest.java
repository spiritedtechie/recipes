package com.blah.recipe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testDefaultRecipe() throws Exception {

        mvc.perform(get("/recipe/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Boiled eggs")));
    }

}
