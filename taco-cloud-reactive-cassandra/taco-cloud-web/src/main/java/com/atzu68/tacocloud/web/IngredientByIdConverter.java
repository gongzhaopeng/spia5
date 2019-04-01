package com.atzu68.tacocloud.web;

import com.atzu68.tacocloud.domain.IngredientUDT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IngredientByIdConverter
        implements Converter<String, IngredientUDT> {

    @Autowired
    public IngredientByIdConverter() {
    }

    @Override
    public IngredientUDT convert(String s) {

        var ingredientUDT =
                new IngredientUDT(null, null);
        ingredientUDT.setId(s);

        return ingredientUDT;
    }
}
