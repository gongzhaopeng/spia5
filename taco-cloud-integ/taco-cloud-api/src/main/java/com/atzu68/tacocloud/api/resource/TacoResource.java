package com.atzu68.tacocloud.api.resource;

import com.atzu68.tacocloud.domain.Taco;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.util.Date;
import java.util.List;

@Relation(value = "taco", collectionRelation = "tacos")
public class TacoResource
        extends ResourceSupport {

    @Getter
    private final String name;

    @Getter
    private final Date createdAt;

    @Getter
    private final List<IngredientResource> ingredients;

    public TacoResource(
            Taco taco,
            IngredientResourceAssembler ingredientResourceAssembler) {

        name = taco.getName();
        createdAt = taco.getCreatedAt();
        ingredients =
                ingredientResourceAssembler
                        .toResources(taco.getIngredients());
    }
}
