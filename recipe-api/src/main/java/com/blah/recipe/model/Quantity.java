package com.blah.recipe.model;

public class Quantity {

    private Integer value;

    private Unit unit;

    public Quantity(Integer value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public Integer getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }
}
