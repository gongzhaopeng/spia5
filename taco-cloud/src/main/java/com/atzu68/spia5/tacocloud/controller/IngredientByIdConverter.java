package com.atzu68.spia5.tacocloud.controller;

import com.atzu68.spia5.tacocloud.model.Ingredient;
import com.atzu68.spia5.tacocloud.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IngredientByIdConverter
        implements Converter<String, Ingredient> {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientByIdConverter(
            IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient convert(String s) {

        return ingredientRepository
                .findById(s).orElse(null);
    }
}
