package com.atzu68.tacocloud.api.resource;

import com.atzu68.tacocloud.api.DesignTacoController;
import com.atzu68.tacocloud.domain.Taco;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class TacoResourceAssembler
        extends ResourceAssemblerSupport<Taco, TacoResource> {

    private final IngredientResourceAssembler ingredientResourceAssembler;
    
    public TacoResourceAssembler(
            IngredientResourceAssembler ingredientResourceAssembler) {

        super(DesignTacoController.class, TacoResource.class);

        this.ingredientResourceAssembler = ingredientResourceAssembler;
    }

    @Override
    protected TacoResource instantiateResource(Taco entity) {
        return new TacoResource(entity, ingredientResourceAssembler);
    }

    @Override
    public TacoResource toResource(Taco taco) {
        return createResourceWithId(taco.getId(), taco);
    }
}
