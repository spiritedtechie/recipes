package com.blah.recipe;

import com.blah.recipe.model.DefaultRecipe;
import com.blah.recipe.repository.RecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RecipeRepository recipeRepository;

    @Test
    public void testGetAllRecipes() throws Exception {
        when(recipeRepository.findAll()).thenReturn(List.of(DefaultRecipe.build()));

        mvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("Boiled eggs")));
    }

}
