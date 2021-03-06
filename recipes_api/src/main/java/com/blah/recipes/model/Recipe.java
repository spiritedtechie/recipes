package com.blah.recipes.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@DynamoDBTable(tableName = "recipes")
public class Recipe {

    private String id;

    private String name;

    private transient Optional<String> rawImageBase64 = Optional.empty();

    private Optional<String> imageUrl = Optional.empty();

    private List<Ingredient> ingredients;

    private Instructions instructions;

    public Recipe() {
    }

    public Recipe(String name, List<Ingredient> ingredients, Instructions instructions) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "idx_global_name")
    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBIgnore
    @JsonProperty
    public Optional<String> getRawImageBase64() {
        return rawImageBase64;
    }

    @JsonProperty
    public void setRawImageBase64(Optional<String> rawImageBase64) {
        this.rawImageBase64 = rawImageBase64;
    }

    @DynamoDBAttribute
    @JsonIgnore
    public String getImageUrl() {
        return imageUrl.orElse(null);
    }

    @JsonIgnore
    public void setImageUrl(String imageUrl) {
        this.imageUrl = Optional.ofNullable(imageUrl);
    }

    @DynamoDBIgnore
    @JsonProperty(value = "imageUrl")
    public Optional<String> getImageUrlSafe() {
        return this.imageUrl;
    }

    @DynamoDBIgnore
    @JsonProperty
    public void setImageUrl(Optional<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    @DynamoDBAttribute
    @JsonProperty
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @JsonProperty
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @DynamoDBAttribute
    @JsonProperty
    public Instructions getInstructions() {
        return instructions;
    }

    @JsonProperty
    public void setInstructions(Instructions instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var recipe = (Recipe) o;
        return Objects.equals(id, recipe.id) &&
                Objects.equals(name, recipe.name) &&
                Objects.equals(rawImageBase64, recipe.rawImageBase64) &&
                Objects.equals(imageUrl, recipe.imageUrl) &&
                Objects.equals(ingredients, recipe.ingredients) &&
                Objects.equals(instructions, recipe.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rawImageBase64, imageUrl, ingredients, instructions);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl=" + imageUrl +
                ", ingredients=" + ingredients +
                ", instructions=" + instructions +
                '}';
    }
}
