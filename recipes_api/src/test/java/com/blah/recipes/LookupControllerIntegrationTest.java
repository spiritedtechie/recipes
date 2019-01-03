package com.blah.recipes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LookupController.class)
public class LookupControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Before
    public void setup() {
    }

    @Test
    public void testGetUnits() throws Exception {
        var resultActions = mvc.perform(get("/ingredient/quantity/units"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasItem("SMALL")));
    }

    @Test
    public void testGetPreparationMethods() throws Exception {
        var resultActions = mvc.perform(get("/ingredient/preparation/methods"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasItem("SLICE")));
    }

    @Test
    public void testGetPreparationStyles() throws Exception {
        var resultActions = mvc.perform(get("/ingredient/preparation/styles"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasItem("THICK")));
    }

}
