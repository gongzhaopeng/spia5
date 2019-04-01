package com.atzu68.tacocloud.web;

import com.atzu68.tacocloud.data.IngredientRepository;
import com.atzu68.tacocloud.domain.Ingredient;
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
