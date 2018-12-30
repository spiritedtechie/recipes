package com.blah.recipes.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Optional;

@DynamoDBDocument
public class Ingredient {

    private String name;

    private Quantity quantity;

    private Optional<Preparation> preparation = Optional.empty();

    public Ingredient() {
    }

    public Ingredient(String name, Quantity quantity, Preparation preparation) {
        this.name = name;
        this.quantity = quantity;
        this.preparation = Optional.ofNullable(preparation);
    }

    public Ingredient(String name, Quantity quantity) {
        this(name, quantity, null);
    }

    @DynamoDBAttribute
    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute
    @JsonProperty
    public Quantity getQuantity() {
        return quantity;
    }

    @JsonProperty
    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    @DynamoDBAttribute
    @JsonIgnore
    public Preparation getPreparation() {
        return preparation.orElse(null);
    }

    @DynamoDBAttribute
    @JsonIgnore
    public void setPreparation(Preparation preparation) {
        this.setPreparation(Optional.ofNullable(preparation));
    }

    @DynamoDBIgnore
    @JsonProperty("preparation")
    public Optional<Preparation> getPreparationSafe() {
        return preparation;
    }

    @JsonProperty
    public void setPreparation(Optional<Preparation> preparation) {
        this.preparation = preparation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(preparation, that.preparation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, preparation);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", preparation=" + preparation +
                '}';
    }
}
