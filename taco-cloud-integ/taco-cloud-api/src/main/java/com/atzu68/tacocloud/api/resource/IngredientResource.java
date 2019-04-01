package com.atzu68.tacocloud.api.resource;

import com.atzu68.tacocloud.domain.Ingredient;
import com.atzu68.tacocloud.domain.Ingredient.Type;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

public class IngredientResource
        extends ResourceSupport {

    @Getter
    private String name;

    @Getter
    private Type type;

    public IngredientResource(Ingredient ingredient) {

        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }
}
