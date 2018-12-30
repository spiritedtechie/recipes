package com.blah.recipes.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@DynamoDBDocument
public class Quantity {

    public enum Unit {
        SMALL, MEDIUM, LARGE,
        WHOLE,
        CUP,
        PINT,
        MILLILETRE, LITRE,
        TEASPOON, TABLESPOON,
        KILOGRAMS, GRAMS,
        SPRINKLE, PINCH,
        INCHES
    }

    private Integer value;

    private Unit unit;

    public Quantity() {
    }

    public Quantity(Integer value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    @DynamoDBAttribute
    @JsonProperty
    public Integer getValue() {
        return value;
    }

    @JsonProperty
    public void setValue(Integer value) {
        this.value = value;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConvertedEnum
    @JsonProperty
    public Unit getUnit() {
        return unit;
    }

    @JsonProperty
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return Objects.equals(value, quantity.value) &&
                unit == quantity.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        return "Quantity{" +
                "value=" + value +
                ", unit=" + unit +
                '}';
    }
}
