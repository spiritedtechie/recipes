package com.blah.recipes;

import com.blah.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends NotFoundException {

    private static final long serialVersionUID = -1465085270351089000L;

    public RecipeNotFoundException(String id) {
        super(id);
    }

    public String getResourceName() {
        return "Recipe";
    }
}
