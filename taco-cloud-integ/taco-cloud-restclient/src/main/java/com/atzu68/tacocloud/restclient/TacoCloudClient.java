package com.atzu68.tacocloud.restclient;

import com.atzu68.tacocloud.domain.Ingredient;
import com.atzu68.tacocloud.domain.Taco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;

@Service
@Slf4j
public class TacoCloudClient {

    private final RestTemplate restTemplate;
    private final Traverson traverson;

    @Autowired
    public TacoCloudClient(
            RestTemplate restTemplate,
            Traverson traverson) {

        this.restTemplate = restTemplate;
        this.traverson = traverson;
    }

    public Ingredient getIngredientById(String ingredientId) {

        var uri = buildWithBaseUri(uriComponentsBuilder ->
                uriComponentsBuilder.pathSegment("ingredients", ingredientId));

        return restTemplate.getForObject(uri, Ingredient.class);
    }

    public List<Ingredient> getAllIngredients() {

        var uri = buildWithBaseUri(uriComponentsBuilder ->
                uriComponentsBuilder.pathSegment("ingredients"));

        return restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Ingredient>>() {
                }).getBody();
    }

    public void updateIngredient(Ingredient ingredient) {

        var uri = buildWithBaseUri(uriComponentsBuilder ->
                uriComponentsBuilder.pathSegment("ingredients", ingredient.getId()));

        restTemplate.put(uri, ingredient);
    }

    public Ingredient createIngredient(Ingredient ingredient) {

        var uri = buildWithBaseUri(uriComponentsBuilder ->
                uriComponentsBuilder.pathSegment("ingredients"));

        return restTemplate.postForObject(uri, ingredient, Ingredient.class);
    }

    public URI createIngredientThenLocation(Ingredient ingredient) {

        var uri = buildWithBaseUri(uriComponentsBuilder ->
                uriComponentsBuilder.pathSegment("ingredients"));

        return restTemplate.postForLocation(uri, ingredient);
    }

    public Ingredient createIngredientThenResponseEntity(
            Ingredient ingredient) {

        var uri = buildWithBaseUri(uriComponentsBuilder ->
                uriComponentsBuilder.pathSegment("ingredients"));

        var responseEntity = restTemplate.postForEntity(
                uri, ingredient, Ingredient.class);

        log.info("New resource created at {}",
                responseEntity.getHeaders().getLocation());

        return responseEntity.getBody();
    }

    public void deleteIngredient(Ingredient ingredient) {

        var uri = buildWithBaseUri(uriComponentsBuilder ->
                uriComponentsBuilder.pathSegment("ingredients", ingredient.getId()));

        restTemplate.delete(uri);
    }

    public Iterable<Ingredient> getAllIngredientsWithTraverson() {

        var ingredientsType =
                new ParameterizedTypeReference<Resources<Ingredient>>() {
                };

        var ingredientsRes = traverson
                .follow("ingredients")
                .toObject(ingredientsType);

        return ingredientsRes.getContent();
    }

    public Ingredient addIngredient(Ingredient ingredient) {

        var ingredientsUrl = traverson
                .follow("ingredients")
                .asLink()
                .getHref();

        log.info(
                "Ingredients Url through traverson: {}",
                ingredientsUrl);

        return restTemplate.postForObject(
                ingredientsUrl,
                ingredient,
                Ingredient.class);
    }

    public Iterable<Taco> getRecentTacosWithTraverson() {

        var tacosType = new ParameterizedTypeReference<Resources<Taco>>() {
        };

        var tacosRes = traverson
//                .follow("tacos", "recents")
                .follow("tacos")
                .follow("recents")
                .toObject(tacosType);

        return tacosRes.getContent();
    }

    private URI buildWithBaseUri(
            Consumer<UriComponentsBuilder> appendingPathSegments) {

        var baseUri = UriComponentsBuilder.fromHttpUrl(
                "http://localhost:8080");
        appendingPathSegments.accept(baseUri);

        return baseUri.build().toUri();
    }
}
