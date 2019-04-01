package com.atzu68.spia5.eureka.client.ingredient.resttemplate;

import com.atzu68.spia5.eureka.client.ingredient.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Conditional(NotFeignAndNotWebClientCondition.class)
@Service
public class IngredientServiceClient {

    private final RestTemplate restTemplate;

    @Autowired
    public IngredientServiceClient(
            @LoadBalanced RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public Ingredient getIngredientById(
            String ingredientId) {

        return restTemplate.getForObject(
                "http://ingredient-service/ingredients/{id}",
                Ingredient.class,
                ingredientId);
    }

    public Iterable<Ingredient> getAllIngredients() {

        var ingredients = restTemplate.getForObject(
                "http://ingredient-service/ingredients",
                Ingredient[].class);

        return Optional.ofNullable(ingredients)
                .map(List::of).orElse(List.of());
    }
}
