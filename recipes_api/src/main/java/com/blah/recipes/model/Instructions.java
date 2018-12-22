package com.blah.recipes.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@DynamoDBDocument
public class Instructions {

    private List<String> steps = new LinkedList<>();

    public Instructions() {
    }

    public void addStep(Integer stepPosition, String details) {
        steps.add(stepPosition, details);
    }

    public void addNextStep(String details) {
        steps.add(details);
    }

    @DynamoDBAttribute
    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instructions that = (Instructions) o;
        return Objects.equals(steps, that.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(steps);
    }

    @Override
    public String toString() {
        return "Instructions{" +
                "steps=" + steps +
                '}';
    }
}
