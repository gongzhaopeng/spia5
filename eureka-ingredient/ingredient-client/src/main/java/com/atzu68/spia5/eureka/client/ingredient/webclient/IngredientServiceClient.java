package com.atzu68.spia5.eureka.client.ingredient.webclient;

import com.atzu68.spia5.eureka.client.ingredient.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Profile("webclient")
@Service
public class IngredientServiceClient {

    private final WebClient.Builder webclientBuilder;

    @Autowired
    public IngredientServiceClient(
            @LoadBalanced WebClient.Builder webclientBuilder) {

        this.webclientBuilder = webclientBuilder;
    }

    public Mono<Ingredient> getIngredientById(
            String ingredientId) {
        return webclientBuilder.build()
                .get()
                .uri("http://ingredient-service/ingredients/{id}",
                        ingredientId)
                .retrieve()
                .bodyToMono(Ingredient.class);
    }

    public Flux<Ingredient> getAllIngredients() {
        return webclientBuilder.build()
                .get()
                .uri("http://ingredient-service/ingredients")
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<Ingredient>() {
                });
    }
}
