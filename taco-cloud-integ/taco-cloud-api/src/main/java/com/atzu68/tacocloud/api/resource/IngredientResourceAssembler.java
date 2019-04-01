package com.atzu68.tacocloud.api.resource;

import com.atzu68.tacocloud.api.IngredientController;
import com.atzu68.tacocloud.domain.Ingredient;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class IngredientResourceAssembler
        extends ResourceAssemblerSupport<Ingredient, IngredientResource> {

    public IngredientResourceAssembler() {
        super(IngredientController.class, IngredientResource.class);
    }

    @Override
    protected IngredientResource instantiateResource(Ingredient entity) {
        return new IngredientResource(entity);
    }

    @Override
    public IngredientResource toResource(Ingredient ingredient) {
        return createResourceWithId(ingredient.getId(), ingredient);
    }
}
