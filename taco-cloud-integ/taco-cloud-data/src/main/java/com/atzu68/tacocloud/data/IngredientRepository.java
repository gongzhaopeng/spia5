package com.atzu68.tacocloud.data;

import com.atzu68.tacocloud.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
public interface IngredientRepository
        extends CrudRepository<Ingredient, String> {
}
