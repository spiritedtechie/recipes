package com.blah.recipes;

import com.blah.recipes.model.Preparation;
import com.blah.recipes.model.Quantity;
import com.blah.recipes.model.Recipe;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Timed(value = "recipes.lookups", percentiles = {0.5, 0.95, 0.999}, histogram = true)
public class LookupController {

    public LookupController() {

    }

    @RequestMapping(value = "/ingredient/quantity/units", method = RequestMethod.GET)
    public Quantity.Unit[] getUnits() {
        return Quantity.Unit.values();
    }

    @RequestMapping(value = "/ingredient/preparation/methods", method = RequestMethod.GET)
    public Preparation.Method[] getPreparationMethods() {
        return Preparation.Method.values();
    }

    @RequestMapping(value = "/ingredient/preparation/styles", method = RequestMethod.GET)
    public Preparation.Style[] getPreparationStyles() {
        return Preparation.Style.values();
    }
}
