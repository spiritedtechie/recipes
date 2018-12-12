package com.blah.recipe.model;

import java.util.LinkedList;
import java.util.List;

public class Instructions {

    private List<String> steps = new LinkedList<>();

    public void addStep(Integer stepPosition, String details) {
        steps.add(stepPosition, details);
    }

    public void addNextStep(String details) {
        steps.add(details);
    }

    public List<String> getSteps() {
        return steps;
    }
}
