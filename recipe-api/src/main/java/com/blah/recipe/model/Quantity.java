package com.blah.recipe.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

import java.util.Objects;

@DynamoDBDocument
public class Quantity {

    private Integer value;

    private Unit unit;

    public Quantity() {
    }

    public Quantity(Integer value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    @DynamoDBAttribute
    public Integer getValue() {
        return value;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConvertedEnum
    public Unit getUnit() {
        return unit;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

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
