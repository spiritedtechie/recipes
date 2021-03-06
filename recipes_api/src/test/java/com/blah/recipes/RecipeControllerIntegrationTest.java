package com.blah.recipes;

import com.blah.recipes.model.DefaultRecipe;
import com.blah.recipes.model.Recipe;
import com.blah.recipes.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerIntegrationTest {

    private static final String RECIPE_ID = "fba40fa2-70be-4fab-8cd7-2319a20eca9c";

    private ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RecipeRepository recipeRepository;

    private ObjectMapper om;

    @Before
    public void setup() throws Exception {
        om = new ObjectMapper();
        om.registerModule(new Jdk8Module());
    }

    @Test
    public void testGetAllRecipes() throws Exception {
        var recipe = DefaultRecipe.getInstance().build();
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        var resultActions = mvc.perform(get("/recipes"));

        verify(recipeRepository).findAll();
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("Cheese Omlette")));
    }

    @Test
    public void testGetRecipe() throws Exception {
        var recipe = DefaultRecipe.getInstance().build();
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.of(recipe));

        var resultActions = mvc.perform(get("/recipes/" + RECIPE_ID));

        verify(recipeRepository).findById(RECIPE_ID);
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cheese Omlette")));
    }

    @Test
    public void testGetRecipeNotFound() throws Exception {
        when(recipeRepository.findById(RECIPE_ID)).thenReturn(Optional.empty());

        var resultActions = mvc.perform(get("/recipes/" + RECIPE_ID));

        verify(recipeRepository).findById(RECIPE_ID);
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void testNewRecipe() throws Exception {
        var recipe = DefaultRecipe.getInstance().build();
        var recipeJson = om.writeValueAsString(recipe);
        when(recipeRepository.saveRecipe(recipe)).thenReturn(recipe);
        System.out.println("New recipe JSON: " + recipeJson);

        var resultActions = mvc.perform(
                post("/recipes")
                        .content(recipeJson)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        verify(recipeRepository).saveRecipe(recipe);
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(recipeJson, true));
    }

    @Test
    public void testNewDefaultRecipe() throws Exception {
        var defaultRecipe = DefaultRecipe.getInstance().build();
        var defaultRecipeJson = om.writeValueAsString(defaultRecipe);
        when(recipeRepository.saveRecipe(defaultRecipe)).thenReturn(defaultRecipe);

        var resultActions = mvc.perform(post("/recipes/default"));

        verify(recipeRepository).saveRecipe(defaultRecipe);
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(defaultRecipeJson, true));
    }

    @Test
    public void testUpdateRecipe() throws Exception {
        var idToUpdate = RECIPE_ID;
        var recipe = DefaultRecipe.getInstance().build();
        var recipeJson = om.writeValueAsString(recipe);
        var recipeWithId = DefaultRecipe.getInstance().withId(idToUpdate).build();
        var recipeWithIdJson = om.writeValueAsString(recipeWithId);
        when(recipeRepository.saveRecipe(recipeWithId)).thenReturn(recipeWithId);

        var resultActions = mvc.perform(
                put(String.format("/recipes/%s", idToUpdate))
                        .content(recipeJson)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        verify(recipeRepository).saveRecipe(captor.capture());
        var capturedRecipe = captor.getValue();
        assertThat(capturedRecipe).isEqualTo(recipeWithId);
        assertThat(capturedRecipe.getId()).isEqualTo(idToUpdate);
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(recipeWithIdJson, true));
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        var idToDelete = RECIPE_ID;
        doNothing().when(recipeRepository).deleteById(idToDelete);

        var resultActions = mvc.perform(delete(String.format("/recipes/%s", idToDelete)));

        verify(recipeRepository).deleteById(idToDelete);
        resultActions.andExpect(status().isOk());
    }

}
