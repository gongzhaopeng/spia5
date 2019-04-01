package com.atzu68.spia5.tacocloud.repository;

import com.atzu68.spia5.tacocloud.model.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository
        extends CrudRepository<Ingredient, String> {
}
