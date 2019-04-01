package com.atzu68.tacocloud.api;

import com.atzu68.tacocloud.api.resource.IngredientResourceAssembler;
import com.atzu68.tacocloud.api.resource.TacoResourceAssembler;
import com.atzu68.tacocloud.domain.Taco;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;

@Configuration
public class HateoasConfig {

    @Bean
    public TacoResourceAssembler tacoResourceAssembler(
            IngredientResourceAssembler ingredientResourceAssembler) {
        return new TacoResourceAssembler(ingredientResourceAssembler);
    }

    @Bean
    public IngredientResourceAssembler ingredientResourceAssembler() {
        return new IngredientResourceAssembler();
    }

    @Bean
    public ResourceProcessor<PagedResources<Resource<Taco>>> tacoProcessor(
            EntityLinks entityLinks) {

        return new ResourceProcessor<PagedResources<Resource<Taco>>>() {
            @Override
            public PagedResources<Resource<Taco>> process(
                    PagedResources<Resource<Taco>> resources) {

                resources.add(
                        entityLinks.linkFor(Taco.class)
                                .slash("recent")
                                .withRel("recents"));

                return resources;
            }
        };
    }
}