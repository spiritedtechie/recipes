package com.blah.recipes.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

import java.util.Objects;

@DynamoDBDocument
public class Ingredient {

    private String name;

    private Quantity quantity;

    private Preparation preparation;

    public Ingredient() {
    }

    public Ingredient(String name, Quantity quantity, Preparation preparation) {
        this.name = name;
        this.quantity = quantity;
        this.preparation = preparation;
    }

    public Ingredient(String name, Quantity quantity) {
        this.name = name;
        this.quantity = quantity;
        this.preparation = null;
    }

    @DynamoDBAttribute
    public String getName() {
        return name;
    }

    @DynamoDBAttribute
    public Quantity getQuantity() {
        return quantity;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConvertedEnum
    public Preparation getPreparation() {
        return preparation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public void setPreparation(Preparation preparation) {
        this.preparation = preparation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(quantity, that.quantity) &&
                preparation == that.preparation;
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
