package com.blah.recipes.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelJsonObjectMapperTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
    }

    @Test
    public void testSerialiseDeserialise() throws Exception {
        var testRecipe = DefaultRecipe.getInstance().build();

        var serialised = objectMapper.writeValueAsString(testRecipe);
        System.out.println("Serialised: " + serialised);
        var deserialised = objectMapper.readValue(serialised, Recipe.class);
        System.out.println("Deserialised: " + deserialised);

        assertThat(deserialised).isEqualTo(testRecipe);
    }

}
