package com.atzu68.tacocloud.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@UserDefinedType("taco")
public class TacoUDT {

    private String name;
    private List<IngredientUDT> ingredients;

    public static TacoUDT of(Taco taco) {

        return new TacoUDT(
                taco.getName(),
                taco.getIngredients());
    }

    public TacoUDT(final String name,
                   final List<IngredientUDT> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }
}
