package com.blah.recipe.model;

import java.util.Optional;

public class Ingredient {

    private String name;

    private Quantity quantity;

    private Optional<Preparation> preparation= Optional.empty();

    public Ingredient(String name, Quantity quantity, Optional<Preparation> preparation) {
        this.name = name;
        this.quantity = quantity;
        this.preparation = preparation;
    }

    public String getName() {
        return name;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Optional<Preparation> getPreparation() {
        return preparation;
    }
}
