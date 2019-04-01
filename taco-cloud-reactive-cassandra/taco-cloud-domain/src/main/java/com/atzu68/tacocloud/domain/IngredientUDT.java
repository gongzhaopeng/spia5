package com.atzu68.tacocloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Data
@NoArgsConstructor(
        access = AccessLevel.PRIVATE,
        force = true)
@UserDefinedType("ingredient")
public class IngredientUDT {

    @JsonIgnore
    private String id;
    private String name;
    private Ingredient.Type type;

    public static IngredientUDT of(
            Ingredient ingredient) {

        return new IngredientUDT(
                ingredient.getName(),
                ingredient.getType());
    }

    public IngredientUDT(final String name,
                         final Ingredient.Type type) {
        this.name = name;
        this.type = type;
    }
}
