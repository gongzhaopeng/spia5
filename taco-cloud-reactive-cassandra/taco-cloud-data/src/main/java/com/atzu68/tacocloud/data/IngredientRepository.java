package com.atzu68.tacocloud.data;

import com.atzu68.tacocloud.domain.Ingredient;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IngredientRepository
        extends ReactiveCrudRepository<Ingredient, String> {
}
